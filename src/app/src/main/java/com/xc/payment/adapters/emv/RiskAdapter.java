package com.xc.payment.adapters.emv;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvAid;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.utils.tlv.HexUtil;

import java.util.List;

class RiskAdapter {

    POIEmvCoreManager emvCoreManager;
    List<PosEmvAid> emvAids;

    RiskAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
    }

    String getLimit(CardScheme cardScheme, Config config) {
        String data = "0";
        int[] transLimit = new int[10];
        int[] transLimitEx = new int[10];
        int[] cvmLimit = new int[10];
        int[] floorLimit = new int[10];
        int[] floorLimitEx = new int[10];

        if (emvAids == null) {
            emvAids = emvCoreManager.EmvGetAid();
        }

        for (PosEmvAid emvAid : emvAids) {
            CardScheme emvCardScheme = CardScheme.getCardTypeByAid(HexUtil.toHexString(emvAid.AID));
            if (emvCardScheme == null) {
                continue;
            }
            if (emvAid.ContactlessTransLimit > transLimit[emvCardScheme.ordinal()]) {
                transLimit[emvCardScheme.ordinal()] = emvAid.ContactlessTransLimit;
            }
            if (emvAid.ContactlessCVMLimit > cvmLimit[emvCardScheme.ordinal()]) {
                cvmLimit[emvCardScheme.ordinal()] = emvAid.ContactlessCVMLimit;
            }
            if (emvAid.ContactlessFloorLimit > floorLimit[emvCardScheme.ordinal()]) {
                floorLimit[emvCardScheme.ordinal()] = emvAid.ContactlessFloorLimit;
            }
            if (emvAid.FloorLimit > floorLimitEx[emvCardScheme.ordinal()]) {
                floorLimitEx[emvCardScheme.ordinal()] = emvAid.FloorLimit;
            }
            if (emvAid.DynamicTransLimit > transLimitEx[emvCardScheme.ordinal()]) {
                transLimitEx[emvCardScheme.ordinal()] = emvAid.DynamicTransLimit;
            }
        }

        switch (config) {
            case RISK_TRANS_LIMIT:
                return Integer.toString(transLimit[cardScheme.ordinal()]);
            case RISK_CVM_LIMIT:
                return Integer.toString(cvmLimit[cardScheme.ordinal()]);
            case RISK_CONTACTLESS_FLOOR_LIMIT:
                return Integer.toString(floorLimit[cardScheme.ordinal()]);
            case RISK_FLOOR_LIMIT:
                return Integer.toString(floorLimitEx[cardScheme.ordinal()]);
            case RISK_DYNAMIC_LIMIT:
                return Integer.toString(transLimitEx[cardScheme.ordinal()]);
            default:
                break;
        }

        return data;
    }

    boolean setLimit(CardScheme cardScheme, Config config, int limit) {
        int limitType;

        switch (config) {
            case RISK_TRANS_LIMIT:
                limitType = 1;
                break;
            case RISK_CVM_LIMIT:
                limitType = 2;
                break;
            case RISK_CONTACTLESS_FLOOR_LIMIT:
                limitType = 3;
                break;
            case RISK_FLOOR_LIMIT:
                limitType = 4;
                break;
            case RISK_DYNAMIC_LIMIT:
                limitType = 5;
                break;
            default:
                limitType = 0;
                break;
        }

        if (emvAids == null) {
            emvAids = emvCoreManager.EmvGetAid();
        }

        for (PosEmvAid emvAid : emvAids) {
            CardScheme emvCardScheme = CardScheme.getCardTypeByAid(HexUtil.toHexString(emvAid.AID));
            if (cardScheme == emvCardScheme) {
                switch (limitType) {
                    case 1:
                        emvAid.ContactlessTransLimit = limit;
                        break;
                    case 2:
                        emvAid.ContactlessCVMLimit = limit;
                        break;
                    case 3:
                        emvAid.ContactlessFloorLimit = limit;
                        break;
                    case 4:
                        emvAid.FloorLimit = limit;
                        break;
                    case 5:
                        emvAid.DynamicTransLimit = limit;
                        break;
                    default:
                        break;
                }
                emvCoreManager.EmvSetAid(emvAid);
            }
        }

        return true;
    }
}
