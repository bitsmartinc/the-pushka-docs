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
import com.xc.payment.utils.tlv.HexUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MasterCardAdapter extends RiskAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private Bundle bundle;

    public MasterCardAdapter() {
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.MASTERCARD_CVM_CAPABILITIES);
        list.add(Config.MASTERCARD_NO_CVM_CAPABILITIES);
        list.add(Config.MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES);
        list.add(Config.MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES);
        list.add(Config.MASTERCARD_MAGSTRIPE_APP_VERSION);
        list.add(Config.MASTERCARD_MOBILE_SUPPORT_INDICATOR);
        list.add(Config.MASTERCARD_KERNEL_ID);
        list.add(Config.MASTERCARD_DEFAULT_UDOL);
        list.add(Config.MASTERCARD_KERNEL_CONFIG);
        list.add(Config.MASTERCARD_RRP_MIN_GRACE);
        list.add(Config.MASTERCARD_RRP_MAX_GRACE);
        list.add(Config.MASTERCARD_RRP_CAPDU_EXPECTED);
        list.add(Config.MASTERCARD_RRP_RAPDU_EXPECTED);
        list.add(Config.MASTERCARD_RRP_ACCURACY_THRESHOLD);
        list.add(Config.MASTERCARD_RRP_MISMATCH_THRESHOLD);
        params.put("MasterCard", list);

        list = new ArrayList<>();
        list.add(Config.RISK_TRANS_LIMIT);
        list.add(Config.RISK_CVM_LIMIT);
        list.add(Config.RISK_CONTACTLESS_FLOOR_LIMIT);
        list.add(Config.RISK_FLOOR_LIMIT);
        list.add(Config.RISK_DYNAMIC_LIMIT);
        params.put("Risk", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case MASTERCARD_CVM_CAPABILITIES:
            case MASTERCARD_NO_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_APP_VERSION:
            case MASTERCARD_MOBILE_SUPPORT_INDICATOR:
            case MASTERCARD_KERNEL_ID:
            case MASTERCARD_DEFAULT_UDOL:
            case MASTERCARD_KERNEL_CONFIG:
            case MASTERCARD_RRP_MIN_GRACE:
            case MASTERCARD_RRP_MAX_GRACE:
            case MASTERCARD_RRP_CAPDU_EXPECTED:
            case MASTERCARD_RRP_RAPDU_EXPECTED:
            case MASTERCARD_RRP_ACCURACY_THRESHOLD:
            case MASTERCARD_RRP_MISMATCH_THRESHOLD:
                return getConfig(config);
            case RISK_TRANS_LIMIT:
            case RISK_CVM_LIMIT:
            case RISK_CONTACTLESS_FLOOR_LIMIT:
            case RISK_FLOOR_LIMIT:
            case RISK_DYNAMIC_LIMIT:
                return getLimit(CardScheme.MASTERCARD, config);
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
            case MASTERCARD_CVM_CAPABILITIES:
            case MASTERCARD_NO_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES:
            case MASTERCARD_MAGSTRIPE_APP_VERSION:
            case MASTERCARD_MOBILE_SUPPORT_INDICATOR:
            case MASTERCARD_KERNEL_ID:
            case MASTERCARD_DEFAULT_UDOL:
            case MASTERCARD_KERNEL_CONFIG:
            case MASTERCARD_RRP_MIN_GRACE:
            case MASTERCARD_RRP_MAX_GRACE:
            case MASTERCARD_RRP_CAPDU_EXPECTED:
            case MASTERCARD_RRP_RAPDU_EXPECTED:
            case MASTERCARD_RRP_ACCURACY_THRESHOLD:
            case MASTERCARD_RRP_MISMATCH_THRESHOLD:
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
                    result = setLimit(CardScheme.MASTERCARD, config, Integer.parseInt((String) data));
                }
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(Config config) {
        String value = "";
        if (bundle == null) {
            bundle = new Bundle();
            emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_MASTERCARD, bundle);
        }
        byte[] data = bundle.getByteArray(EmvTerminalConstraints.CONFIG);
        BerTlvParser tlvParser = new BerTlvParser();
        BerTlvs tlvs = tlvParser.parse(data);
        BerTlv tlv;
        switch (config) {
            case MASTERCARD_CVM_CAPABILITIES:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_CVM_CAPABILITIES));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_NO_CVM_CAPABILITIES:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_NO_CVM_CAPABILITIES));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_CVM_CAPABILITIES));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_NO_CVM_CAPABILITIES));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_MAGSTRIPE_APP_VERSION:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_APP_VERSION));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_MOBILE_SUPPORT_INDICATOR:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MOBILE_SUPPORT_INDICATOR));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_KERNEL_ID:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_KERNEL_ID));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_DEFAULT_UDOL:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_DEFAULT_UDOL));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_KERNEL_CONFIG:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_KERNEL_CONFIG));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_MIN_GRACE:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MIN_GRACE));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_MAX_GRACE:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MAX_GRACE));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_CAPDU_EXPECTED:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_CAPDU_EXPECTED));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_RAPDU_EXPECTED:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_RAPDU_EXPECTED));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_ACCURACY_THRESHOLD:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_ACCURACY_THRESHOLD));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            case MASTERCARD_RRP_MISMATCH_THRESHOLD:
                tlv = tlvs.find(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MISMATCH_THRESHOLD));
                if (tlv != null) {
                    value = tlv.getHexValue();
                }
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(Config config, Object data) {
        boolean result;
        String value = (String) data;
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        bundle = null;
        switch (config) {
            case MASTERCARD_CVM_CAPABILITIES:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_CVM_CAPABILITIES),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_NO_CVM_CAPABILITIES:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_NO_CVM_CAPABILITIES),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_CVM_CAPABILITIES),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_NO_CVM_CAPABILITIES),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_MAGSTRIPE_APP_VERSION:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_APP_VERSION),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_MOBILE_SUPPORT_INDICATOR:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MOBILE_SUPPORT_INDICATOR),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_KERNEL_ID:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_KERNEL_ID),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_DEFAULT_UDOL:
                result = checkDataFormat(value, 0);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_DEFAULT_UDOL),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_KERNEL_CONFIG:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_KERNEL_CONFIG),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_MIN_GRACE:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MIN_GRACE),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_MAX_GRACE:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MAX_GRACE),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_CAPDU_EXPECTED:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_CAPDU_EXPECTED),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_RAPDU_EXPECTED:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_RAPDU_EXPECTED),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_ACCURACY_THRESHOLD:
                result = checkDataFormat(value, 4);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_ACCURACY_THRESHOLD),
                            HexUtil.parseHex(value)));
                }
                break;
            case MASTERCARD_RRP_MISMATCH_THRESHOLD:
                result = checkDataFormat(value, 2);
                if (result) {
                    tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_RRP_MISMATCH_THRESHOLD),
                            HexUtil.parseHex(value)));
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray());
            result = PosEmvErrorCode.EMV_OK == emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_MASTERCARD, bundle);
        }
        return result;
    }

    private boolean checkDataFormat(String string, int length) {
        boolean result;

        if (TextUtils.isEmpty(string)) {
            return false;
        }

        result = RegexUtils.isCharacter(string);

        if (length != 0) {
            if (string.length() != length) {
                result = false;
            }
        }

        return result;
    }
}
