package com.xc.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.pos.sdk.accessory.POIGeneralAPI;
import com.pos.sdk.emvcore.IPosEmvCoreListener;
import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvCardInfoConstraints;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvOnlineConstraints;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvResultConstraints;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvTransDataConstraints;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.pos.sdk.security.POIHsmManage;
import com.pos.sdk.utils.PosByteArray;

import com.xc.payment.data.TransactionData;
import com.xc.payment.data.TransactionRepository;
import com.xc.payment.device.DeviceConfig;
import com.xc.payment.emv.utils.EmvCard;
import com.xc.payment.emv.ErrorCodeType;
import java.util.ArrayList;
import java.util.List;

import com.xc.payment.utils.tlv.BerTag;
import com.xc.payment.utils.tlv.BerTlv;
import com.xc.payment.utils.tlv.BerTlvBuilder;
import com.xc.payment.utils.tlv.BerTlvParser;
import com.xc.payment.utils.tlv.BerTlvs;
import com.xc.payment.utils.tlv.HexUtil;

import com.example.flutter.MainActivity;

public class FlutterTransaction {

    private POIEmvCoreManager emvCoreManager;
    private NewPOIEmvCoreListener emvCoreListener;
    private TransactionData transData;

    @SuppressLint("DefaultLocale")
    public String startTransaction() {
        final int MOLD_SELECT_TRANS = 0;
        final int MSG_SUCCESS = 1;
        final int MSG_FAILED = 2;
        // AudioBeep mBeeper = null;

        emvCoreManager = POIEmvCoreManager.getDefault();
        emvCoreListener = new NewPOIEmvCoreListener();

        transData = new TransactionData();

        try {
            int transType = POIEmvCoreManager.EMV_PAYMENT;
            Bundle bundle = new Bundle();

            bundle.putInt(EmvTransDataConstraints.TRANS_TYPE, transType);

            int mode = 0;
            mode |= POIEmvCoreManager.DEVICE_CONTACT;
            mode |= POIEmvCoreManager.DEVICE_CONTACTLESS;
            mode |= POIEmvCoreManager.DEVICE_MAGSTRIPE;
            bundle.putInt(EmvTransDataConstraints.TRANS_MODE, mode);

            bundle.putInt(EmvTransDataConstraints.TRANS_TIMEOUT, 60);
            bundle.putBoolean(EmvTransDataConstraints.SPECIAL_CONTACT, true); // normal is true
            bundle.putBoolean(EmvTransDataConstraints.SPECIAL_MAGSTRIPE, true); // normal is true
            bundle.putBoolean(EmvTransDataConstraints.TRANS_FALLBACK, true);

            bundle.putLong(EmvTransDataConstraints.TRANS_AMOUNT, 100);
            bundle.putLong(EmvTransDataConstraints.TRANS_AMOUNT_OTHER, 100);

            transData.setTransType(transType);
            transData.setTransAmount((double) 100);
            transData.setTransAmountOther((double) 100);
            transData.setTransResult(PosEmvErrorCode.EMV_OTHER_ERROR);

            int result = 0;
            try {
                result = emvCoreManager.startTransaction(bundle, emvCoreListener);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            if (PosEmvErrorCode.EXCEPTION_ERROR == result || PosEmvErrorCode.EMV_ENCRYPT_ERROR == result) {
                return "err_" + String.valueOf(result);
            }
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "err_" + e.toString();
        }
    }

    public class NewPOIEmvCoreListener extends IPosEmvCoreListener.Stub {

        @Override
        public void onTransactionResult(final int result, final Bundle bundle) {

            MainActivity mainActivity = MainActivity.instance;
            boolean isSuccess = result > 0 && result <= 6 && result != 3;
            byte[] data;
            int vasResult;
            byte[] vasData;
            byte[] vasMerchant;
            int encryptResult;
            byte[] encryptData;
            byte[] scriptResult;

            //final data array to keep the card data in an array of strings
            List<String> finalData = new ArrayList<String>();
            String codeData = "code=" + String.valueOf(result);
            String codeTextData = "codeText=" + ErrorCodeType.getErrorCodeType(result);
            String isSuccessData = "isSuccess=" + String.valueOf(isSuccess);
            finalData.add(codeTextData);
            finalData.add(codeData);
            finalData.add(isSuccessData);

            switch (result) {
                case PosEmvErrorCode.EMV_CANCEL:
                case PosEmvErrorCode.EMV_TIMEOUT:
                    mainActivity.sendDataToFlutter(String.join("&", finalData));
                    return;
                default:
                    break;
            }

            data = bundle.getByteArray(POIEmvCoreManager.EmvResultConstraints.EMV_DATA);

            if (data != null) {
                BerTlvBuilder tlvBuilder = new BerTlvBuilder();
                BerTlvParser tlvParser = new BerTlvParser();
                BerTlvs tlvs = tlvParser.parse(data);
                for (BerTlv tlv : tlvs.getList()) {
                    tlvBuilder.addBerTlv(tlv);
                    if (tlv.isConstructed()) {
                        for (BerTlv value : tlv.getValues()) {
                            finalData.add(String.format("HEX_%1$-4s", value.getTag().getBerTagHex())
                                    + "=" + value.getHexValue());
                        }
                        finalData.add(String.format("HEX_%1$-4s=<<", tlv.getTag().getBerTagHex()));
                    } else {
                        finalData.add("HEX_" + tlv.getTag().getBerTagHex() + "=" + tlv.getHexValue());
                    }
                }

                data = tlvBuilder.buildArray();
            }
            if (data != null) {
                EmvCard emvCard = new EmvCard(data);
                finalData.add("EMV_DATA=" + emvCard.toString());
            }

            String finalDataResult = String.join("&", finalData);
            //this will send the data as a string to flutter
            mainActivity.sendDataToFlutter(finalDataResult);
        }

        @Override
        public void onRequestOnlineProcess(final Bundle bundle) {
            byte[] data;
            int vasResult;
            byte[] vasData;
            byte[] vasMerchant;
            int encryptResult;
            byte[] encryptData;

            vasResult = bundle.getInt(EmvOnlineConstraints.APPLE_RESULT, PosEmvErrorCode.APPLE_VAS_UNTREATED);
            encryptResult = bundle.getInt(EmvOnlineConstraints.ENCRYPT_RESULT, PosEmvErrorCode.EMV_UNENCRYPTED);

            vasData = bundle.getByteArray(EmvOnlineConstraints.APPLE_DATA);

            vasMerchant = bundle.getByteArray(EmvOnlineConstraints.APPLE_MERCHANT);
            data = bundle.getByteArray(EmvOnlineConstraints.EMV_DATA);

            encryptData = bundle.getByteArray(EmvOnlineConstraints.ENCRYPT_DATA);

            if (data != null) {
                BerTlvBuilder tlvBuilder = new BerTlvBuilder();
                BerTlvParser tlvParser = new BerTlvParser();
                BerTlvs tlvs = tlvParser.parse(data);
                for (BerTlv tlv : tlvs.getList()) {
                    tlvBuilder.addBerTlv(tlv);
                }

                if (encryptResult == PosEmvErrorCode.EMV_OK && encryptData != null) {
                    BerTlvs encryptTlvs = new BerTlvParser().parse(encryptData);
                    for (BerTlv tlv : encryptTlvs.getList()) {
                        tlvBuilder.addBerTlv(tlv);
                    }
                }

                data = tlvBuilder.buildArray();
            }

            transData.setTransData(data);

            if (vasResult != PosEmvErrorCode.APPLE_VAS_UNTREATED) {
                BerTlvBuilder tlvBuilder = new BerTlvBuilder();
                if (vasData != null) {
                    BerTlvs tlvs = new BerTlvParser().parse(vasData);
                    for (BerTlv tlv : tlvs.getList()) {
                        tlvBuilder.addBerTlv(tlv);
                    }
                }

                if (vasMerchant != null) {
                    BerTlvs tlvs = new BerTlvParser().parse(vasMerchant);
                    for (BerTlv tlv : tlvs.getList()) {
                        tlvBuilder.addBerTlv(tlv);
                    }
                }

                transData.setAppleVasResult(vasResult);
                transData.setAppleVasData(tlvBuilder.buildArray());
            }

            Bundle outBundle = new Bundle();

            outBundle.putInt(EmvOnlineConstraints.OUT_AUTH_RESP_CODE, 0);
            emvCoreManager.onSetOnlineResponse(outBundle);

        }

        @Override
        public void onEmvProcess(final int type, Bundle bundle) {
            // called
            System.out.println("onEmvProcess");
            // do nothing
        }

        @Override
        public void onSelectApplication(final List<String> list, boolean isFirstSelect) {
            emvCoreManager.onSetSelectResponse(0);
        }

        @Override
        public void onConfirmCardInfo(int mode, Bundle bundle) {
            Bundle outBundle = new Bundle();
            if (mode == POIEmvCoreManager.CMD_AMOUNT_CONFIG) {
                outBundle.putString(EmvCardInfoConstraints.OUT_AMOUNT, "11");
                outBundle.putString(EmvCardInfoConstraints.OUT_AMOUNT_OTHER, "22");
            } else if (mode == POIEmvCoreManager.CMD_TRY_OTHER_APPLICATION) {
                outBundle.putBoolean(EmvCardInfoConstraints.OUT_CONFIRM, true);
            } else if (mode == POIEmvCoreManager.CMD_ISSUER_REFERRAL) {
                outBundle.putBoolean(EmvCardInfoConstraints.OUT_CONFIRM, true);
            }
            emvCoreManager.onSetCardInfoResponse(outBundle);
        }

        @Override
        public void onKernelType(int type) {
            transData.setCardType(type);
        }

        @Override
        public void onSecondTapCard() {
            // do nothing
        }

        @Override
        public void onRequestInputPin(final Bundle bndl) {
            Bundle bundle = new Bundle();
            bundle.putInt(POIEmvCoreManager.EmvPinConstraints.OUT_PIN_VERIFY_RESULT, POIEmvCoreManager.EmvPinConstraints.VERIFY_SUCCESS);
            bundle.putInt(POIEmvCoreManager.EmvPinConstraints.OUT_PIN_TRY_COUNTER, 0);
            bundle.putByteArray(POIEmvCoreManager.EmvPinConstraints.OUT_PIN_BLOCK, HexUtil.parseHex("0102030405060708"));
            POIEmvCoreManager.getDefault().onSetPinResponse(bundle);
                   
        }

    }

}
