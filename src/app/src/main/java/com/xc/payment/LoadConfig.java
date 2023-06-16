package com.xc.payment;

import com.xc.payment.emv.EmvConfig;

public class LoadConfig {

    public static void load(){
        EmvConfig.loadTerminal();
        EmvConfig.loadAid();
        EmvConfig.loadCapk();
        EmvConfig.loadExceptionFile();
        EmvConfig.loadRevocationIPK();

        EmvConfig.loadVisa();
        EmvConfig.loadUnionPay();
        EmvConfig.loadMasterCard();
        EmvConfig.loadDiscover();
        EmvConfig.loadAmex();
        EmvConfig.loadMir();
        EmvConfig.loadVisaDRL();
        EmvConfig.loadAmexDRL();
        EmvConfig.loadService();
    }
}