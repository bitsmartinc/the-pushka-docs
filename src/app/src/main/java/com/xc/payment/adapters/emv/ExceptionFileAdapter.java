package com.xc.payment.adapters.emv;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvErrorCode;
import com.pos.sdk.emvcore.PosEmvExceptionFile;
import com.xc.payment.adapters.config.Config;
import com.xc.payment.adapters.config.IConfigAdapter;
import com.xc.payment.utils.tlv.HexUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExceptionFileAdapter implements IConfigAdapter {

    private POIEmvCoreManager emvCoreManager;
    private Map<String, List<Config>> params;
    private List<PosEmvExceptionFile> emvExceptionFiles;
    private int exceptionFileIndex;
    private String[] exceptionFileNames;

    public ExceptionFileAdapter() {
        emvCoreManager = POIEmvCoreManager.getDefault();
        params = new LinkedHashMap<>();
        emvExceptionFiles = emvCoreManager.EmvGetExceptionFile();

        List<Config> list = new ArrayList<>();
        list.add(Config.EXCEPTION_FILE_INDEX);
        params.put("Name", list);

        if (emvExceptionFiles != null && !emvExceptionFiles.isEmpty()) {
            list = new ArrayList<>();
            list.add(Config.EXCEPTION_FILE_PAN);
            list.add(Config.EXCEPTION_FILE_SERIAL_NO);

            exceptionFileIndex = 0;
            exceptionFileNames = new String[emvExceptionFiles.size()];
            for (int i = 0; i < emvExceptionFiles.size(); i++) {
                exceptionFileNames[i] = (HexUtil.toHexString(emvExceptionFiles.get(i).PAN));
            }
        } else {
            exceptionFileNames = new String[1];
            exceptionFileNames[0] = "No ExceptionFile";
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
            case EXCEPTION_FILE_INDEX:
                return exceptionFileNames[exceptionFileIndex];
            case EXCEPTION_FILE_PAN:
            case EXCEPTION_FILE_SERIAL_NO:
                return getConfig(exceptionFileIndex, config);
            default:
                break;
        }
        return null;
    }

    @Override
    public Object[] getValueList(Config config) {
        if (config == Config.EXCEPTION_FILE_INDEX) {
            return exceptionFileNames;
        }
        return null;
    }

    @Override
    public boolean setValue(Config config, Object data) {
        boolean result;
        switch (config) {
            case EXCEPTION_FILE_INDEX:
                for (int i = 0; i < exceptionFileNames.length; i++) {
                    if (exceptionFileNames[i].equals(data)) {
                        exceptionFileIndex = i;
                    }
                }
                result = true;
                break;
            case EXCEPTION_FILE_PAN:
            case EXCEPTION_FILE_SERIAL_NO:
                result = setConfig(exceptionFileIndex, config, data);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    private String getConfig(int index, Config config) {
        String value = "";
        PosEmvExceptionFile exceptionFile = emvExceptionFiles.get(index);
        switch (config) {
            case EXCEPTION_FILE_INDEX:
                return exceptionFileNames[exceptionFileIndex];
            case EXCEPTION_FILE_PAN:
                value = HexUtil.toHexString(exceptionFile.PAN);
                break;
            case EXCEPTION_FILE_SERIAL_NO:
                value = HexUtil.toHexString(exceptionFile.SerialNo);
                break;
            default:
                break;
        }
        return value;
    }

    private boolean setConfig(int index, Config config, Object data) {
        boolean result;
        String value = (String) data;
        PosEmvExceptionFile exceptionFile = emvExceptionFiles.get(index);
        switch (config) {
            case EXCEPTION_FILE_PAN:
                result = (12 < value.length() && value.length() < 19);
                if (result) {
                    exceptionFile.PAN = HexUtil.parseHex(value);
                }
                break;
            case EXCEPTION_FILE_SERIAL_NO:
                result = (value.length() == 2);
                if (result) {
                    exceptionFile.SerialNo = HexUtil.parseHex(value);
                }
                break;
            default:
                result = false;
                break;
        }
        if (result) {
            result = PosEmvErrorCode.EMV_OK == emvCoreManager.EmvSetExceptionFile(exceptionFile);
        }
        return result;
    }
}
