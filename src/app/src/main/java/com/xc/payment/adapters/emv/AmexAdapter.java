package com.xc.payment.adapters.emv;

import android.os.Bundle;
import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager.EmvTerminalConstraints;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.RegexUtils;
import com.xc.payment.utils.tlv.BerTag;
import com.xc.payment.utils.tlv.BerTlv;
import com.xc.payment.utils.tlv.BerTlvBuilder;
import com.xc.payment.utils.tlv.BerTlvParser;
import com.xc.payment.utils.tlv.BerTlvs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AmexAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private AmexParameter amexParameter;

    public AmexAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.AMEX_SUPPORT_CONTACT);
        list.add(Config.AMEX_SUPPORT_MAGSTRIPE);
        list.add(Config.AMEX_SUPPORT_EMV);
        list.add(Config.AMEX_TRY_ANOTHER_INTERFACE);
        list.add(Config.AMEX_SUPPORT_CDCVM);
        list.add(Config.AMEX_SUPPORT_ONLINE_PIN);
        list.add(Config.AMEX_SUPPORT_SIGNATURE);
        list.add(Config.AMEX_OFFLINE_ONLY);
        list.add(Config.AMEX_EXEMPT_NO_CVM_CHECK);
        list.add(Config.AMEX_DELAYED_AUTHORIZATION);
        list.add(Config.AMEX_SUPPORT_DRL);
        params.put("Amex", list);

        list = new ArrayList<>();
        list.add(Config.RISK_CTL_CHECK);
        list.add(Config.RISK_TRANS_LIMIT);
        list.add(Config.RISK_CVM_CHECK);
        list.add(Config.RISK_CVM_LIMIT);
        list.add(Config.RISK_CFL_CHECK);
        list.add(Config.RISK_CONTACTLESS_FLOOR_LIMIT);
        list.add(Config.RISK_FLOOR_LIMIT);
        list.add(Config.RISK_STATUS_CHECK);
        list.add(Config.RISK_ZERO_AMOUNT_CHECK);
        list.add(Config.RISK_ZERO_AMOUNT_PATH);
        params.put("Risk", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case AMEX_SUPPORT_CONTACT:
            case AMEX_SUPPORT_MAGSTRIPE:
            case AMEX_SUPPORT_EMV:
            case AMEX_TRY_ANOTHER_INTERFACE:
            case AMEX_SUPPORT_CDCVM:
            case AMEX_SUPPORT_ONLINE_PIN:
            case AMEX_SUPPORT_SIGNATURE:
            case AMEX_OFFLINE_ONLY:
            case AMEX_EXEMPT_NO_CVM_CHECK:
            case AMEX_DELAYED_AUTHORIZATION:
            case AMEX_SUPPORT_DRL:
            case RISK_CTL_CHECK:
            case RISK_CVM_CHECK:
            case RISK_CFL_CHECK:
            case RISK_STATUS_CHECK:
            case RISK_ZERO_AMOUNT_CHECK:
            case RISK_ZERO_AMOUNT_PATH:
                return getConfig(config);
            case RISK_TRANS_LIMIT:
            case RISK_CVM_LIMIT:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_FLOOR_LIMIT:
                return getLimit(CardScheme.AMERICAN_EXPRESS, config);
            default:
                return "";
        }
    }

    @Override
    public Object[] getValueList(Config config) {
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case AMEX_SUPPORT_CONTACT:
            case AMEX_SUPPORT_MAGSTRIPE:
            case AMEX_SUPPORT_EMV:
            case AMEX_TRY_ANOTHER_INTERFACE:
            case AMEX_SUPPORT_CDCVM:
            case AMEX_SUPPORT_ONLINE_PIN:
            case AMEX_SUPPORT_SIGNATURE:
            case AMEX_OFFLINE_ONLY:
            case AMEX_EXEMPT_NO_CVM_CHECK:
            case AMEX_DELAYED_AUTHORIZATION:
            case AMEX_SUPPORT_DRL:
            case RISK_CTL_CHECK:
            case RISK_CVM_CHECK:
            case RISK_CFL_CHECK:
            case RISK_STATUS_CHECK:
            case RISK_ZERO_AMOUNT_CHECK:
            case RISK_ZERO_AMOUNT_PATH:
                result = setConfig(config, data);
                break;
            case RISK_TRANS_LIMIT:
            case RISK_CVM_LIMIT:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_FLOOR_LIMIT:
                if (TextUtils.isEmpty((String) data)) {
                    data = "0";
                }
                if (result = RegexUtils.isNumber((String) data)) {
                    result = setLimit(CardScheme.AMERICAN_EXPRESS, config, Integer.parseInt((String) data));
                }
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private boolean getConfig(Config config) {
        boolean value;
        if (amexParameter == null) {
            amexParameter = readAmexParameter();
        }
        switch (config) {
            case AMEX_SUPPORT_CONTACT:
                value = amexParameter.SupportContact;
                break;
            case AMEX_SUPPORT_MAGSTRIPE:
                value = amexParameter.SupportMagstripe;
                break;
            case AMEX_SUPPORT_EMV:
                value = amexParameter.SupportEMV;
                break;
            case AMEX_TRY_ANOTHER_INTERFACE:
                value = amexParameter.TryAnotherInterface;
                break;
            case AMEX_SUPPORT_CDCVM:
                value = amexParameter.SupportCDCVM;
                break;
            case AMEX_SUPPORT_ONLINE_PIN:
                value = amexParameter.SupportOnlinePIN;
                break;
            case AMEX_SUPPORT_SIGNATURE:
                value = amexParameter.SupportSignature;
                break;
            case AMEX_OFFLINE_ONLY:
                value = amexParameter.OfflineOnly;
                break;
            case AMEX_EXEMPT_NO_CVM_CHECK:
                value = amexParameter.ExemptNoCVMCheck;
                break;
            case AMEX_DELAYED_AUTHORIZATION:
                value = amexParameter.DelayedAuthorization;
                break;
            case AMEX_SUPPORT_DRL:
                value = amexParameter.SupportDRL;
                break;
            case RISK_CTL_CHECK:
                value = amexParameter.CTLCheck;
                break;
            case RISK_CFL_CHECK:
                value = amexParameter.CFLCheck;
                break;
            case RISK_CVM_CHECK:
                value = amexParameter.CVMCheck;
                break;
            case RISK_STATUS_CHECK:
                value = amexParameter.StatusCheck;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = amexParameter.ZeroAmountCheck;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = amexParameter.ZeroAmountPath;
                break;
            default:
                value = false;
                break;
        }
        return value;
    }

    private boolean setConfig(Config config, Object data) {
        boolean result;
        boolean value = (boolean) data;
        switch (config) {
            case AMEX_SUPPORT_CONTACT:
                amexParameter.SupportContact = value;
                break;
            case AMEX_SUPPORT_MAGSTRIPE:
                amexParameter.SupportMagstripe = value;
                break;
            case AMEX_SUPPORT_EMV:
                amexParameter.SupportEMV = value;
                break;
            case AMEX_TRY_ANOTHER_INTERFACE:
                amexParameter.TryAnotherInterface = value;
                break;
            case AMEX_SUPPORT_CDCVM:
                amexParameter.SupportCDCVM = value;
                break;
            case AMEX_SUPPORT_ONLINE_PIN:
                amexParameter.SupportOnlinePIN = value;
                break;
            case AMEX_SUPPORT_SIGNATURE:
                amexParameter.SupportSignature = value;
                break;
            case AMEX_OFFLINE_ONLY:
                amexParameter.OfflineOnly = value;
                break;
            case AMEX_EXEMPT_NO_CVM_CHECK:
                amexParameter.ExemptNoCVMCheck = value;
                break;
            case AMEX_DELAYED_AUTHORIZATION:
                amexParameter.DelayedAuthorization = value;
                break;
            case AMEX_SUPPORT_DRL:
                amexParameter.SupportDRL = value;
                break;
            case RISK_CTL_CHECK:
                amexParameter.CTLCheck = value;
                break;
            case RISK_CFL_CHECK:
                amexParameter.CFLCheck = value;
                break;
            case RISK_CVM_CHECK:
                amexParameter.CVMCheck = value;
                break;
            case RISK_STATUS_CHECK:
                amexParameter.StatusCheck = value;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                amexParameter.ZeroAmountCheck = value;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                amexParameter.ZeroAmountPath = value;
                break;
            default:
                break;
        }
        result = writeAmexParameter(amexParameter);
        return result;
    }

    private AmexParameter readAmexParameter() {
        Bundle bundle = new Bundle();
        AmexParameter amexParameter = new AmexParameter();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_AMEX, bundle)) {
            byte[] configParam = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
            byte[] qualifiers = new byte[4];
            byte[] kernelConfig = new byte[1];
            byte[] entryPoint = new byte[1];
            byte[] statusZero = new byte[1];

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(configParam);
            for (BerTlv tlv : tlvs.getList()) {
                if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_QUALIFIERS))) {
                    qualifiers = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_KERNEL_CONFIG))) {
                    kernelConfig = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_ENTRY_POINT))) {
                    entryPoint = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_STATUS_ZERO_AMOUNT))) {
                    statusZero = tlv.getBytesValue();
                }
            }

            if ((qualifiers[0] & 0x80) != 0) {
                amexParameter.SupportContact = true;
            }
            if ((qualifiers[0] & 0x40) != 0) {
                amexParameter.SupportMagstripe = true;
            }
            if ((qualifiers[0] & 0x04) != 0) {
                amexParameter.TryAnotherInterface = true;
            }
            if ((qualifiers[1] & 0x80) != 0) {
                amexParameter.SupportCDCVM = true;
            }
            if ((qualifiers[1] & 0x40) != 0) {
                amexParameter.SupportOnlinePIN = true;
            }
            if ((qualifiers[1] & 0x20) != 0) {
                amexParameter.SupportSignature = true;
            }
            if ((qualifiers[2] & 0x80) != 0) {
                amexParameter.OfflineOnly = true;
            }
            if ((qualifiers[3] & 0x80) != 0) {
                amexParameter.ExemptNoCVMCheck = true;
            }
            if ((qualifiers[3] & 0x40) != 0) {
                amexParameter.DelayedAuthorization = true;
            }

            if ((kernelConfig[0] & 0x01) != 0) {
                amexParameter.SupportDRL = true;
            }

            if ((entryPoint[0] & 0x80) != 0) {
                amexParameter.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                amexParameter.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                amexParameter.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                amexParameter.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                amexParameter.CVMCheck = true;
            }

            if ((statusZero[0] & 0x01) != 0) {
                amexParameter.ZeroAmountPath = true;
            }
        }
        return amexParameter;
    }

    private boolean writeAmexParameter(AmexParameter amexParameter) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] qualifiers = new byte[4];
        byte[] kernelConfig = new byte[1];
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (amexParameter.SupportContact) {
            qualifiers[0] |= 0x80;
        }
        if (amexParameter.SupportMagstripe) {
            qualifiers[0] |= 0x40;
        }
        if (amexParameter.TryAnotherInterface) {
            qualifiers[0] |= 0x04;
        }
        if (amexParameter.SupportCDCVM) {
            qualifiers[1] |= 0x80;
        }
        if (amexParameter.SupportOnlinePIN) {
            qualifiers[1] |= 0x40;
        }
        if (amexParameter.SupportSignature) {
            qualifiers[1] |= 0x20;
        }
        if (amexParameter.OfflineOnly) {
            qualifiers[2] |= 0x80;
        }
        if (amexParameter.ExemptNoCVMCheck) {
            qualifiers[3] |= 0x80;
        }
        if (amexParameter.DelayedAuthorization) {
            qualifiers[3] |= 0x40;
        }

        if (amexParameter.SupportDRL) {
            kernelConfig[0] |= 0x01;
        }

        if (amexParameter.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (amexParameter.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (amexParameter.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (amexParameter.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (amexParameter.CVMCheck) {
            entryPoint[0] |= 0x08;
        }

        if (amexParameter.ZeroAmountPath) {
            statusZero[0] |= 0x01;
        } else {
            statusZero[0] |= 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_QUALIFIERS), qualifiers));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_KERNEL_CONFIG), kernelConfig));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_AMEX_SET_STATUS_ZERO_AMOUNT), statusZero));
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_AMEX, bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    private static class AmexParameter {
        boolean SupportContact;
        boolean SupportMagstripe;
        boolean SupportEMV;
        boolean TryAnotherInterface;
        boolean SupportCDCVM;
        boolean SupportOnlinePIN;
        boolean SupportSignature;
        boolean OfflineOnly;
        boolean ExemptNoCVMCheck;
        boolean DelayedAuthorization;
        boolean SupportDRL;

        boolean CTLCheck;
        boolean CVMCheck;
        boolean CFLCheck;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
