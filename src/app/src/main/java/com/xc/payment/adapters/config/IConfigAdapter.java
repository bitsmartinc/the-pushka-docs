package com.xc.payment.adapters.config;

import java.util.List;
import java.util.Map;

public interface IConfigAdapter {

    Map<String, List<Config>> getDataList();

    Object getValue(Config config);

    Object[] getValueList(Config config);

    boolean setValue(Config config, Object data);
}
