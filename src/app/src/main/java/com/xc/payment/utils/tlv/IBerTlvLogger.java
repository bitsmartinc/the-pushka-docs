package com.xc.payment.utils.tlv;

public interface IBerTlvLogger {

    boolean isDebugEnabled();

    void debug(String aFormat, Object... args);
}
