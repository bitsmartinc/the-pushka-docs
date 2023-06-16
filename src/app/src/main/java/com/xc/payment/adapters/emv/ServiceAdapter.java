package com.xc.payment.adapters.emv;

import android.os.Bundle;
import android.text.TextUtils;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.POIEmvCoreManager.EmvServiceConstraints;
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

public class ServiceAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private Service[] services;
    private int serviceIndex;
    private String[] serviceNames;
    private int prmacqIndex;
    private String[] prmacqNames;

    public ServiceAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();

        services = new Service[10];
        for (int i = 0; i < services.length; i++) {
            services[i] = new Service();
            services[i].PRMacq = new PRMacq[10];
            for (int j = 0; j < 10; j++) {
                services[i].PRMacq[j] = new PRMacq();
            }
        }

        readService();

        List<Config> list = new ArrayList<>();
        list.add(Config.SERVICE_INDEX);
        params.put("Service Index", list);

        if (services != null && services.length != 0) {
            serviceIndex = 0;
            serviceNames = new String[services.length];
            for (int i = 0; i < services.length; i++) {
                serviceNames[i] = ("" + i);
            }

            prmacqIndex = 0;
            prmacqNames = new String[services[serviceIndex].PRMacq.length];
            for (int i = 0; i < services[serviceIndex].PRMacq.length; i++) {
                prmacqNames[i] = ("" + i);
            }
        }

        list = new ArrayList<>();
        list.add(Config.SERVICE_ACTIVE);
        list.add(Config.SERVICE_ID);
        list.add(Config.SERVICE_PRIORITY);
        list.add(Config.SERVICE_MANAGE_INFO);
        list.add(Config.SERVICE_DATA);
        list.add(Config.SERVICE_PRMISS);
        params.put("Service", list);

        list = new ArrayList<>();
        list.add(Config.SERVICE_PRMACQ);
        params.put("PRMacq Index", list);

        list = new ArrayList<>();
        list.add(Config.SERVICE_PRMACQ_INDEX);
        list.add(Config.SERVICE_PRMACQ_KEY);
        list.add(Config.SERVICE_PRMACQ_KCV);
        params.put("PRMacq", list);
    }

    @Override
    public Map<String, List<Config>> getDataList() {
        return params;
    }

    @Override
    public Object getValue(Config config) {
        switch (config) {
            case SERVICE_INDEX:
                return serviceNames[serviceIndex];
            case SERVICE_PRMACQ:
                return prmacqNames[prmacqIndex];
            case SERVICE_ACTIVE:
            case SERVICE_ID:
            case SERVICE_PRIORITY:
            case SERVICE_MANAGE_INFO:
            case SERVICE_DATA:
            case SERVICE_PRMISS:
            case SERVICE_PRMACQ_INDEX:
            case SERVICE_PRMACQ_KEY:
            case SERVICE_PRMACQ_KCV:
                return getConfig(config);
            default:
                break;
        }
        return "";
    }

    @Override
    public Object[] getValueList(Config config) {
        switch (config) {
            case SERVICE_INDEX:
                return serviceNames;
            case SERVICE_PRMACQ:
                return prmacqNames;
            default:
                return null;
        }
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case SERVICE_INDEX:
                for (int i = 0; i < serviceNames.length; i++) {
                    if (serviceNames[i].equals(data)) {
                        serviceIndex = i;
                        prmacqIndex = 0;
                    }
                }
                result = true;
                break;
            case SERVICE_PRMACQ:
                for (int i = 0; i < prmacqNames.length; i++) {
                    if (prmacqNames[i].equals(data)) {
                        prmacqIndex = i;
                    }
                }
                result = true;
                break;
            case SERVICE_ACTIVE:
            case SERVICE_ID:
            case SERVICE_PRIORITY:
            case SERVICE_MANAGE_INFO:
            case SERVICE_DATA:
            case SERVICE_PRMISS:
            case SERVICE_PRMACQ_INDEX:
            case SERVICE_PRMACQ_KEY:
            case SERVICE_PRMACQ_KCV:
                result = setConfig(config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(Config config) {
        String value = "";
        switch (config) {
            case SERVICE_ACTIVE:
                value = String.valueOf(services[serviceIndex].Active);
                break;
            case SERVICE_ID:
                if (services[serviceIndex].ServiceID != null) {
                    value = HexUtil.toHexString(services[serviceIndex].ServiceID);
                }
                break;
            case SERVICE_PRIORITY:
                value = String.valueOf(services[serviceIndex].Priority);
                break;
            case SERVICE_MANAGE_INFO:
                if (services[serviceIndex].ServiceManage != null) {
                    value = HexUtil.toHexString(services[serviceIndex].ServiceManage);
                }
                break;
            case SERVICE_DATA:
                if (services[serviceIndex].ServiceData != null) {
                    value = HexUtil.toHexString(services[serviceIndex].ServiceData);
                }
                break;
            case SERVICE_PRMISS:
                if (services[serviceIndex].PRMiss != null) {
                    value = HexUtil.toHexString(services[serviceIndex].PRMiss);
                }
                break;
            case SERVICE_PRMACQ_INDEX:
                value = String.valueOf(services[serviceIndex].PRMacq[prmacqIndex].Index);
                break;
            case SERVICE_PRMACQ_KEY:
                if (services[serviceIndex].PRMacq[prmacqIndex].Key != null) {
                    value = HexUtil.toHexString(services[serviceIndex].PRMacq[prmacqIndex].Key);
                }
                break;
            case SERVICE_PRMACQ_KCV:
                if (services[serviceIndex].PRMacq[prmacqIndex].Kcv != null) {
                    value = HexUtil.toHexString(services[serviceIndex].PRMacq[prmacqIndex].Kcv);
                }
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(Config config, Object data) {
        boolean result;
        String value = String.valueOf(data);
        switch (config) {
            case SERVICE_ACTIVE:
                services[serviceIndex].Active = !"False".equalsIgnoreCase(value);
                result = true;
                break;
            case SERVICE_ID:
                result = checkDataFormat(value, 4);
                if (result) {
                    services[serviceIndex].ServiceID = HexUtil.parseHex(value);
                }
                break;
            case SERVICE_PRIORITY:
                result = checkDataFormat(value, 2);
                if (result) {
                    services[serviceIndex].Priority = HexUtil.parseHex(value)[0];
                }
                break;
            case SERVICE_MANAGE_INFO:
                result = checkDataFormat(value, 2);
                if (result) {
                    services[serviceIndex].ServiceManage = HexUtil.parseHex(value);
                }
                break;
            case SERVICE_DATA:
                if (!TextUtils.isEmpty(value)) {
                    services[serviceIndex].ServiceData = HexUtil.parseHex(value);
                } else {
                    services[serviceIndex].ServiceData = null;
                }
                result = true;
                break;
            case SERVICE_PRMISS:
                result = checkDataFormat(value, 32);
                if (result) {
                    services[serviceIndex].PRMiss = HexUtil.parseHex(value);
                }
                break;
            case SERVICE_PRMACQ_INDEX:
                if (!TextUtils.isEmpty(value) && value.length() == 1) {
                    services[serviceIndex].PRMacq[prmacqIndex].Index = Byte.parseByte(value);
                    result = true;
                } else {
                    result = false;
                }
                break;
            case SERVICE_PRMACQ_KEY:
                if (!TextUtils.isEmpty(value) && (value.length() == 16 || value.length() == 32)) {
                    services[serviceIndex].PRMacq[prmacqIndex].Key = HexUtil.parseHex(value);
                    result = true;
                } else {
                    result = false;
                }
                break;
            case SERVICE_PRMACQ_KCV:
                if (!TextUtils.isEmpty(value) && value.length() == 6) {
                    services[serviceIndex].PRMacq[prmacqIndex].Kcv = HexUtil.parseHex(value);
                    result = true;
                } else {
                    result = false;
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = writeService();
        }
        return result;
    }

    private void readService() {
        byte[] data;
        Bundle bundle = new Bundle();
        if (PosEmvErrorCode.EMV_OK == emvCoreManager.EmvGetService(bundle)) {
            data = bundle.getByteArray(EmvServiceConstraints.CONFIG);
            if (data == null) {
                return;
            }

            BerTlvParser tlvParser = new BerTlvParser();
            List<BerTlv> tlvList = tlvParser.parse(data).findAll(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DELIMITER));

            for (int i = 0; i < tlvList.size(); i++) {
                BerTlvs tlvs = new BerTlvParser().parse(tlvList.get(i).getBytesValue());
                BerTlv tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_ID));
                Service service = new Service();

                if (tlv == null) {
                    continue;
                } else {
                    service.Active = true;
                    service.ServiceID = tlv.getBytesValue();
                }

                tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRIORITY));
                if (tlv == null || tlv.getBytesValue().length != 1) {
                    continue;
                } else {
                    service.Priority = tlv.getBytesValue()[0];
                }

                tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_MANAGEMENT));
                if (tlv == null || tlv.getBytesValue().length != 2) {
                    continue;
                } else {
                    service.ServiceManage = tlv.getBytesValue();
                }

                tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DATA));
                if (tlv == null) {
                    continue;
                } else {
                    service.ServiceData = tlv.getBytesValue();
                }

                tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMISS));
                if (tlv == null) {
                    continue;
                } else {
                    service.PRMiss = tlv.getBytesValue();
                }

                tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMACQ));
                if (tlv != null) {
                    service.PRMacq = dataToPrmacq(tlv.getBytesValue());
                }

                services[i] = service;
            }
        }
    }

    private boolean writeService() {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        for (Service service : services) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DELIMITER), serviceToData(service)));
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvServiceConstraints.CONFIG, tlvBuilder.buildArray());
        int result = emvCoreManager.EmvSetService(bundle);
        return PosEmvErrorCode.EMV_OK == result;
    }

    public byte[] serviceToData(Service service) {
        byte[] data;
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();

        data = service.ServiceID;
        if (data != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_ID), data));
        }

        tlvBuilder.addByte(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRIORITY), service.Priority);

        data = service.ServiceManage;
        if (data != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_MANAGEMENT), data));
        }

        data = service.ServiceData;
        if (data != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DATA), data));
        }

        data = service.PRMiss;
        if (data != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMISS), data));
        }

        data = prmacqToData(service.PRMacq);
        if (data != null) {
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMACQ), data));
        }

        return tlvBuilder.buildArray();
    }

    public static byte[] prmacqToData(PRMacq[] prmacqs) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();

        for (PRMacq prmacq : prmacqs) {
            if (prmacq == null) {
                continue;
            }

            if (prmacq.Key == null || !(prmacq.Key.length == 8 || prmacq.Key.length == 16)) {
                continue;
            }

            if (prmacq.Kcv == null || prmacq.Kcv.length != 3) {
                continue;
            }

            BerTlvBuilder prmTlvBuilder = new BerTlvBuilder();
            prmTlvBuilder.addByte(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_INDEX), prmacq.Index);
            prmTlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KEY), prmacq.Key));
            prmTlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KCV), prmacq.Kcv));
            tlvBuilder.addBerTlv(new BerTlv(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_DELIMITER), prmTlvBuilder.buildArray()));
        }

        if (tlvBuilder.build() != 0) {
            return null;
        } else {
            return tlvBuilder.buildArray();
        }
    }

    public static PRMacq[] dataToPrmacq(byte[] data) {
        PRMacq[] prmacqs = new PRMacq[10];

        for (int i = 0; i < prmacqs.length; i++) {
            prmacqs[i] = new PRMacq();
        }

        BerTlvParser tlvParser = new BerTlvParser();
        List<BerTlv> tlvList = tlvParser.parse(data).findAll(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_DELIMITER));

        for (int i = 0; i < tlvList.size(); i++) {
            BerTlvs tlvs = new BerTlvParser().parse(tlvList.get(i).getBytesValue());
            BerTlv tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_INDEX));
            PRMacq prmacq = new PRMacq();

            if (tlv == null) {
                continue;
            } else {
                prmacq.Index = (byte) tlv.getIntValue();
            }

            tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KEY));
            if (tlv == null || !(tlv.getBytesValue().length == 8 || tlv.getBytesValue().length == 16)) {
                continue;
            } else {
                prmacq.Key = tlv.getBytesValue();
            }

            tlv = tlvs.find(new BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KCV));
            if (tlv != null && tlv.getBytesValue().length == 3) {
                prmacq.Kcv = tlv.getBytesValue();
            }

            prmacqs[i] = prmacq;
        }

        return prmacqs;
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

    static class Service {
        private boolean  Active;
        private byte     Priority;
        private byte[]   ServiceID;
        private byte[]   ServiceManage;
        private byte[]   ServiceData;
        private byte[]   PRMiss;
        private PRMacq[] PRMacq;
    }

    static class PRMacq {
        private byte   Index;
        private byte[] Key;
        private byte[] Kcv;
    }
}
