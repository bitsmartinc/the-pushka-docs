package com.xc.payment.adapters.emv;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvCapk;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.tlv.HexUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CapkAdapter implements IConfigAdapter {

    private Map<String, List<Config>> params;
    private List<PosEmvCapk> emvCapks;
    private int capkIndex;
    private String[] capkNames;

    public CapkAdapter() {
        POIEmvCoreManager emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();
        emvCapks = emvCoreManager.EmvGetCapk();

        List<Config> list = new ArrayList<>();
        list.add(Config.CAPK_RID);
        params.put("Name", list);

        if (emvCapks != null && !emvCapks.isEmpty()) {
            list = new ArrayList<>();
            list.add(Config.CAPK_KEY_ID);
            list.add(Config.CAPK_ALGO_IND);
            list.add(Config.CAPK_HASH_IND);
            list.add(Config.CAPK_MODULE);
            list.add(Config.CAPK_EXPONENT);
            list.add(Config.CAPK_CHECKSUM);

            capkIndex = 0;
            capkNames = new String[emvCapks.size()];
            for (int i = 0; i < emvCapks.size(); i++) {
                capkNames[i] = (HexUtil.toHexString(emvCapks.get(i).RID).toUpperCase() + "  " +
                        HexUtil.toHexString(new byte[]{emvCapks.get(i).CapkIndex}).toUpperCase());
            }
        } else {
            capkNames = new String[1];
            capkNames[0] = "No CAPK";
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
            case CAPK_RID:
                return capkNames[capkIndex];
            case CAPK_KEY_ID:
            case CAPK_ALGO_IND:
            case CAPK_HASH_IND:
            case CAPK_MODULE:
            case CAPK_EXPONENT:
            case CAPK_CHECKSUM:
                return getConfig(capkIndex, config);
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        if (config == Config.CAPK_RID) {
            return capkNames;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        if (config == Config.CAPK_RID) {
            for (int i = 0; i < capkNames.length; i++) {
                if (capkNames[i].equals(data)) {
                    capkIndex = i;
                }
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        PosEmvCapk capk = emvCapks.get(index);
        switch (config) {
            case CAPK_RID:
                return capkNames[this.capkIndex];
            case CAPK_KEY_ID:
                byte[] data = {capk.CapkIndex};
                value = HexUtil.toHexString(data);
                break;
            case CAPK_ALGO_IND:
                value = "0" + capk.AlgorithmInd;
                break;
            case CAPK_HASH_IND:
                value = "0" + capk.HashInd;
                break;
            case CAPK_MODULE:
                value = HexUtil.toHexString(capk.Module);
                break;
            case CAPK_EXPONENT:
                value = HexUtil.toHexString(capk.Exponent);
                break;
            case CAPK_CHECKSUM:
                value = HexUtil.toHexString(capk.Checksum);
                break;
            default:
                break;
        }
        return value;
    }
}
