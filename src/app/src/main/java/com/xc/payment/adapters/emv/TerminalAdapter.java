package com.xc.payment.adapters.emv;

import android.os.Bundle;
import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvTerminalConstraints;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.emv.OnlineConfig;
import com.xc.payment.emv.OnlineResult;
import com.xc.payment.emv.TransType;
import com.xc.payment.utils.GlobalData;
import com.xc.payment.utils.RegexUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TerminalAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private Bundle cacheBundle;

    public TerminalAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.TRANSACTION_TYPE);
        list.add(Config.SUPPORT_CONTACT);
        list.add(Config.SUPPORT_CONTACTLESS);
        list.add(Config.SUPPORT_MAGSTRIPE);
        list.add(Config.SUPPORT_APPLE_VAS);
        list.add(Config.SUPPORT_GOOGLE_SMART_TAP);
        list.add(Config.ONLINE_CONFIG);
        list.add(Config.ONLINE_RESULT);
        list.add(Config.KEYBOARD_FIX);
        params.put("Transaction", list);

        list = new ArrayList<>();
        list.add(Config.TERMINAL_TYPE);
        list.add(Config.TERMINAL_CAPABILITY);
        list.add(Config.TERMINAL_ADDITION_TERMINAL_CAPABILITY);
        list.add(Config.TERMINAL_ID);
        list.add(Config.TERMINAL_COUNTRY_CODE);
        list.add(Config.TERMINAL_ENTRY_MODE);
        list.add(Config.MERCHANT_ID);
        list.add(Config.MERCHANT_CATEGORY_CODE);
        list.add(Config.MERCHANT_NAME);
        list.add(Config.TRANSACTION_CURRENCY_CODE);
        list.add(Config.TRANSACTION_CURRENCY_EXP);
        list.add(Config.TRANSACTION_REFER_CURRENCY_CODE);
        list.add(Config.TRANSACTION_REFER_CURRENCY_EXP);
        list.add(Config.IFD_SERIAL_NUMBER);
        params.put("Terminal", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case TRANSACTION_TYPE:
                return getTransType();
            case SUPPORT_CONTACT:
                return isSupportContact();
            case SUPPORT_CONTACTLESS:
                return isSupportContactless();
            case SUPPORT_MAGSTRIPE:
                return isSupportMagstripe();
            case SUPPORT_APPLE_VAS:
                return isSupportAppleVas();
            case SUPPORT_GOOGLE_SMART_TAP:
                return isSupportGoogleSmartTap();
            case ONLINE_CONFIG:
                return getOnlineConfig();
            case ONLINE_RESULT:
                return getOnlineResult();
            case KEYBOARD_FIX:
                return isKeyboardFix();
            case TERMINAL_TYPE:
            case TERMINAL_CAPABILITY:
            case TERMINAL_ADDITION_TERMINAL_CAPABILITY:
            case TERMINAL_ID:
            case TERMINAL_COUNTRY_CODE:
            case TERMINAL_ENTRY_MODE:
            case MERCHANT_ID:
            case MERCHANT_CATEGORY_CODE:
            case MERCHANT_NAME:
            case TRANSACTION_CURRENCY_CODE:
            case TRANSACTION_CURRENCY_EXP:
            case TRANSACTION_REFER_CURRENCY_CODE:
            case TRANSACTION_REFER_CURRENCY_EXP:
            case IFD_SERIAL_NUMBER:
                return getConfig(config);
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        switch (config) {
            case TRANSACTION_TYPE:
                return TransType.getTransType().toArray(new String[0]);
            case ONLINE_CONFIG:
                return OnlineConfig.getOnlineConfig().toArray(new String[0]);
            case ONLINE_RESULT:
                return OnlineResult.getOnlineResult().toArray(new String[0]);
            default:
                break;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result = true;
        switch (config) {
            case TRANSACTION_TYPE:
                setTransType((String) data);
                break;
            case SUPPORT_CONTACT:
                setSupportContact((Boolean) data);
                break;
            case SUPPORT_CONTACTLESS:
                setSupportContactless((Boolean) data);
                break;
            case SUPPORT_MAGSTRIPE:
                setSupportMagstripe((Boolean) data);
                break;
            case SUPPORT_APPLE_VAS:
                setSupportAppleVas((Boolean) data);
                break;
            case SUPPORT_GOOGLE_SMART_TAP:
                setSupportGoogleSmartTap((Boolean) data);
                break;
            case ONLINE_CONFIG:
                setOnlineConfig((String) data);
                break;
            case ONLINE_RESULT:
                setOnlineResult((String) data);
                break;
            case KEYBOARD_FIX:
                setKeyboardFix((Boolean)data);
                break;
            case TERMINAL_TYPE:
            case TERMINAL_CAPABILITY:
            case TERMINAL_ADDITION_TERMINAL_CAPABILITY:
            case TERMINAL_ID:
            case TERMINAL_COUNTRY_CODE:
            case TERMINAL_ENTRY_MODE:
            case MERCHANT_ID:
            case MERCHANT_CATEGORY_CODE:
            case MERCHANT_NAME:
            case TRANSACTION_CURRENCY_CODE:
            case TRANSACTION_CURRENCY_EXP:
            case TRANSACTION_REFER_CURRENCY_CODE:
            case TRANSACTION_REFER_CURRENCY_EXP:
            case IFD_SERIAL_NUMBER:
                result = setConfig(config, data);
                break;
            default:
                break;
        }
        return result;
    }

    private String getTransType() {
        return TransType.getTransType(GlobalData.getTransType());
    }

    private void setTransType(String type) {
        GlobalData.setTransType(TransType.getTransType(type));
    }

    private boolean isSupportContact() {
        return GlobalData.isSupportContact();
    }

    private void setSupportContact(boolean is) {
        GlobalData.setSupportContact(is);
    }

    private boolean isSupportContactless() {
        return GlobalData.isSupportContactless();
    }

    private void setSupportContactless(boolean is) {
        GlobalData.setSupportContactless(is);
    }

    private boolean isSupportMagstripe() {
        return GlobalData.isSupportMagstripe();
    }

    private void setSupportMagstripe(boolean is) {
        GlobalData.setSupportMagstripe(is);
    }

    private boolean isSupportAppleVas() {
        return GlobalData.isSupportAppleVas();
    }

    private void setSupportAppleVas(boolean is) {
        GlobalData.setSupportAppleVas(is);
    }

    private boolean isSupportGoogleSmartTap() {
        return GlobalData.isSupportGoogleSmartTap();
    }

    private void setSupportGoogleSmartTap(boolean is) {
        GlobalData.setSupportGoogleSmartTap(is);
    }

    private String getOnlineConfig() {
        return OnlineConfig.getOnlineConfig(GlobalData.getTransOnlineConfig());
    }

    private void setOnlineConfig(String type) {
        GlobalData.setTransOnlineConfig(OnlineConfig.getOnlineConfig(type));
    }

    private String getOnlineResult() {
        return OnlineResult.getOnlineResult(GlobalData.getTransOnlineResult());
    }

    private void setOnlineResult(String type) {
        GlobalData.setTransOnlineResult(OnlineResult.getOnlineResult(type));
    }

    private boolean isKeyboardFix() {
        return GlobalData.isKeyboardFix();
    }

    private void setKeyboardFix(boolean is) {
        GlobalData.setKeyboardFix(is);
    }

    private String getConfig(Config config) {
        String value = "";
        if (cacheBundle == null) {
            cacheBundle = new Bundle();
            emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_TERMINAL, cacheBundle);
        }
        switch (config) {
            case TERMINAL_TYPE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_TYPE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_TYPE));
                }
                break;
            case TERMINAL_CAPABILITY:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_CAPABILITY)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_CAPABILITY));
                }
                break;
            case TERMINAL_ADDITION_TERMINAL_CAPABILITY:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_EX_CAPABILITY)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_EX_CAPABILITY));
                }
                break;
            case TERMINAL_ID:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_ID)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_ID));
                }
                break;
            case TERMINAL_COUNTRY_CODE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_COUNTRY_CODE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_COUNTRY_CODE));
                }
                break;
            case TERMINAL_ENTRY_MODE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_ENTRY_MODE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TERMINAL_ENTRY_MODE));
                }
                break;
            case MERCHANT_ID:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_ID)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_ID));
                }
                break;
            case MERCHANT_CATEGORY_CODE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_CATEGORY_CODE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_CATEGORY_CODE));
                }
                break;
            case MERCHANT_NAME:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_NAME)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.MERCHANT_NAME));
                }
                break;
            case TRANSACTION_CURRENCY_CODE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_CURRENCY_CODE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_CURRENCY_CODE));
                }
                break;
            case TRANSACTION_CURRENCY_EXP:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_CURRENCY_EXP)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_CURRENCY_EXP));
                }
                break;
            case TRANSACTION_REFER_CURRENCY_CODE:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_CODE)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_CODE));
                }
                break;
            case TRANSACTION_REFER_CURRENCY_EXP:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_EXP)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_EXP));
                }
                break;
            case IFD_SERIAL_NUMBER:
                if (null != cacheBundle.getByteArray(EmvTerminalConstraints.IFD_SERIAL_NUMBER)) {
                    value = new String(cacheBundle.getByteArray(EmvTerminalConstraints.IFD_SERIAL_NUMBER));
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
        cacheBundle = null;
        Bundle bundle = new Bundle();
        switch (config) {
            case TERMINAL_TYPE:
                result = checkDataFormat(value, 2, true);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_TYPE, value.getBytes());
                break;
            case TERMINAL_CAPABILITY:
                result = checkDataFormat(value, 6, false);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_CAPABILITY, value.getBytes());
                break;
            case TERMINAL_ADDITION_TERMINAL_CAPABILITY:
                result = checkDataFormat(value, 10, false);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_EX_CAPABILITY, value.getBytes());
                break;
            case TERMINAL_ID:
                result = checkDataFormat(value, 8, false);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_ID, value.getBytes());
                break;
            case TERMINAL_COUNTRY_CODE:
                result = checkDataFormat(value, 4, true);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_COUNTRY_CODE, value.getBytes());
                break;
            case TERMINAL_ENTRY_MODE:
                result = checkDataFormat(value, 2, false);
                bundle.putByteArray(EmvTerminalConstraints.TERMINAL_ENTRY_MODE, value.getBytes());
                break;
            case MERCHANT_ID:
                result = checkDataFormat(value, 15, false);
                bundle.putByteArray(EmvTerminalConstraints.MERCHANT_ID, value.getBytes());
                break;
            case MERCHANT_CATEGORY_CODE:
                result = checkDataFormat(value, 4, true);
                bundle.putByteArray(EmvTerminalConstraints.MERCHANT_CATEGORY_CODE, value.getBytes());
                break;
            case MERCHANT_NAME:
                result = checkDataFormat(value, 0, false);
                bundle.putByteArray(EmvTerminalConstraints.MERCHANT_NAME, value.getBytes());
                break;
            case TRANSACTION_CURRENCY_CODE:
                result = checkDataFormat(value, 4, true);
                bundle.putByteArray(EmvTerminalConstraints.TRANS_CURRENCY_CODE, value.getBytes());
                break;
            case TRANSACTION_CURRENCY_EXP:
                result = checkDataFormat(value, 2, true);
                bundle.putByteArray(EmvTerminalConstraints.TRANS_CURRENCY_EXP, value.getBytes());
                break;
            case TRANSACTION_REFER_CURRENCY_CODE:
                result = checkDataFormat(value, 4, true);
                bundle.putByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_CODE, value.getBytes());
                break;
            case TRANSACTION_REFER_CURRENCY_EXP:
                result = checkDataFormat(value, 2, true);
                bundle.putByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_EXP, value.getBytes());
                break;
            case IFD_SERIAL_NUMBER:
                result = checkDataFormat(value, 8, false);
                bundle.putByteArray(EmvTerminalConstraints.IFD_SERIAL_NUMBER, value.getBytes());
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = PosEmvErrorCode.EMV_OK == emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_TERMINAL, bundle);
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
            if (string.length() != length) {
                result = false;
            }
        }

        return result;
    }
}
