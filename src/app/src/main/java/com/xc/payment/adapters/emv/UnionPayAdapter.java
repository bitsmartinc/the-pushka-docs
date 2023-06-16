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

public class UnionPayAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private QuicsParameter quicsParameter;

    public UnionPayAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.UNIONPAY_SUPPORT_PBOC);
        list.add(Config.UNIONPAY_SUPPORT_QPBOC);
        list.add(Config.UNIONPAY_SUPPORT_CONTACT);
        list.add(Config.UNIONPAY_OFFLINE_ONLY);
        list.add(Config.UNIONPAY_SUPPORT_ONLINE_PIN);
        list.add(Config.UNIONPAY_SUPPORT_SIGNATURE);
        list.add(Config.UNIONPAY_SUPPORT_CDCVM);
        params.put("UnionPay", list);

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
            case UNIONPAY_SUPPORT_PBOC:
            case UNIONPAY_SUPPORT_QPBOC:
            case UNIONPAY_SUPPORT_CONTACT:
            case UNIONPAY_OFFLINE_ONLY:
            case UNIONPAY_SUPPORT_ONLINE_PIN:
            case UNIONPAY_SUPPORT_SIGNATURE:
            case UNIONPAY_SUPPORT_CDCVM:
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
                return getLimit(CardScheme.UNIONPAY, config);
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
            case UNIONPAY_SUPPORT_PBOC:
            case UNIONPAY_SUPPORT_QPBOC:
            case UNIONPAY_SUPPORT_CONTACT:
            case UNIONPAY_OFFLINE_ONLY:
            case UNIONPAY_SUPPORT_ONLINE_PIN:
            case UNIONPAY_SUPPORT_SIGNATURE:
            case UNIONPAY_SUPPORT_CDCVM:
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
                    result = setLimit(CardScheme.UNIONPAY, config, Integer.parseInt((String) data));
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
        if (quicsParameter == null) {
            quicsParameter = readQuicsParameter();
        }
        switch (config) {
            case UNIONPAY_SUPPORT_QPBOC:
                value = quicsParameter.SupportQPBOC;
                break;
            case UNIONPAY_SUPPORT_CONTACT:
                value = quicsParameter.SupportContact;
                break;
            case UNIONPAY_OFFLINE_ONLY:
                value = quicsParameter.OfflineOnly;
                break;
            case UNIONPAY_SUPPORT_ONLINE_PIN:
                value = quicsParameter.SupportOnlinePIN;
                break;
            case UNIONPAY_SUPPORT_SIGNATURE:
                value = quicsParameter.SupportSignature;
                break;
            case UNIONPAY_SUPPORT_CDCVM:
                value = quicsParameter.SupportCDCVM;
                break;
            case RISK_CTL_CHECK:
                value = quicsParameter.CTLCheck;
                break;
            case RISK_CFL_CHECK:
                value = quicsParameter.CFLCheck;
                break;
            case RISK_CVM_CHECK:
                value = quicsParameter.CVMCheck;
                break;
            case RISK_STATUS_CHECK:
                value = quicsParameter.StatusCheck;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = quicsParameter.ZeroAmountCheck;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = quicsParameter.ZeroAmountPath;
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
            case UNIONPAY_SUPPORT_PBOC:
                return true;
            case UNIONPAY_SUPPORT_QPBOC:
                quicsParameter.SupportQPBOC = value;
                break;
            case UNIONPAY_SUPPORT_CONTACT:
                quicsParameter.SupportContact = value;
                break;
            case UNIONPAY_OFFLINE_ONLY:
                quicsParameter.OfflineOnly = value;
                break;
            case UNIONPAY_SUPPORT_ONLINE_PIN:
                quicsParameter.SupportOnlinePIN = value;
                break;
            case UNIONPAY_SUPPORT_SIGNATURE:
                quicsParameter.SupportSignature = value;
                break;
            case UNIONPAY_SUPPORT_CDCVM:
                quicsParameter.SupportCDCVM = value;
                break;
            case RISK_CTL_CHECK:
                quicsParameter.CTLCheck = value;
                break;
            case RISK_CFL_CHECK:
                quicsParameter.CFLCheck = value;
                break;
            case RISK_CVM_CHECK:
                quicsParameter.CVMCheck = value;
                break;
            case RISK_STATUS_CHECK:
                quicsParameter.StatusCheck = value;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                quicsParameter.ZeroAmountCheck = value;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                quicsParameter.ZeroAmountPath = value;
                break;
            default:
                break;
        }
        result = writeQuicsParameter(quicsParameter);
        return result;
    }

    private QuicsParameter readQuicsParameter() {
        Bundle bundle = new Bundle();
        QuicsParameter quicsParameter = new QuicsParameter();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_UNIONPAY, bundle)) {
            byte[] config = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
            byte[] qualifiers = new byte[4];
            byte[] entryPoint = new byte[1];
            byte[] statusZero = new byte[1];

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(config);
            for (BerTlv tlv : tlvs.getList()) {
                if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_QUALIFIERS))) {
                    qualifiers = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_ENTRY_POINT))) {
                    entryPoint = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_STATUS_ZERO_AMOUNT))) {
                    statusZero = tlv.getBytesValue();
                }
            }

            if ((qualifiers[0] & 0x20) != 0) {
                quicsParameter.SupportQPBOC = true;
            }
            if ((qualifiers[0] & 0x10) != 0) {
                quicsParameter.SupportContact = true;
            }
            if ((qualifiers[0] & 0x08) != 0) {
                quicsParameter.OfflineOnly = true;
            }
            if ((qualifiers[0] & 0x04) != 0) {
                quicsParameter.SupportOnlinePIN = true;
            }
            if ((qualifiers[0] & 0x02) != 0) {
                quicsParameter.SupportSignature = true;
            }

            if ((qualifiers[2] & 0x40) != 0) {
                quicsParameter.SupportCDCVM = true;
            }

            if ((entryPoint[0] & 0x80) != 0) {
                quicsParameter.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                quicsParameter.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                quicsParameter.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                quicsParameter.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                quicsParameter.CVMCheck = true;
            }

            if ((statusZero[0] & 0x01) != 0) {
                quicsParameter.ZeroAmountPath = true;
            }
        }
        return quicsParameter;
    }

    private boolean writeQuicsParameter(QuicsParameter quicsParameter) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] qualifiers = new byte[4];
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (quicsParameter.SupportQPBOC) {
            qualifiers[0] |= 0x20;
        }
        if (quicsParameter.SupportContact) {
            qualifiers[0] |= 0x10;
        }
        if (quicsParameter.OfflineOnly) {
            qualifiers[0] |= 0x08;
        }
        if (quicsParameter.SupportOnlinePIN) {
            qualifiers[0] |= 0x04;
        }
        if (quicsParameter.SupportSignature) {
            qualifiers[0] |= 0x02;
        }
        if (quicsParameter.SupportCDCVM) {
            qualifiers[2] |= 0x40;
        }

        if (quicsParameter.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (quicsParameter.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (quicsParameter.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (quicsParameter.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (quicsParameter.CVMCheck) {
            entryPoint[0] |= 0x08;
        }

        if (quicsParameter.ZeroAmountPath) {
            statusZero[0] |= 0x01;
        } else {
            statusZero[0] |= 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_QUALIFIERS), qualifiers));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_STATUS_ZERO_AMOUNT), statusZero));
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_UNIONPAY, bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    private static class QuicsParameter {
        boolean SupportQPBOC;
        boolean SupportContact;
        boolean OfflineOnly;
        boolean SupportOnlinePIN;
        boolean SupportSignature;
        boolean SupportCDCVM;

        boolean CTLCheck;
        boolean CVMCheck;
        boolean CFLCheck;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
