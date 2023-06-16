package com.xc.payment.adapters.emv;

import android.os.Bundle;
import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvDrlConstraints;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.tlv.BerTag;
import com.xc.payment.utils.tlv.BerTlv;
import com.xc.payment.utils.tlv.BerTlvBuilder;
import com.xc.payment.utils.tlv.BerTlvParser;
import com.xc.payment.utils.tlv.BerTlvs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DrlAdapter implements IConfigAdapter {

    public static int DRL_VISA_TYPE = 1;
    public static int DRL_AMEX_TYPE = 2;

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private DynamicReaderLimit[] dynamicReaderLimits;
    private int dynamicReaderLimitType;
    private int dynamicReaderLimitIndex;
    private String[] dynamicReaderLimitNames;

    public DrlAdapter(int type) {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();

        if (type == DRL_VISA_TYPE) {
            dynamicReaderLimitType = EmvDrlConstraints.TYPE_VISA;
            dynamicReaderLimits = new DynamicReaderLimit[16];
        } else if (type == DRL_AMEX_TYPE) {
            dynamicReaderLimitType = EmvDrlConstraints.TYPE_AMEX;
            dynamicReaderLimits = new DynamicReaderLimit[17];
        }

        for (int i = 0; i < dynamicReaderLimits.length; i++) {
            dynamicReaderLimits[i] = new DynamicReaderLimit();
        }

        readDynamicReaderLimit();

        List<Config> list = new ArrayList<>();
        list.add(Config.DRL_INDEX);
        params.put("Name", list);

        dynamicReaderLimitIndex = 0;
        dynamicReaderLimitNames = new String[dynamicReaderLimits.length];
        for (int i = 0; i < dynamicReaderLimits.length; i++) {
            dynamicReaderLimitNames[i] = ("" + i);
        }

        list = new ArrayList<>();
        list.add(Config.DRL_ACTIVE);
        if (type == DRL_VISA_TYPE) {
            list.add(Config.DRL_PROGRAM_ID);
        }
        params.put("DRL", list);

        list = new ArrayList<>();
        list.add(Config.RISK_CTL_CHECK);
        list.add(Config.RISK_TRANS_LIMIT);
        list.add(Config.RISK_CVM_CHECK);
        list.add(Config.RISK_CVM_LIMIT);
        list.add(Config.RISK_CFL_CHECK);
        list.add(Config.RISK_CONTACTLESS_FLOOR_LIMIT);
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
            case DRL_INDEX:
                return dynamicReaderLimitNames[dynamicReaderLimitIndex];
            case DRL_ACTIVE:
            case DRL_PROGRAM_ID:
            case RISK_CTL_CHECK:
            case RISK_TRANS_LIMIT:
            case RISK_CVM_CHECK:
            case RISK_CVM_LIMIT:
            case RISK_CFL_CHECK:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_STATUS_CHECK:
            case RISK_ZERO_AMOUNT_CHECK:
            case RISK_ZERO_AMOUNT_PATH:
                return getConfig(dynamicReaderLimitIndex, config);
            default:
                return null;
        }
    }

    @Override
    public Object[] getValueList(Config config) {
        if (config == Config.DRL_INDEX) {
            return dynamicReaderLimitNames;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case DRL_INDEX:
                for (int i = 0; i < dynamicReaderLimitNames.length; i++) {
                    if (dynamicReaderLimitNames[i].equals(data)) {
                        dynamicReaderLimitIndex = i;
                    }
                }
                result = true;
                break;
            case DRL_ACTIVE:
            case DRL_PROGRAM_ID:
            case RISK_CTL_CHECK:
            case RISK_TRANS_LIMIT:
            case RISK_CVM_CHECK:
            case RISK_CVM_LIMIT:
            case RISK_CFL_CHECK:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_STATUS_CHECK:
            case RISK_ZERO_AMOUNT_CHECK:
            case RISK_ZERO_AMOUNT_PATH:
                result = setConfig(dynamicReaderLimitIndex, config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        switch (config) {
            case DRL_ACTIVE:
                value = String.valueOf(dynamicReaderLimits[index].Active);
                break;
            case DRL_PROGRAM_ID:
                if (dynamicReaderLimits[index].ProgramID != null) {
                    value = String.valueOf(dynamicReaderLimits[index].ProgramID);
                }
                break;
            case RISK_CTL_CHECK:
                value = String.valueOf(dynamicReaderLimits[index].CTLCheck);
                break;
            case RISK_TRANS_LIMIT:
                value = String.valueOf(dynamicReaderLimits[index].TransLimit);
                break;
            case RISK_CVM_CHECK:
                value = String.valueOf(dynamicReaderLimits[index].CVMCheck);
                break;
            case RISK_CVM_LIMIT:
                value = String.valueOf(dynamicReaderLimits[index].CVMLimit);
                break;
            case RISK_CFL_CHECK:
                value = String.valueOf(dynamicReaderLimits[index].CFLCheck);
                break;
            case RISK_CONTACTLESS_FLOOR_LIMIT:
                value = String.valueOf(dynamicReaderLimits[index].FloorLimit);
                break;
            case RISK_STATUS_CHECK:
                value = String.valueOf(dynamicReaderLimits[index].StatusCheck);
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = String.valueOf(dynamicReaderLimits[index].ZeroAmountCheck);
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = String.valueOf(dynamicReaderLimits[index].ZeroAmountPath);
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(int index, Config config, Object data) {
        boolean result = true;
        String value = String.valueOf(data);
        switch (config) {
            case DRL_ACTIVE:
                dynamicReaderLimits[index].Active = !"False".equalsIgnoreCase(value);
                break;
            case DRL_PROGRAM_ID:
                if (!TextUtils.isEmpty(value)) {
                    dynamicReaderLimits[index].ProgramID = value;
                } else {
                    result = false;
                }
                break;
            case RISK_CTL_CHECK:
                dynamicReaderLimits[index].CTLCheck = !"False".equalsIgnoreCase(value);
                break;
            case RISK_TRANS_LIMIT:
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                dynamicReaderLimits[index].TransLimit = Integer.parseInt(value);
                break;
            case RISK_CVM_CHECK:
                dynamicReaderLimits[index].CVMCheck = !"False".equalsIgnoreCase(value);
                break;
            case RISK_CVM_LIMIT:
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                dynamicReaderLimits[index].CVMLimit = Integer.parseInt(value);
                break;
            case RISK_CFL_CHECK:
                dynamicReaderLimits[index].CFLCheck = !"False".equalsIgnoreCase(value);
                break;
            case RISK_CONTACTLESS_FLOOR_LIMIT:
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                dynamicReaderLimits[index].FloorLimit = Integer.parseInt(value);
                break;
            case RISK_STATUS_CHECK:
                dynamicReaderLimits[index].StatusCheck = !"False".equalsIgnoreCase(value);
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                dynamicReaderLimits[index].ZeroAmountCheck = !"False".equalsIgnoreCase(value);
                break;
            case RISK_ZERO_AMOUNT_PATH:
                dynamicReaderLimits[index].ZeroAmountPath = !"False".equalsIgnoreCase(value);
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = writeDynamicReaderLimit();
        }
        return result;
    }

    private void readDynamicReaderLimit() {
        byte[] data;
        Bundle bundle = new Bundle();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetDRL(dynamicReaderLimitType, bundle)) {
            data = bundle.getByteArray(EmvDrlConstraints.CONFIG);
            if (data == null) {
                return;
            }

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(data);
            List<BerTlv> tlvList = tlvs.findAll(new BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER));

            for (int i = 0; i < tlvList.size(); i++) {
                dynamicReaderLimits[i] = dataToDrl(tlvList.get(i).getBytesValue());
            }
        }
    }

    private boolean writeDynamicReaderLimit() {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();

        for (DynamicReaderLimit dynamicReaderLimit : dynamicReaderLimits) {
            if (dynamicReaderLimit.Active) {
                tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER), drlToData(dynamicReaderLimit)));
            } else {
                tlvBuilder.addEmpty(new BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER));
            }
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvDrlConstraints.CONFIG, tlvBuilder.buildArray());
        return emvCoreManager.EmvSetDRL(dynamicReaderLimitType, bundle) == PosEmvErrorCode.EMV_OK;
    }

    private byte[] drlToData(DynamicReaderLimit limit) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (limit.ProgramID != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvDrlConstraints.TAG_DRL_SET_PROGRAM_ID), limit.ProgramID.getBytes()));
        }
        tlvBuilder.addIntAsHex(new BerTag(EmvDrlConstraints.TAG_DRL_SET_TRANSACTION_LIMIT), limit.TransLimit);
        tlvBuilder.addIntAsHex(new BerTag(EmvDrlConstraints.TAG_DRL_SET_CVM_REQUIRED_LIMIT), limit.CVMLimit);
        tlvBuilder.addIntAsHex(new BerTag(EmvDrlConstraints.TAG_DRL_SET_FLOOR_LIMIT), limit.FloorLimit);

        if (limit.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (limit.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (limit.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (limit.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (limit.CVMCheck) {
            entryPoint[0] |= 0x08;
        }
        if (limit.ZeroAmountPath) {
            statusZero[0] = 0x01;
        } else {
            statusZero[0] = 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvDrlConstraints.TAG_DRL_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvDrlConstraints.TAG_DRL_SET_STATUS_ZERO_AMOUNT), statusZero));
        return tlvBuilder.buildArray();
    }

    private DynamicReaderLimit dataToDrl(byte[] data) {
        DynamicReaderLimit dynamicReaderLimit = new DynamicReaderLimit();

        if (data != null && data.length == 0) {
            dynamicReaderLimit.Active = false;
            return dynamicReaderLimit;
        } else {
            dynamicReaderLimit.Active = true;
        }

        BerTlvs tlvs = new BerTlvParser().parse(data);
        BerTlv tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_PROGRAM_ID));

        if (dynamicReaderLimitType == EmvDrlConstraints.TYPE_VISA) {
            dynamicReaderLimit.ProgramID = tlv.getTextValue();
        }

        tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_TRANSACTION_LIMIT));
        if (tlv != null) {
            dynamicReaderLimit.TransLimit = Integer.parseInt(tlv.getHexValue());
        }
        tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_CVM_REQUIRED_LIMIT));
        if (tlv != null) {
            dynamicReaderLimit.CVMLimit = Integer.parseInt(tlv.getHexValue());
        }
        tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_FLOOR_LIMIT));
        if (tlv != null) {
            dynamicReaderLimit.FloorLimit = Integer.parseInt(tlv.getHexValue());
        }
        tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_ENTRY_POINT));
        if (tlv != null) {
            byte[] entryPoint = tlv.getBytesValue();
            if ((entryPoint[0] & 0x80) != 0) {
                dynamicReaderLimit.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                dynamicReaderLimit.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                dynamicReaderLimit.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                dynamicReaderLimit.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                dynamicReaderLimit.CVMCheck = true;
            }
        }
        tlv = tlvs.find(new BerTag(EmvDrlConstraints.TAG_DRL_SET_STATUS_ZERO_AMOUNT));
        if (tlv != null) {
            byte[] zeroAmountPath = tlv.getBytesValue();
            dynamicReaderLimit.ZeroAmountPath = (zeroAmountPath[0] & 0x01) != 0;
        }

        return dynamicReaderLimit;
    }

    private static class DynamicReaderLimit {
        boolean Active;
        String  ProgramID;
        boolean CTLCheck;
        int     TransLimit;
        boolean CVMCheck;
        int     CVMLimit;
        boolean CFLCheck;
        int     FloorLimit;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
