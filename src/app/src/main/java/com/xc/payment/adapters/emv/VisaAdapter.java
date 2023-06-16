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

public class VisaAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private QvsdcParameter qvsdcParameter;

    public VisaAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.VISA_SUPPORT_MAGSTRIPE);
        list.add(Config.VISA_SUPPORT_QVSDC);
        list.add(Config.VISA_SUPPORT_CONTACT);
        list.add(Config.VISA_OFFLINE_ONLY);
        list.add(Config.VISA_SUPPORT_ONLINE_PIN);
        list.add(Config.VISA_SUPPORT_SIGNATURE);
        list.add(Config.VISA_SUPPORT_ONLINE_ODA);
        list.add(Config.VISA_SUPPORT_ISSUER_SCRIPT_UPDATE);
        list.add(Config.VISA_SUPPORT_CDCVM);
        list.add(Config.VISA_SUPPORT_DRL);
        params.put("Visa", list);

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
            case VISA_SUPPORT_MAGSTRIPE:
            case VISA_SUPPORT_QVSDC:
            case VISA_SUPPORT_CONTACT:
            case VISA_OFFLINE_ONLY:
            case VISA_SUPPORT_ONLINE_PIN:
            case VISA_SUPPORT_SIGNATURE:
            case VISA_SUPPORT_ONLINE_ODA:
            case VISA_SUPPORT_ISSUER_SCRIPT_UPDATE:
            case VISA_SUPPORT_CDCVM:
            case VISA_SUPPORT_DRL:
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
                return getLimit(CardScheme.VISA, config);
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
    public boolean setValue(Config config, Object param) {
        boolean result;
        switch (config) {
            case VISA_SUPPORT_MAGSTRIPE:
            case VISA_SUPPORT_QVSDC:
            case VISA_SUPPORT_CONTACT:
            case VISA_OFFLINE_ONLY:
            case VISA_SUPPORT_ONLINE_PIN:
            case VISA_SUPPORT_SIGNATURE:
            case VISA_SUPPORT_ONLINE_ODA:
            case VISA_SUPPORT_ISSUER_SCRIPT_UPDATE:
            case VISA_SUPPORT_CDCVM:
            case VISA_SUPPORT_DRL:
            case RISK_CTL_CHECK:
            case RISK_CVM_CHECK:
            case RISK_CFL_CHECK:
            case RISK_STATUS_CHECK:
            case RISK_ZERO_AMOUNT_CHECK:
            case RISK_ZERO_AMOUNT_PATH:
                result = setConfig(config, param);
                break;
            case RISK_TRANS_LIMIT:
            case RISK_CVM_LIMIT:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_FLOOR_LIMIT:
                if (TextUtils.isEmpty((String) param)) {
                    param = "0";
                }
                if (result = RegexUtils.isNumber((String) param)) {
                    result = setLimit(CardScheme.VISA, config, Integer.parseInt((String) param));
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
        if (qvsdcParameter == null) {
            qvsdcParameter = readQvsdcParameter();
        }
        switch (config) {
            case VISA_SUPPORT_MAGSTRIPE:
                value = qvsdcParameter.SupportMagstripe;
                break;
            case VISA_SUPPORT_QVSDC:
                value = qvsdcParameter.SupportQVSDC;
                break;
            case VISA_SUPPORT_CONTACT:
                value = qvsdcParameter.SupportContact;
                break;
            case VISA_OFFLINE_ONLY:
                value = qvsdcParameter.OfflineOnly;
                break;
            case VISA_SUPPORT_ONLINE_PIN:
                value = qvsdcParameter.SupportOnlinePIN;
                break;
            case VISA_SUPPORT_SIGNATURE:
                value = qvsdcParameter.SupportSignature;
                break;
            case VISA_SUPPORT_ONLINE_ODA:
                value = qvsdcParameter.SupportOnlineODA;
                break;
            case VISA_SUPPORT_ISSUER_SCRIPT_UPDATE:
                value = qvsdcParameter.SupportIssuerScriptUpdate;
                break;
            case VISA_SUPPORT_CDCVM:
                value = qvsdcParameter.SupportCDCVM;
                break;
            case VISA_SUPPORT_DRL:
                value = qvsdcParameter.SupportDRL;
                break;
            case RISK_CTL_CHECK:
                value = qvsdcParameter.CTLCheck;
                break;
            case RISK_CFL_CHECK:
                value = qvsdcParameter.CFLCheck;
                break;
            case RISK_CVM_CHECK:
                value = qvsdcParameter.CVMCheck;
                break;
            case RISK_STATUS_CHECK:
                value = qvsdcParameter.StatusCheck;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = qvsdcParameter.ZeroAmountCheck;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = qvsdcParameter.ZeroAmountPath;
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
            case VISA_SUPPORT_MAGSTRIPE:
                qvsdcParameter.SupportMagstripe = value;
                break;
            case VISA_SUPPORT_QVSDC:
                qvsdcParameter.SupportQVSDC = value;
                break;
            case VISA_SUPPORT_CONTACT:
                qvsdcParameter.SupportContact = value;
                break;
            case VISA_OFFLINE_ONLY:
                qvsdcParameter.OfflineOnly = value;
                break;
            case VISA_SUPPORT_ONLINE_PIN:
                qvsdcParameter.SupportOnlinePIN = value;
                break;
            case VISA_SUPPORT_SIGNATURE:
                qvsdcParameter.SupportSignature = value;
                break;
            case VISA_SUPPORT_ONLINE_ODA:
                qvsdcParameter.SupportOnlineODA = value;
                break;
            case VISA_SUPPORT_ISSUER_SCRIPT_UPDATE:
                qvsdcParameter.SupportIssuerScriptUpdate = value;
                break;
            case VISA_SUPPORT_CDCVM:
                qvsdcParameter.SupportCDCVM = value;
                break;
            case VISA_SUPPORT_DRL:
                qvsdcParameter.SupportDRL = value;
                break;
            case RISK_CTL_CHECK:
                qvsdcParameter.CTLCheck = value;
                break;
            case RISK_CFL_CHECK:
                qvsdcParameter.CFLCheck = value;
                break;
            case RISK_CVM_CHECK:
                qvsdcParameter.CVMCheck = value;
                break;
            case RISK_STATUS_CHECK:
                qvsdcParameter.StatusCheck = value;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                qvsdcParameter.ZeroAmountCheck = value;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                qvsdcParameter.ZeroAmountPath = value;
                break;
            default:
                break;
        }
        result = writeQvsdcParameter(qvsdcParameter);
        return result;
    }

    private QvsdcParameter readQvsdcParameter() {
        Bundle bundle = new Bundle();
        QvsdcParameter qvsdcParameter = new QvsdcParameter();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_VISA, bundle)) {
            byte[] config = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
            byte[] qualifiers = new byte[4];
            byte[] kernelConfig = new byte[1];
            byte[] entryPoint = new byte[1];
            byte[] statusZero = new byte[1];

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(config);
            for (BerTlv tlv : tlvs.getList()) {
                if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_QUALIFIERS))) {
                    qualifiers = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_KERNEL_CONFIG))) {
                    kernelConfig = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_ENTRY_POINT))) {
                    entryPoint = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_STATUS_ZERO_AMOUNT))) {
                    statusZero = tlv.getBytesValue();
                }
            }

            if ((qualifiers[0] & 0x80) != 0) {
                qvsdcParameter.SupportMagstripe = true;
            }
            if ((qualifiers[0] & 0x20) != 0) {
                qvsdcParameter.SupportQVSDC = true;
            }
            if ((qualifiers[0] & 0x10) != 0) {
                qvsdcParameter.SupportContact = true;
            }
            if ((qualifiers[0] & 0x08) != 0) {
                qvsdcParameter.OfflineOnly = true;
            }
            if ((qualifiers[0] & 0x04) != 0) {
                qvsdcParameter.SupportOnlinePIN = true;
            }
            if ((qualifiers[0] & 0x02) != 0) {
                qvsdcParameter.SupportSignature = true;
            }
            if ((qualifiers[0] & 0x01) != 0) {
                qvsdcParameter.SupportOnlineODA = true;
            }
            if ((qualifiers[2] & 0x80) != 0) {
                qvsdcParameter.SupportIssuerScriptUpdate = true;
            }
            if ((qualifiers[2] & 0x40) != 0) {
                qvsdcParameter.SupportCDCVM = true;
            }

            if ((kernelConfig[0] & 0x01) != 0) {
                qvsdcParameter.SupportDRL = true;
            }

            if ((entryPoint[0] & 0x80) != 0) {
                qvsdcParameter.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                qvsdcParameter.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                qvsdcParameter.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                qvsdcParameter.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                qvsdcParameter.CVMCheck = true;
            }

            if ((statusZero[0] & 0x01) != 0) {
                qvsdcParameter.ZeroAmountPath = true;
            }
        }
        return qvsdcParameter;
    }

    private boolean writeQvsdcParameter(QvsdcParameter qvsdcParameter) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] qualifiers = new byte[4];
        byte[] kernelConfig = new byte[1];
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (qvsdcParameter.SupportMagstripe) {
            qualifiers[0] |= 0x80;
        }
        if (qvsdcParameter.SupportQVSDC) {
            qualifiers[0] |= 0x20;
        }
        if (qvsdcParameter.SupportContact) {
            qualifiers[0] |= 0x10;
        }
        if (qvsdcParameter.OfflineOnly) {
            qualifiers[0] |= 0x08;
        }
        if (qvsdcParameter.SupportOnlinePIN) {
            qualifiers[0] |= 0x04;
        }
        if (qvsdcParameter.SupportSignature) {
            qualifiers[0] |= 0x02;
        }
        if (qvsdcParameter.SupportOnlineODA) {
            qualifiers[0] |= 0x01;
        }
        if (qvsdcParameter.SupportIssuerScriptUpdate) {
            qualifiers[2] |= 0x80;
        }
        if (qvsdcParameter.SupportCDCVM) {
            qualifiers[2] |= 0x40;
        }

        if (qvsdcParameter.SupportDRL) {
            kernelConfig[0] |= 0x01;
        }

        if (qvsdcParameter.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (qvsdcParameter.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (qvsdcParameter.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (qvsdcParameter.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (qvsdcParameter.CVMCheck) {
            entryPoint[0] |= 0x08;
        }

        if (qvsdcParameter.ZeroAmountPath) {
            statusZero[0] |= 0x01;
        } else {
            statusZero[0] |= 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_QUALIFIERS), qualifiers));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_KERNEL_CONFIG), kernelConfig));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_VISA_SET_STATUS_ZERO_AMOUNT), statusZero));
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_VISA, bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    private static class QvsdcParameter {
        boolean SupportMagstripe;
        boolean SupportQVSDC;
        boolean SupportContact;
        boolean OfflineOnly;
        boolean SupportOnlinePIN;
        boolean SupportSignature;
        boolean SupportOnlineODA;
        boolean SupportIssuerScriptUpdate;
        boolean SupportCDCVM;
        boolean SupportDRL;

        boolean CTLCheck;
        boolean CFLCheck;
        boolean CVMCheck;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
