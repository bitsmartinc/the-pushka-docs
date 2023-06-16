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

public class DiscoverAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private DiscoverParameter discoverParameter;

    public DiscoverAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.DISCOVER_SUPPORT_MAGSTRIPE);
        list.add(Config.DISCOVER_SUPPORT_EMV);
        list.add(Config.DISCOVER_SUPPORT_CONTACT);
        list.add(Config.DISCOVER_OFFLINE_ONLY);
        list.add(Config.DISCOVER_SUPPORT_ONLINE_PIN);
        list.add(Config.DISCOVER_SUPPORT_SIGNATURE);
        list.add(Config.DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE);
        list.add(Config.DISCOVER_SUPPORT_CDCVM);
        params.put("Discover", list);

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
            case DISCOVER_SUPPORT_MAGSTRIPE:
            case DISCOVER_SUPPORT_EMV:
            case DISCOVER_SUPPORT_CONTACT:
            case DISCOVER_OFFLINE_ONLY:
            case DISCOVER_SUPPORT_ONLINE_PIN:
            case DISCOVER_SUPPORT_SIGNATURE:
            case DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE:
            case DISCOVER_SUPPORT_CDCVM:
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
                return getLimit(CardScheme.DISCOVER, config);
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
            case DISCOVER_SUPPORT_MAGSTRIPE:
            case DISCOVER_SUPPORT_EMV:
            case DISCOVER_SUPPORT_CONTACT:
            case DISCOVER_OFFLINE_ONLY:
            case DISCOVER_SUPPORT_ONLINE_PIN:
            case DISCOVER_SUPPORT_SIGNATURE:
            case DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE:
            case DISCOVER_SUPPORT_CDCVM:
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
                    result = setLimit(CardScheme.DISCOVER, config, Integer.parseInt((String) data));
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
        if (discoverParameter == null) {
            discoverParameter = readDiscoverParameter();
        }
        switch (config) {
            case DISCOVER_SUPPORT_MAGSTRIPE:
                value = discoverParameter.SupportMagstripe;
                break;
            case DISCOVER_SUPPORT_EMV:
                value = discoverParameter.SupportEMV;
                break;
            case DISCOVER_SUPPORT_CONTACT:
                value = discoverParameter.SupportContact;
                break;
            case DISCOVER_OFFLINE_ONLY:
                value = discoverParameter.OfflineOnly;
                break;
            case DISCOVER_SUPPORT_ONLINE_PIN:
                value = discoverParameter.SupportOnlinePIN;
                break;
            case DISCOVER_SUPPORT_SIGNATURE:
                value = discoverParameter.SupportSignature;
                break;
            case DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE:
                value = discoverParameter.SupportIssuerScriptUpdate;
                break;
            case DISCOVER_SUPPORT_CDCVM:
                value = discoverParameter.SupportCDCVM;
                break;
            case RISK_CTL_CHECK:
                value = discoverParameter.CTLCheck;
                break;
            case RISK_CFL_CHECK:
                value = discoverParameter.CFLCheck;
                break;
            case RISK_CVM_CHECK:
                value = discoverParameter.CVMCheck;
                break;
            case RISK_STATUS_CHECK:
                value = discoverParameter.StatusCheck;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                value = discoverParameter.ZeroAmountCheck;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                value = discoverParameter.ZeroAmountPath;
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
            case DISCOVER_SUPPORT_MAGSTRIPE:
                discoverParameter.SupportMagstripe = value;
                break;
            case DISCOVER_SUPPORT_EMV:
                discoverParameter.SupportEMV = value;
                break;
            case DISCOVER_SUPPORT_CONTACT:
                discoverParameter.SupportContact = value;
                break;
            case DISCOVER_OFFLINE_ONLY:
                discoverParameter.OfflineOnly = value;
                break;
            case DISCOVER_SUPPORT_ONLINE_PIN:
                discoverParameter.SupportOnlinePIN = value;
                break;
            case DISCOVER_SUPPORT_SIGNATURE:
                discoverParameter.SupportSignature = value;
                break;
            case DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE:
                discoverParameter.SupportIssuerScriptUpdate = value;
                break;
            case DISCOVER_SUPPORT_CDCVM:
                discoverParameter.SupportCDCVM = value;
                break;
            case RISK_CTL_CHECK:
                discoverParameter.CTLCheck = value;
                break;
            case RISK_CFL_CHECK:
                discoverParameter.CFLCheck = value;
                break;
            case RISK_CVM_CHECK:
                discoverParameter.CVMCheck = value;
                break;
            case RISK_STATUS_CHECK:
                discoverParameter.StatusCheck = value;
                break;
            case RISK_ZERO_AMOUNT_CHECK:
                discoverParameter.ZeroAmountCheck = value;
                break;
            case RISK_ZERO_AMOUNT_PATH:
                discoverParameter.ZeroAmountPath = value;
                break;
            default:
                break;
        }
        result = writeDiscoverParameter(discoverParameter);
        return result;
    }

    private DiscoverParameter readDiscoverParameter() {
        Bundle bundle = new Bundle();
        DiscoverParameter discoverParameter = new DiscoverParameter();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_DISCOVER, bundle)) {
            byte[] configParam = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
            byte[] qualifiers = new byte[4];
            byte[] entryPoint = new byte[1];
            byte[] statusZero = new byte[1];

            BerTlvParser tlvParser = new BerTlvParser();
            BerTlvs tlvs = tlvParser.parse(configParam);
            for (BerTlv tlv : tlvs.getList()) {
                if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_QUALIFIERS))) {
                    qualifiers = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_ENTRY_POINT))) {
                    entryPoint = tlv.getBytesValue();
                } else if (tlv.getTag().equals(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_STATUS_ZERO_AMOUNT))) {
                    statusZero = tlv.getBytesValue();
                }
            }

            if ((qualifiers[0] & 0x80) != 0) {
                discoverParameter.SupportMagstripe = true;
            }
            if ((qualifiers[0] & 0x20) != 0) {
                discoverParameter.SupportEMV = true;
            }
            if ((qualifiers[0] & 0x10) != 0) {
                discoverParameter.SupportContact = true;
            }
            if ((qualifiers[0] & 0x08) != 0) {
                discoverParameter.OfflineOnly = true;
            }
            if ((qualifiers[0] & 0x04) != 0) {
                discoverParameter.SupportOnlinePIN = true;
            }
            if ((qualifiers[0] & 0x02) != 0) {
                discoverParameter.SupportSignature = true;
            }
            if ((qualifiers[2] & 0x80) != 0) {
                discoverParameter.SupportIssuerScriptUpdate = true;
            }
            if ((qualifiers[2] & 0x40) != 0) {
                discoverParameter.SupportCDCVM = true;
            }

            if ((entryPoint[0] & 0x80) != 0) {
                discoverParameter.StatusCheck = true;
            }
            if ((entryPoint[0] & 0x40) != 0) {
                discoverParameter.ZeroAmountCheck = true;
            }
            if ((entryPoint[0] & 0x20) != 0) {
                discoverParameter.CTLCheck = true;
            }
            if ((entryPoint[0] & 0x10) != 0) {
                discoverParameter.CFLCheck = true;
            }
            if ((entryPoint[0] & 0x08) != 0) {
                discoverParameter.CVMCheck = true;
            }

            if ((statusZero[0] & 0x01) != 0) {
                discoverParameter.ZeroAmountPath = true;
            }
        }
        return discoverParameter;
    }

    private boolean writeDiscoverParameter(DiscoverParameter discoverParameter) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        byte[] qualifiers = new byte[4];
        byte[] entryPoint = new byte[1];
        byte[] statusZero = new byte[1];

        if (discoverParameter.SupportMagstripe) {
            qualifiers[0] |= 0x80;
        }
        if (discoverParameter.SupportEMV) {
            qualifiers[0] |= 0x20;
        }
        if (discoverParameter.SupportContact) {
            qualifiers[0] |= 0x10;
        }
        if (discoverParameter.OfflineOnly) {
            qualifiers[0] |= 0x08;
        }
        if (discoverParameter.SupportOnlinePIN) {
            qualifiers[0] |= 0x04;
        }
        if (discoverParameter.SupportSignature) {
            qualifiers[0] |= 0x02;
        }
        if (discoverParameter.SupportIssuerScriptUpdate) {
            qualifiers[2] |= 0x80;
        }
        if (discoverParameter.SupportCDCVM) {
            qualifiers[2] |= 0x40;
        }

        if (discoverParameter.StatusCheck) {
            entryPoint[0] |= 0x80;
        }
        if (discoverParameter.ZeroAmountCheck) {
            entryPoint[0] |= 0x40;
        }
        if (discoverParameter.CTLCheck) {
            entryPoint[0] |= 0x20;
        }
        if (discoverParameter.CFLCheck) {
            entryPoint[0] |= 0x10;
        }
        if (discoverParameter.CVMCheck) {
            entryPoint[0] |= 0x08;
        }

        if (discoverParameter.ZeroAmountPath) {
            statusZero[0] |= 0x01;
        } else {
            statusZero[0] |= 0x02;
        }

        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_QUALIFIERS), qualifiers));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_ENTRY_POINT), entryPoint));
        tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_STATUS_ZERO_AMOUNT), statusZero));
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_DISCOVER, bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    private static class DiscoverParameter {
        boolean SupportMagstripe;
        boolean SupportEMV;
        boolean SupportContact;
        boolean OfflineOnly;
        boolean SupportOnlinePIN;
        boolean SupportSignature;
        boolean SupportIssuerScriptUpdate;
        boolean SupportCDCVM;

        boolean CTLCheck;
        boolean CVMCheck;
        boolean CFLCheck;
        boolean StatusCheck;
        boolean ZeroAmountCheck;
        boolean ZeroAmountPath;
    }
}
