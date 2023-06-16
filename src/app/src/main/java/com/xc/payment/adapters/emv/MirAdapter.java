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

public class MirAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private MirParameter mirParameter;

    public MirAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.MIR_SUPPORT_ONLINE_PIN);
        list.add(Config.MIR_SUPPORT_SIGNATURE);
        list.add(Config.MIR_SUPPORT_CDCVM);
        list.add(Config.MIR_UNABLE_ONLINE);
        list.add(Config.MIR_SUPPORT_CONTACT);
        list.add(Config.MIR_OFFLINE_ONLY);
        list.add(Config.MIR_DELAYED_AUTHORIZATION);
        list.add(Config.MIR_ATM);
        params.put("MIR", list);

        list = new ArrayList<>();
        list.add(Config.RISK_CTL_CHECK);
        list.add(Config.RISK_TRANS_LIMIT);
        list.add(Config.RISK_DYNAMIC_LIMIT);
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
            case MIR_SUPPORT_ONLINE_PIN:
            case MIR_SUPPORT_SIGNATURE:
            case MIR_SUPPORT_CDCVM:
            case MIR_UNABLE_ONLINE:
            case MIR_SUPPORT_CONTACT:
            case MIR_OFFLINE_ONLY:
            case MIR_DELAYED_AUTHORIZATION:
            case MIR_ATM:
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
            case RISK_DYNAMIC_LIMIT:
                return getLimit(CardScheme.MIR, config);
            default:
                break;
        }
        return "";
    }

    @Override
    public Object[] getValueList(Config config) {
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case MIR_SUPPORT_ONLINE_PIN:
            case MIR_SUPPORT_SIGNATURE:
            case MIR_SUPPORT_CDCVM:
            case MIR_UNABLE_ONLINE:
            case MIR_SUPPORT_CONTACT:
            case MIR_OFFLINE_ONLY:
            case MIR_DELAYED_AUTHORIZATION:
            case MIR_ATM:
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
            case RISK_DYNAMIC_LIMIT:
                if (TextUtils.isEmpty((String) data)) {
                    data = "0";
                }
                if (result = RegexUtils.isNumber((String) data)) {
                    result = setLimit(CardScheme.MIR, config, Integer.parseInt((String) data));
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
        if (mirParameter == null) {
            mirParameter = readMirParameter();
        }
        switch (config) {
            case MIR_SUPPORT_ONLINE_PIN:
                value = mirParameter.SupportOnlinePIN;
                break;
            case MIR_SUPPORT_SIGNATURE:
                value = mirParameter.SupportSignature;
                break;
            case MIR_SUPPORT_CDCVM:
                value = mirParameter.SupportCDCVM;
                break;
            case MIR_UNABLE_ONLINE:
                value = mirParameter.UnableOnline;
                break;
            case MIR_SUPPORT_CONTACT:
                value = mirParameter.SupportContact;
                break;
            case MIR_OFFLINE_ONLY:
                value = mirParameter.OfflineOnly;
                break;
            case MIR_DELAYED_AUTHORIZATION:
                value = mirParameter.DelayedAuthorization;
                break;
            case MIR_ATM:
                value = mirParameter.ATM;
                break;
            case RISK_CTL_CHECK:
                value = mirParameter.CTLCheck;
                break;
            case RISK_CFL_CHECK:
                value = mirParameter.CFLCheck;
                break;
            case RISK_CVM_CHECK:
                value = mirParameter.CVMCheck;
                break;
            case RISK_STATUS_CHECK:
                value = mirParameter.StatusCheck;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = mirParameter.ZeroAmountCheck;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = mirParameter.ZeroAmountPath;
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
            case MIR_SUPPORT_ONLINE_PIN:
                mirParameter.SupportOnlinePIN = value;
                break;
            case MIR_SUPPORT_SIGNATURE:
                mirParameter.SupportSignature = value;
                break;
            case MIR_SUPPORT_CDCVM:
                mirParameter.SupportCDCVM = value;
                break;
            case MIR_UNABLE_ONLINE:
                mirParameter.UnableOnline = value;
                break;
            case MIR_SUPPORT_CONTACT:
                mirParameter.SupportContact = value;
                break;
            case MIR_OFFLINE_ONLY:
                mirParameter.OfflineOnly = value;
                break;
            case MIR_DELAYED_AUTHORIZATION:
                mirParameter.DelayedAuthorization = value;
                break;
            case MIR_ATM:
                mirParameter.ATM = value;
                break;
            case RISK_CTL_CHECK:
                mirParameter.CTLCheck = value;
                break;
            case RISK_CFL_CHECK:
                mirParameter.CFLCheck = value;
                break;
            case RISK_CVM_CHECK:
                mirParameter.CVMCheck = value;
                break;
            case RISK_STATUS_CHECK:
                mirParameter.StatusCheck = value;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                mirParameter.ZeroAmountCheck = value;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                mirParameter.ZeroAmountPath = value;
                break;
            default:
                break;
        }
        result = writeMirParameter(mirParameter);
        return result;
    }

    private MirParameter readMirParameter() {
        Bundle bundle = new Bundle();
        MirParameter mirParameter = new MirParameter();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_MIR, bundle)) {
            byte[] config = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
            byte[] qualifiers = new byte[4];
            byte[] entryPoint = new byte[1];
            byte[] statusZero = new byte[1];

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(config);
            for (BerTlv tlv : tlvs.getList()) {
                if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_QUALIFIERS))) {
                    qualifiers = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_ENTRY_POINT))) {
                    entryPoint = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_STATUS_ZERO_AMOUNT))) {
                    statusZero = tlv.getBytesValue();
                }
            }

            if ((qualifiers[0] & 0x80) != 0) {
                mirParameter.SupportOnlinePIN = true;
            }
            if ((qualifiers[0] & 0x40) != 0) {
                mirParameter.SupportSignature = true;
            }
            if ((qualifiers[0] & 0x20) != 0) {
                mirParameter.SupportCDCVM = true;
            }
            if ((qualifiers[0] & 0x10) != 0) {
                mirParameter.UnableOnline = true;
            }
            if ((qualifiers[0] & 0x08) != 0) {
                mirParameter.SupportContact = true;
            }
            if ((qualifiers[0] & 0x04) != 0) {
                mirParameter.OfflineOnly = true;
            }
            if ((qualifiers[0] & 0x02) != 0) {
                mirParameter.DelayedAuthorization = true;
            }
            if ((qualifiers[0] & 0x01) != 0) {
                mirParameter.ATM = true;
            }

            if ((entryPoint[0] & 0x80) != 0) {
                mirParameter.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                mirParameter.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                mirParameter.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                mirParameter.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                mirParameter.CVMCheck = true;
            }

            if ((statusZero[0] & 0x01) != 0) {
                mirParameter.ZeroAmountPath = true;
            }
        }
        return mirParameter;
    }

    private boolean writeMirParameter(MirParameter mirParameter) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] qualifiers = new byte[4];
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (mirParameter.SupportOnlinePIN) {
            qualifiers[0] |= 0x80;
        }
        if (mirParameter.SupportSignature) {
            qualifiers[0] |= 0x40;
        }
        if (mirParameter.SupportCDCVM) {
            qualifiers[0] |= 0x20;
        }
        if (mirParameter.UnableOnline) {
            qualifiers[0] |= 0x10;
        }
        if (mirParameter.SupportContact) {
            qualifiers[0] |= 0x08;
        }
        if (mirParameter.OfflineOnly) {
            qualifiers[0] |= 0x04;
        }
        if (mirParameter.DelayedAuthorization) {
            qualifiers[0] |= 0x02;
        }
        if (mirParameter.ATM) {
            qualifiers[0] |= 0x01;
        }

        if (mirParameter.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (mirParameter.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (mirParameter.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (mirParameter.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (mirParameter.CVMCheck) {
            entryPoint[0] |= 0x08;
        }

        if (mirParameter.ZeroAmountPath) {
            statusZero[0] |= 0x01;
        } else {
            statusZero[0] |= 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_QUALIFIERS), qualifiers));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MIR_SET_STATUS_ZERO_AMOUNT), statusZero));
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_MIR, bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    private static class MirParameter {
        boolean SupportOnlinePIN;
        boolean SupportSignature;
        boolean SupportCDCVM;
        boolean UnableOnline;
        boolean SupportContact;
        boolean OfflineOnly;
        boolean DelayedAuthorization;
        boolean ATM;

        boolean CTLCheck;
        boolean CVMCheck;
        boolean CFLCheck;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
