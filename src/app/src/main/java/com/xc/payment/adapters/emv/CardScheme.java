package com.xc.payment.adapters.emv;

public enum CardScheme {

    VISA("VISA", "A000000003"),
    MASTERCARD("MasterCard", "A000000004"),
    AMERICAN_EXPRESS("AmericanExpress", "A000000025"),
    DISCOVER("Discover", "A000000152", "A000000324"),
    UNIONPAY("UnionPay", "A000000333"),
    MIR("MIR", "A000000658"),
    RUPAY("RuPay", "A000000524");

    private final String[] aids;
    private final String   name;

    CardScheme(final String name, final String... aids) {
        this.name = name;
        this.aids = aids;
    }

    public String getName() {
        return name;
    }

    public String[] getAid() {
        return aids;
    }

    public static CardScheme getCardTypeByAid(final String aid) {
        CardScheme ret = null;
        if (aid != null) {
            for (CardScheme val : CardScheme.values()) {
                for (String aidName : val.getAid()) {
                    if (aid.startsWith(aidName)) {
                        ret = val;
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
