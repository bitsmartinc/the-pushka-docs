package com.xc.payment.adapters.emv;

import android.os.Bundle;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvTerminalConstraints;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private Bundle cacheBundle;

    public ConfigAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();

        List<Config> list = new ArrayList<>();
        list.add(Config.CONFIG_PSE);
        list.add(Config.CONFIG_CARDHOLDER_CONFIRM);
        list.add(Config.CONFIG_LANGUAGE_SELECT);
        list.add(Config.CONFIG_DEFAULT_DDOL);
        list.add(Config.CONFIG_DEFAULT_TDOL);
        list.add(Config.CONFIG_BYPASS_PIN_ENTRY);
        list.add(Config.CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY);
        list.add(Config.CONFIG_GET_DATA_FOR_PIN_COUNTER);
        list.add(Config.CONFIG_FLOOR_LIMIT_CHECKING);
        list.add(Config.CONFIG_RANDOM_TRANSACTION_SELECTION);
        list.add(Config.CONFIG_VELOCITY_CHECKING);
        list.add(Config.CONFIG_EXCEPTION_FILE);
        list.add(Config.CONFIG_REVOCATION_ISSUER_PUBLIC_KEY);
        list.add(Config.CONFIG_ISSUER_REFERRAL);
        list.add(Config.CONFIG_UNABLE_TO_GO_ONLINE);
        list.add(Config.CONFIG_FORCED_ONLINE);
        list.add(Config.CONFIG_FORCED_ACCEPT);
        params.put("Config", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case CONFIG_PSE:
            case CONFIG_CARDHOLDER_CONFIRM:
            case CONFIG_LANGUAGE_SELECT:
            case CONFIG_DEFAULT_DDOL:
            case CONFIG_DEFAULT_TDOL:
            case CONFIG_BYPASS_PIN_ENTRY:
            case CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY:
            case CONFIG_GET_DATA_FOR_PIN_COUNTER:
            case CONFIG_FLOOR_LIMIT_CHECKING:
            case CONFIG_RANDOM_TRANSACTION_SELECTION:
            case CONFIG_VELOCITY_CHECKING:
            case CONFIG_EXCEPTION_FILE:
            case CONFIG_REVOCATION_ISSUER_PUBLIC_KEY:
            case CONFIG_ISSUER_REFERRAL:
            case CONFIG_UNABLE_TO_GO_ONLINE:
            case CONFIG_FORCED_ONLINE:
            case CONFIG_FORCED_ACCEPT:
                return getConfig(config);
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case CONFIG_PSE:
            case CONFIG_CARDHOLDER_CONFIRM:
            case CONFIG_LANGUAGE_SELECT:
            case CONFIG_DEFAULT_DDOL:
            case CONFIG_DEFAULT_TDOL:
            case CONFIG_BYPASS_PIN_ENTRY:
            case CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY:
            case CONFIG_GET_DATA_FOR_PIN_COUNTER:
            case CONFIG_FLOOR_LIMIT_CHECKING:
            case CONFIG_RANDOM_TRANSACTION_SELECTION:
            case CONFIG_VELOCITY_CHECKING:
            case CONFIG_EXCEPTION_FILE:
            case CONFIG_REVOCATION_ISSUER_PUBLIC_KEY:
            case CONFIG_ISSUER_REFERRAL:
            case CONFIG_UNABLE_TO_GO_ONLINE:
            case CONFIG_FORCED_ONLINE:
            case CONFIG_FORCED_ACCEPT:
                result = setConfig(config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private boolean getConfig(Config config) {
        boolean value = false;
        if (cacheBundle == null) {
            cacheBundle = new Bundle();
            emvCoreManager.EmvGetTerminal(EmvTerminalConstraints.TYPE_CONFIG, cacheBundle);
        }
        switch (config) {
            case CONFIG_PSE:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.PSE, false);
                break;
            case CONFIG_CARDHOLDER_CONFIRM:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.CARD_HOLDER_CONFIRM, false);
                break;
            case CONFIG_LANGUAGE_SELECT:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.LANGUAGE_SELECT, false);
                break;
            case CONFIG_DEFAULT_DDOL:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.DEFAULT_DDOL, false);
                break;
            case CONFIG_DEFAULT_TDOL:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.DEFAULT_TDOL, false);
                break;
            case CONFIG_BYPASS_PIN_ENTRY:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.BYPASS_PIN_ENTRY, false);
                break;
            case CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.SUBSEQUENT_BYPASS_PIN_ENTRY, false);
                break;
            case CONFIG_GET_DATA_FOR_PIN_COUNTER:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.GET_DATA_FOR_PIN_COUNTER, false);
                break;
            case CONFIG_FLOOR_LIMIT_CHECKING:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.FLOOR_LIMIT_CHECKING, false);
                break;
            case CONFIG_RANDOM_TRANSACTION_SELECTION:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.RANDOM_TRANSACTION_SELECTION, false);
                break;
            case CONFIG_VELOCITY_CHECKING:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.VELOCITY_CHECKING, false);
                break;
            case CONFIG_EXCEPTION_FILE:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.EXCEPTION_FILE, false);
                break;
            case CONFIG_REVOCATION_ISSUER_PUBLIC_KEY:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.REVOCATION_ISSUER_PUBLIC_KEY, false);
                break;
            case CONFIG_ISSUER_REFERRAL:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.ISSUER_REFERRAL, false);
                break;
            case CONFIG_UNABLE_TO_GO_ONLINE:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.UNABLE_TO_GO_ONLINE, false);
                break;
            case CONFIG_FORCED_ONLINE:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.FORCED_ONLINE, false);
                break;
            case CONFIG_FORCED_ACCEPT:
                value = cacheBundle.getBoolean(EmvTerminalConstraints.FORCED_ACCEPT, false);
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(Config config, Object data) {
        boolean value = (boolean) data;
        cacheBundle = null;
        Bundle bundle = new Bundle();
        switch (config) {
            case CONFIG_PSE:
                bundle.putBoolean(EmvTerminalConstraints.PSE, value);
                break;
            case CONFIG_CARDHOLDER_CONFIRM:
                bundle.putBoolean(EmvTerminalConstraints.CARD_HOLDER_CONFIRM, value);
                break;
            case CONFIG_LANGUAGE_SELECT:
                bundle.putBoolean(EmvTerminalConstraints.LANGUAGE_SELECT, value);
                break;
            case CONFIG_DEFAULT_DDOL:
                bundle.putBoolean(EmvTerminalConstraints.DEFAULT_DDOL, value);
                break;
            case CONFIG_DEFAULT_TDOL:
                bundle.putBoolean(EmvTerminalConstraints.DEFAULT_TDOL, value);
                break;
            case CONFIG_BYPASS_PIN_ENTRY:
                bundle.putBoolean(EmvTerminalConstraints.BYPASS_PIN_ENTRY, value);
                break;
            case CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY:
                bundle.putBoolean(EmvTerminalConstraints.SUBSEQUENT_BYPASS_PIN_ENTRY, value);
                break;
            case CONFIG_GET_DATA_FOR_PIN_COUNTER:
                bundle.putBoolean(EmvTerminalConstraints.GET_DATA_FOR_PIN_COUNTER, value);
                break;
            case CONFIG_FLOOR_LIMIT_CHECKING:
                bundle.putBoolean(EmvTerminalConstraints.FLOOR_LIMIT_CHECKING, value);
                break;
            case CONFIG_RANDOM_TRANSACTION_SELECTION:
                bundle.putBoolean(EmvTerminalConstraints.RANDOM_TRANSACTION_SELECTION, value);
                break;
            case CONFIG_VELOCITY_CHECKING:
                bundle.putBoolean(EmvTerminalConstraints.VELOCITY_CHECKING, value);
                break;
            case CONFIG_EXCEPTION_FILE:
                bundle.putBoolean(EmvTerminalConstraints.EXCEPTION_FILE, value);
                break;
            case CONFIG_REVOCATION_ISSUER_PUBLIC_KEY:
                bundle.putBoolean(EmvTerminalConstraints.REVOCATION_ISSUER_PUBLIC_KEY, value);
                break;
            case CONFIG_ISSUER_REFERRAL:
                bundle.putBoolean(EmvTerminalConstraints.ISSUER_REFERRAL, value);
                break;
            case CONFIG_UNABLE_TO_GO_ONLINE:
                bundle.putBoolean(EmvTerminalConstraints.UNABLE_TO_GO_ONLINE, value);
                break;
            case CONFIG_FORCED_ONLINE:
                bundle.putBoolean(EmvTerminalConstraints.FORCED_ONLINE, value);
                break;
            case CONFIG_FORCED_ACCEPT:
                bundle.putBoolean(EmvTerminalConstraints.FORCED_ACCEPT, value);
                break;
            default:
                break;
        }
        emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_CONFIG, bundle);
        return true;
    }
}
