package com.xc.payment.adapters.emv;

import android.text.TextUtils;

import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.emv.AppleConfig;
import com.xc.payment.emv.AppleMerchant;
import com.xc.payment.utils.RegexUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppleAdapter implements IConfigAdapter {

    private AppleConfig appleConfig;
    private Map<String, List<Config>> params;
    private List<AppleMerchant> merchants;
    private int merchantIndex;
    private String[] merchantNames;

    public AppleAdapter() {
        appleConfig = new AppleConfig();
        init();
    }

    private void init() {
        merchantIndex = 0;
        params = new LinkedHashMap<>();
        merchants = appleConfig.getMerchant();

        List<Config> list = new ArrayList<>();
        list.add(Config.APPLE_CONFIG);
        params.put("Apple Config", list);

        list = new ArrayList<>();
        list.add(Config.APPLE_PROTOCOL);
        list.add(Config.APPLE_CAPABILITY);
        params.put("Apple Terminal", list);

        if (merchants.size() != 0) {
            list = new ArrayList<>();
            list.add(Config.APPLE_MERCHANT);
            params.put("Merchant Name", list);

            list = new ArrayList<>();
            list.add(Config.APPLE_MERCHANT_ID);
            list.add(Config.APPLE_MERCHANT_URL);
            list.add(Config.APPLE_FILTER);
            params.put("Merchant Value", list);
        }

        merchantNames = new String[merchants.size()];
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getMerchantId() != null) {
                merchantNames[i] = (" " + new String(merchants.get(i).getMerchantId()));
            }
        }
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case APPLE_CONFIG:
            case APPLE_PROTOCOL:
            case APPLE_CAPABILITY:
            case APPLE_MERCHANT_ID:
            case APPLE_MERCHANT_URL:
            case APPLE_FILTER:
                return getConfig(merchantIndex, config);
            case APPLE_MERCHANT:
                return merchantNames[merchantIndex];
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        switch (config) {
            case APPLE_CONFIG:
                return appleConfig.getConfigs();
            case APPLE_PROTOCOL:
                return appleConfig.getProtocol();
            case APPLE_CAPABILITY:
                return appleConfig.getCapability();
            case APPLE_MERCHANT:
                return merchantNames;
            default:
                break;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case APPLE_CONFIG:
            case APPLE_PROTOCOL:
            case APPLE_CAPABILITY:
            case APPLE_MERCHANT_ID:
            case APPLE_MERCHANT_URL:
            case APPLE_FILTER:
                result = setConfig(merchantIndex, config, data);
                break;
            case APPLE_MERCHANT:
                for (int i = 0; i < merchantNames.length; i++) {
                    if (merchantNames[i].equals(data)) {
                        merchantIndex = i;
                    }
                }
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        AppleMerchant merchant;
        switch (config) {
            case APPLE_CONFIG:
                value = appleConfig.getConfig();
                break;
            case APPLE_PROTOCOL:
                value = appleConfig.getAppleVasProtocol();
                break;
            case APPLE_CAPABILITY:
                value = appleConfig.getAppleVasCapability();
                break;
            case APPLE_MERCHANT_ID:
                merchant = merchants.get(index);
                if (merchant.getMerchantId() != null) {
                    value = new String(merchant.getMerchantId());
                }
                break;
            case APPLE_MERCHANT_URL:
                merchant = merchants.get(index);
                if (merchant.getMerchantUrl() != null) {
                    value = new String(merchant.getMerchantUrl());
                }
                break;
            case APPLE_FILTER:
                merchant = merchants.get(index);
                if (merchant.getVasFilter() != null) {
                    value = new String(merchant.getVasFilter());
                }
                break;
            case APPLE_MERCHANT:
                return merchantNames[merchantIndex];
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(int index, Config config, Object data) {
        boolean result;
        String value = (String) data;
        switch (config) {
            case APPLE_CONFIG:
                appleConfig.setConfig(value);
                init();
                return true;
            case APPLE_PROTOCOL:
                appleConfig.setAppleVasProtocol(value);
                return true;
            case APPLE_CAPABILITY:
                appleConfig.setAppleVasCapability(value);
                return true;
            case APPLE_MERCHANT_ID:
                result = !TextUtils.isEmpty(value);
                if (result) {
                    merchants.get(index).setMerchantId(value.getBytes());

                    merchantNames = new String[merchants.size()];
                    for (int i = 0; i < merchants.size(); i++) {
                        if (merchants.get(i).getMerchantId() != null) {
                            merchantNames[i] = (" " + new String(merchants.get(i).getMerchantId()));
                        }
                    }
                    return true;
                }
                break;
            case APPLE_MERCHANT_URL:
                result = TextUtils.isEmpty(value);
                if (result) {
                    merchants.get(index).setMerchantUrl(value.getBytes());
                }
                break;
            case APPLE_FILTER:
                result = RegexUtils.isCharacter(value);
                if (result && value.length() == 5) {
                    merchants.get(index).setVasFilter(value.getBytes());
                } else {
                    result = false;
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            appleConfig.setMerchant(merchants);
        }
        return result;
    }
}
