package com.xc.payment.adapters.emv;

import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.pos.sdk.emvcore.PosEmvRevocationIPK;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.RegexUtils;
import com.xc.payment.utils.tlv.HexUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RevocationIPKAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private List<PosEmvRevocationIPK> emvRevocationIPKs;
    private int revocationIPKIndex;
    private String[] revocationIPKNames;

    public RevocationIPKAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();
        emvRevocationIPKs = emvCoreManager.EmvGetRevocationIPK();

        List<Config> list = new ArrayList<>();
        list.add(Config.REVOCATION_IPK_INDEX);
        params.put("Name", list);

        if (emvRevocationIPKs != null && !emvRevocationIPKs.isEmpty()) {
            list = new ArrayList<>();
            list.add(Config.REVOCATION_IPK_RID);
            list.add(Config.REVOCATION_IPK_CAPK_ID);
            list.add(Config.REVOCATION_IPK_SERIAL_NO);

            revocationIPKIndex = 0;
            revocationIPKNames = new String[emvRevocationIPKs.size()];
            for (int i = 0; i < emvRevocationIPKs.size(); i++) {
                revocationIPKNames[i] = (HexUtil.toHexString(emvRevocationIPKs.get(i).RID) +
                        " " + HexUtil.toHexString(new byte[]{emvRevocationIPKs.get(i).CapkIndex}) +
                        " " + HexUtil.toHexString(emvRevocationIPKs.get(i).SerialNo)).toUpperCase();
            }
        } else {
            revocationIPKNames = new String[1];
            revocationIPKNames[0] = "No RevocationIPK";
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
            case REVOCATION_IPK_INDEX:
                return revocationIPKNames[revocationIPKIndex];
            case REVOCATION_IPK_RID:
            case REVOCATION_IPK_SERIAL_NO:
            case REVOCATION_IPK_CAPK_ID:
                return getConfig(revocationIPKIndex, config);
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        if (config == Config.REVOCATION_IPK_INDEX) {
            return revocationIPKNames;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case REVOCATION_IPK_INDEX:
                for (int i = 0; i < revocationIPKNames.length; i++) {
                    if (revocationIPKNames[i].equals(data)) {
                        revocationIPKIndex = i;
                    }
                }
                result = true;
                break;
            case REVOCATION_IPK_RID:
            case REVOCATION_IPK_CAPK_ID:
            case REVOCATION_IPK_SERIAL_NO:
                result = setConfig(revocationIPKIndex, config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        PosEmvRevocationIPK revocationIPK = emvRevocationIPKs.get(index);
        switch (config) {
            case REVOCATION_IPK_RID:
                value = HexUtil.toHexString(revocationIPK.RID);
                break;
            case REVOCATION_IPK_CAPK_ID:
                value = HexUtil.toHexString(new byte[]{revocationIPK.CapkIndex});
                break;
            case REVOCATION_IPK_SERIAL_NO:
                value = HexUtil.toHexString(revocationIPK.SerialNo);
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(int index, Config config, Object data) {
        boolean result;
        String value = (String) data;
        PosEmvRevocationIPK revocationIPK = emvRevocationIPKs.get(index);
        switch (config) {
            case REVOCATION_IPK_RID:
                result = checkDataFormat(value, 10);
                if (result) {
                    revocationIPK.RID = HexUtil.parseHex(value);
                }
                break;
            case REVOCATION_IPK_SERIAL_NO:
                result = checkDataFormat(value, 6);
                if (result) {
                    revocationIPK.SerialNo = HexUtil.parseHex(value);
                }
                break;
            case REVOCATION_IPK_CAPK_ID:
                result = checkDataFormat(value, 2);
                if (result) {
                    revocationIPK.CapkIndex = (byte) Integer.parseInt(value);
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = PosEmvErrorCode.EMV_OK == emvCoreManager.EmvSetRevocationIPK(revocationIPK);
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
