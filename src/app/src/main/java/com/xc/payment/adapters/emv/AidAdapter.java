package com.xc.payment.adapters.emv;

import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvAid;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.RegexUtils;
import com.xc.payment.utils.tlv.HexUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AidAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private List<PosEmvAid> emvAids;
    private int aidIndex;
    private String[] aidNames;

    public AidAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();
        emvAids = emvCoreManager.EmvGetAid();

        List<Config> list = new ArrayList<>();
        list.add(Config.AID_AID);
        params.put("Name", list);

        if (emvAids != null && !emvAids.isEmpty()) {
            list = new ArrayList<>();
            list.add(Config.AID_VERSION);
            list.add(Config.AID_TYPE_FLAG);
            list.add(Config.AID_SELECT_FLAG);
            list.add(Config.AID_ACQUIRER_ID);
            list.add(Config.AID_DDOL);
            list.add(Config.AID_TDOL);
            list.add(Config.AID_TAC_DENIAL);
            list.add(Config.AID_TAC_ONLINE);
            list.add(Config.AID_TAC_DEFAULT);
            list.add(Config.AID_THRESHOLD);
            list.add(Config.AID_TARGET_PERCENTAGE);
            list.add(Config.AID_MAX_TARGET_PERCENTAGE);
            list.add(Config.AID_FLOOR_LIMIT);
            list.add(Config.AID_CONTACTLESS_TRANS_LIMIT);
            list.add(Config.AID_CONTACTLESS_CVM_LIMIT);
            list.add(Config.AID_CONTACTLESS_FLOOR_LIMIT);
            list.add(Config.AID_DYNAMIC_TRANS_LIMIT);
            list.add(Config.AID_TERMINAL_TYPE);
            list.add(Config.AID_TERMINAL_CAPABILITY);
            list.add(Config.AID_TERMINAL_ADDITION_CAPABILITY);
            list.add(Config.AID_TERMINAL_RISK_MANAGE_DATA);
            list.add(Config.AID_TERMINAL_COUNTRY_CODE);
            list.add(Config.AID_MERCHANT_CATEGORY_CODE);
            list.add(Config.AID_TRANSACTION_CURRENCY_CODE);
            list.add(Config.AID_TRANSACTION_CURRENCY_EXP);

            aidIndex = 0;
            aidNames = new String[emvAids.size()];
            for (int i = 0; i < emvAids.size(); i++) {
                aidNames[i] = (HexUtil.toHexString(emvAids.get(i).AID).toUpperCase() + " "
                        + (emvAids.get(i).TypeIndicator ? "Ex" : ""));
            }
        } else {
            aidNames = new String[1];
            aidNames[0] = "No AID";
        }

        params.put("Value", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case AID_AID:
                return aidNames[aidIndex];
            case AID_VERSION:
            case AID_TYPE_FLAG:
            case AID_SELECT_FLAG:
            case AID_ACQUIRER_ID:
            case AID_DDOL:
            case AID_TDOL:
            case AID_TAC_DENIAL:
            case AID_TAC_ONLINE:
            case AID_TAC_DEFAULT:
            case AID_THRESHOLD:
            case AID_TARGET_PERCENTAGE:
            case AID_MAX_TARGET_PERCENTAGE:
            case AID_FLOOR_LIMIT:
            case AID_CONTACTLESS_TRANS_LIMIT:
            case AID_CONTACTLESS_CVM_LIMIT:
            case AID_CONTACTLESS_FLOOR_LIMIT:
            case AID_DYNAMIC_TRANS_LIMIT:
            case AID_TERMINAL_TYPE:
            case AID_TERMINAL_CAPABILITY:
            case AID_TERMINAL_ADDITION_CAPABILITY:
            case AID_TERMINAL_RISK_MANAGE_DATA:
            case AID_TERMINAL_COUNTRY_CODE:
            case AID_MERCHANT_CATEGORY_CODE:
            case AID_TRANSACTION_CURRENCY_CODE:
            case AID_TRANSACTION_CURRENCY_EXP:
                return getConfig(aidIndex, config);
            default:
                return null;
        }
    }

    @Override
    public Object[] getValueList(Config config) {
        if (config == Config.AID_AID) {
            return aidNames;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case AID_AID:
                for (int i = 0; i < aidNames.length; i++) {
                    if (aidNames[i].equals(data)) {
                        aidIndex = i;
                    }
                }
                result = true;
                break;
            case AID_VERSION:
            case AID_TYPE_FLAG:
            case AID_SELECT_FLAG:
            case AID_ACQUIRER_ID:
            case AID_DDOL:
            case AID_TDOL:
            case AID_TAC_DENIAL:
            case AID_TAC_ONLINE:
            case AID_TAC_DEFAULT:
            case AID_THRESHOLD:
            case AID_TARGET_PERCENTAGE:
            case AID_MAX_TARGET_PERCENTAGE:
            case AID_FLOOR_LIMIT:
            case AID_CONTACTLESS_TRANS_LIMIT:
            case AID_CONTACTLESS_CVM_LIMIT:
            case AID_CONTACTLESS_FLOOR_LIMIT:
            case AID_DYNAMIC_TRANS_LIMIT:
            case AID_TERMINAL_TYPE:
            case AID_TERMINAL_CAPABILITY:
            case AID_TERMINAL_ADDITION_CAPABILITY:
            case AID_TERMINAL_RISK_MANAGE_DATA:
            case AID_TERMINAL_COUNTRY_CODE:
            case AID_MERCHANT_CATEGORY_CODE:
            case AID_TRANSACTION_CURRENCY_CODE:
            case AID_TRANSACTION_CURRENCY_EXP:
                result = setConfig(aidIndex, config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        PosEmvAid aid = emvAids.get(index);
        switch (config) {
            case AID_AID:
                return aidNames[aidIndex];
            case AID_VERSION:
                value = HexUtil.toHexString(aid.Version);
                break;
            case AID_TYPE_FLAG:
                value = aid.TypeIndicator + "";
                break;
            case AID_SELECT_FLAG:
                value = aid.SelectIndicator + "";
                break;
            case AID_ACQUIRER_ID:
                value = HexUtil.toHexString(aid.AcquirerIdentifier);
                break;
            case AID_DDOL:
                value = HexUtil.toHexString(aid.dDOL);
                break;
            case AID_TDOL:
                value = HexUtil.toHexString(aid.tDOL);
                break;
            case AID_TAC_DENIAL:
                value = HexUtil.toHexString(aid.TACDenial);
                break;
            case AID_TAC_ONLINE:
                value = HexUtil.toHexString(aid.TACOnline);
                break;
            case AID_TAC_DEFAULT:
                value = HexUtil.toHexString(aid.TACDefault);
                break;
            case AID_THRESHOLD:
                value = aid.Threshold + "";
                break;
            case AID_TARGET_PERCENTAGE:
                value = aid.TargetPercentage + "";
                break;
            case AID_MAX_TARGET_PERCENTAGE:
                value = aid.MaxTargetPercentage + "";
                break;
            case AID_FLOOR_LIMIT:
                value = aid.FloorLimit + "";
                break;
            case AID_CONTACTLESS_TRANS_LIMIT:
                value = aid.ContactlessTransLimit + "";
                break;
            case AID_CONTACTLESS_CVM_LIMIT:
                value = aid.ContactlessCVMLimit + "";
                break;
            case AID_CONTACTLESS_FLOOR_LIMIT:
                value = aid.ContactlessFloorLimit + "";
                break;
            case AID_DYNAMIC_TRANS_LIMIT:
                value = aid.DynamicTransLimit + "";
                break;
            case AID_TERMINAL_TYPE:
                if (aid.TerminalType != null) {
                    value = HexUtil.toHexString(aid.TerminalType);
                }
                break;
            case AID_TERMINAL_CAPABILITY:
                if (aid.TerminalCapabilities != null) {
                    value = HexUtil.toHexString(aid.TerminalCapabilities);
                }
                break;
            case AID_TERMINAL_ADDITION_CAPABILITY:
                if (aid.AdditionalTerminalCapabilities != null) {
                    value = HexUtil.toHexString(aid.AdditionalTerminalCapabilities);
                }
                break;
            case AID_TERMINAL_RISK_MANAGE_DATA:
                if (aid.TerminalRiskManagementData != null) {
                    value = HexUtil.toHexString(aid.TerminalRiskManagementData);
                }
                break;
            case AID_TERMINAL_COUNTRY_CODE:
                if (aid.TerminalCountryCode != null) {
                    value = HexUtil.toHexString(aid.TerminalCountryCode);
                }
                break;
            case AID_MERCHANT_CATEGORY_CODE:
                if (aid.MerchantCategoryCode != null) {
                    value = HexUtil.toHexString(aid.MerchantCategoryCode);
                }
                break;
            case AID_TRANSACTION_CURRENCY_CODE:
                if (aid.TransCurrencyCode != null) {
                    value = HexUtil.toHexString(aid.TransCurrencyCode);
                }
                break;
            case AID_TRANSACTION_CURRENCY_EXP:
                if (aid.TransCurrencyExp != null) {
                    value = HexUtil.toHexString(aid.TransCurrencyExp);
                }
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(int index, Config config, Object data) {
        boolean result;
        String value = (data + "");
        PosEmvAid aid = emvAids.get(index);
        switch (config) {
            case AID_VERSION:
                result = checkDataFormat(value, 4, false);
                if (result) {
                    aid.Version = HexUtil.parseHex(value);
                }
                break;
            case AID_SELECT_FLAG:
                aid.SelectIndicator = !"False".equalsIgnoreCase(value);
                result = true;
                break;
            case AID_ACQUIRER_ID:
                result = checkDataFormat(value, 0, false);
                aid.AcquirerIdentifier = HexUtil.parseHex(value);
                break;
            case AID_DDOL:
                result = checkDataFormat(value, 0, false);
                aid.dDOL = HexUtil.parseHex(value);
                break;
            case AID_TDOL:
                result = checkDataFormat(value, 0, false);
                aid.tDOL = HexUtil.parseHex(value);
                break;
            case AID_TAC_DENIAL:
                result = checkDataFormat(value, 10, false);
                aid.TACDenial = HexUtil.parseHex(value);
                break;
            case AID_TAC_ONLINE:
                result = checkDataFormat(value, 10, false);
                aid.TACOnline = HexUtil.parseHex(value);
                break;
            case AID_TAC_DEFAULT:
                result = checkDataFormat(value, 10, false);
                aid.TACDefault = HexUtil.parseHex(value);
                break;
            case AID_THRESHOLD:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.Threshold = Integer.parseInt(value);
                break;
            case AID_TARGET_PERCENTAGE:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.TargetPercentage = Integer.parseInt(value);
                break;
            case AID_MAX_TARGET_PERCENTAGE:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.MaxTargetPercentage = Integer.parseInt(value);
                break;
            case AID_FLOOR_LIMIT:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.FloorLimit = Integer.parseInt(value);
                break;
            case AID_CONTACTLESS_TRANS_LIMIT:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.ContactlessTransLimit = Integer.parseInt(value);
                break;
            case AID_CONTACTLESS_CVM_LIMIT:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.ContactlessCVMLimit = Integer.parseInt(value);
                break;
            case AID_CONTACTLESS_FLOOR_LIMIT:
                result = true;
                if (TextUtils.isEmpty(value)) {
                    value = "0";
                }
                aid.ContactlessFloorLimit = Integer.parseInt(value);
                break;
            case AID_TERMINAL_TYPE:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 2, true);
                    aid.TerminalType = HexUtil.parseHex(value);
                } else {
                    aid.TerminalType = null;
                    result = true;
                }
                break;
            case AID_TERMINAL_CAPABILITY:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 6, false);
                    aid.TerminalCapabilities = HexUtil.parseHex(value);
                } else {
                    aid.TerminalCapabilities = null;
                    result = true;
                }
                break;
            case AID_TERMINAL_ADDITION_CAPABILITY:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 10, false);
                    aid.AdditionalTerminalCapabilities = HexUtil.parseHex(value);
                } else {
                    aid.AdditionalTerminalCapabilities = null;
                    result = true;
                }
                break;
            case AID_TERMINAL_RISK_MANAGE_DATA:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 16, false);
                    aid.TerminalRiskManagementData = HexUtil.parseHex(value);
                } else {
                    aid.TerminalRiskManagementData = null;
                    result = true;
                }
                break;
            case AID_TERMINAL_COUNTRY_CODE:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 4, true);
                    aid.TerminalCountryCode = HexUtil.parseHex(value);
                } else {
                    aid.TerminalCountryCode = null;
                    result = true;
                }
                break;
            case AID_MERCHANT_CATEGORY_CODE:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 4, false);
                    aid.MerchantCategoryCode = HexUtil.parseHex(value);
                } else {
                    aid.MerchantCategoryCode = null;
                    result = true;
                }
                break;
            case AID_TRANSACTION_CURRENCY_CODE:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 4, true);
                    aid.TransCurrencyCode = HexUtil.parseHex(value);
                } else {
                    aid.TransCurrencyCode = null;
                    result = true;
                }
                break;
            case AID_TRANSACTION_CURRENCY_EXP:
                if (!TextUtils.isEmpty(value)) {
                    result = checkDataFormat(value, 2, true);
                    aid.TransCurrencyExp = HexUtil.parseHex(value);
                } else {
                    aid.TransCurrencyExp = null;
                    result = true;
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = PosEmvErrorCode.EMV_OK == emvCoreManager.EmvSetAid(aid);
        }
        return result;
    }

    private boolean checkDataFormat(String string, int length, boolean isCheckNumber) {
        boolean result;

        if (TextUtils.isEmpty(string)) {
            return false;
        }

        if (isCheckNumber) {
            result = RegexUtils.isNumber(string);
        } else {
            result = RegexUtils.isCharacter(string);
        }

        if (length != 0) {
            if (result && (string.length() != length)) {
                result = false;
            }
        } else {
            if (string.length() % 2 != 0) {
                result = false;
            }
        }

        return result;
    }
}
