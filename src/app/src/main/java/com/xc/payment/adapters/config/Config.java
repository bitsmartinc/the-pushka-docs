package com.xc.payment.adapters.config;

public enum Config {

    TRANSACTION_TYPE(ConfigView.VIEW_SELECT, "Transaction Type"),
    SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    SUPPORT_CONTACTLESS(ConfigView.VIEW_SW, "Support Contactless"),
    SUPPORT_MAGSTRIPE(ConfigView.VIEW_SW, "Support Magstripe"),
    SUPPORT_APPLE_VAS(ConfigView.VIEW_SW, "Support AppleVAS"),
    SUPPORT_GOOGLE_SMART_TAP(ConfigView.VIEW_SW, "Support GoogleSmartTap"),
    ONLINE_CONFIG(ConfigView.VIEW_SELECT, "Online Config"),
    ONLINE_RESULT(ConfigView.VIEW_SELECT, "Online Result"),
    KEYBOARD_FIX(ConfigView.VIEW_SW, "Keyboard Fix"),

    TERMINAL_TYPE(ConfigView.VIEW_TXT, "Terminal Type (TAG 9F35)"),
    TERMINAL_CAPABILITY(ConfigView.VIEW_TXT, "Terminal Capability (TAG 9F33)"),
    TERMINAL_ADDITION_TERMINAL_CAPABILITY(ConfigView.VIEW_TXT, "Addition Terminal Capability (TAG 9F40)"),
    TERMINAL_ID(ConfigView.VIEW_TXT, "Terminal ID (TAG 9F1C)"),
    TERMINAL_COUNTRY_CODE(ConfigView.VIEW_TXT, "Terminal Country Code (TAG 9F1A)"),
    TERMINAL_ENTRY_MODE(ConfigView.VIEW_TXT, "POS Entry Mode (TAG 9F39)"),
    MERCHANT_ID(ConfigView.VIEW_TXT, "Merchant ID (TAG 9F16)"),
    MERCHANT_CATEGORY_CODE(ConfigView.VIEW_TXT, "Merchant Category Code (TAG 9F15)"),
    MERCHANT_NAME(ConfigView.VIEW_TXT, "Merchant Name (TAG 9F4E)"),
    TRANSACTION_CURRENCY_CODE(ConfigView.VIEW_TXT, "Transaction Currency Code (TAG 5F2A)"),
    TRANSACTION_CURRENCY_EXP(ConfigView.VIEW_TXT, "Transaction Currency Exp (TAG 5F36)"),
    TRANSACTION_REFER_CURRENCY_CODE(ConfigView.VIEW_TXT, "Transaction Refer Currency Code (TAG 9F3C)"),
    TRANSACTION_REFER_CURRENCY_EXP(ConfigView.VIEW_TXT, "Transaction Refer Currency Exp (TAG 9F3D)"),
    IFD_SERIAL_NUMBER(ConfigView.VIEW_TXT, "IFD Serial Number (TAG 9F1E)"),

    CONFIG_PSE(ConfigView.VIEW_SW, "PSE"),
    CONFIG_CARDHOLDER_CONFIRM(ConfigView.VIEW_SW, "Cardholder Confirm"),
    CONFIG_LANGUAGE_SELECT(ConfigView.VIEW_SW, "Language Select"),
    CONFIG_DEFAULT_DDOL(ConfigView.VIEW_SW, "Default dDOL"),
    CONFIG_DEFAULT_TDOL(ConfigView.VIEW_SW, "Default tDOL"),
    CONFIG_BYPASS_PIN_ENTRY(ConfigView.VIEW_SW, "Bypass Pin Entry"),
    CONFIG_SUBSEQUENT_BYPASS_PIN_ENTRY(ConfigView.VIEW_SW, "Sub Bypass Pin Entry"),
    CONFIG_GET_DATA_FOR_PIN_COUNTER(ConfigView.VIEW_SW, "Get Pin Counter"),
    CONFIG_FLOOR_LIMIT_CHECKING(ConfigView.VIEW_SW, "Floor Limit Check"),
    CONFIG_RANDOM_TRANSACTION_SELECTION(ConfigView.VIEW_SW, "Random Transaction Select"),
    CONFIG_VELOCITY_CHECKING(ConfigView.VIEW_SW, "Velocity Check"),
    CONFIG_EXCEPTION_FILE(ConfigView.VIEW_SW, "Exception File"),
    CONFIG_REVOCATION_ISSUER_PUBLIC_KEY(ConfigView.VIEW_SW, "Revocation Issuer Public Key"),
    CONFIG_ISSUER_REFERRAL(ConfigView.VIEW_SW, "Issuer Referral"),
    CONFIG_UNABLE_TO_GO_ONLINE(ConfigView.VIEW_SW, "Unable To Go Online"),
    CONFIG_FORCED_ONLINE(ConfigView.VIEW_SW, "Force Online"),
    CONFIG_FORCED_ACCEPT(ConfigView.VIEW_SW, "Force Accept"),

    AID_AID(ConfigView.VIEW_SELECT, "Application Id (9F06)"),
    AID_VERSION(ConfigView.VIEW_TXT, "Version (9F09)"),
    AID_TYPE_FLAG(ConfigView.VIEW_SW, "Type Flag (Contact or Contactless)"),
    AID_SELECT_FLAG(ConfigView.VIEW_SW, "Select Flag (Full or Part)"),
    AID_ACQUIRER_ID(ConfigView.VIEW_TXT, "Acquirer Identifier (9F01)"),
    AID_DDOL(ConfigView.VIEW_TXT, "Default dDOL"),
    AID_TDOL(ConfigView.VIEW_TXT, "Default tDOL"),
    AID_TAC_DENIAL(ConfigView.VIEW_TXT, "TAC Denial"),
    AID_TAC_ONLINE(ConfigView.VIEW_TXT, "TAC Online"),
    AID_TAC_DEFAULT(ConfigView.VIEW_TXT, "TAC Default"),
    AID_THRESHOLD(ConfigView.VIEW_TXT, "Threshold"),
    AID_TARGET_PERCENTAGE(ConfigView.VIEW_TXT, "Target Percentage"),
    AID_MAX_TARGET_PERCENTAGE(ConfigView.VIEW_TXT, "Maximum Target Percentage"),
    AID_FLOOR_LIMIT(ConfigView.VIEW_TXT, "Floor Limit"),
    AID_CONTACTLESS_TRANS_LIMIT(ConfigView.VIEW_TXT, "Contactless Trans Limit"),
    AID_CONTACTLESS_CVM_LIMIT(ConfigView.VIEW_TXT, "Contactless CVM Limit"),
    AID_CONTACTLESS_FLOOR_LIMIT(ConfigView.VIEW_TXT, "Contactless Floor Limit"),
    AID_DYNAMIC_TRANS_LIMIT(ConfigView.VIEW_TXT, "Dynamic Trans Limit"),
    AID_TERMINAL_TYPE(ConfigView.VIEW_TXT, "Terminal Type (9F35)"),
    AID_TERMINAL_CAPABILITY(ConfigView.VIEW_TXT, " Terminal Capability (9F33)"),
    AID_TERMINAL_ADDITION_CAPABILITY(ConfigView.VIEW_TXT, "Addition Terminal Capability (9F40)"),
    AID_TERMINAL_RISK_MANAGE_DATA(ConfigView.VIEW_TXT, "Terminal Risk Manage Data (9F1D)"),
    AID_TERMINAL_COUNTRY_CODE(ConfigView.VIEW_TXT, "Terminal Country Code (TAG 9F1A)"),
    AID_MERCHANT_CATEGORY_CODE(ConfigView.VIEW_TXT, "Merchant Category Code (TAG 9F15)"),
    AID_TRANSACTION_CURRENCY_CODE(ConfigView.VIEW_TXT, "Transaction Currency Code (TAG 5F2A)"),
    AID_TRANSACTION_CURRENCY_EXP(ConfigView.VIEW_TXT, "Transaction Currency Exp (TAG 5F36)"),

    CAPK_RID(ConfigView.VIEW_SELECT, "RID"),
    CAPK_KEY_ID(ConfigView.VIEW_TXT, "Key ID"),
    CAPK_ALGO_IND(ConfigView.VIEW_TXT, "Algorithm"),
    CAPK_HASH_IND(ConfigView.VIEW_TXT, "Hash Algorithm"),
    CAPK_MODULE(ConfigView.VIEW_TXT, "Module"),
    CAPK_EXPONENT(ConfigView.VIEW_TXT, "Exponent"),
    CAPK_CHECKSUM(ConfigView.VIEW_TXT, "Checksum"),

    EXCEPTION_FILE_INDEX(ConfigView.VIEW_SELECT, "Index"),
    EXCEPTION_FILE_PAN(ConfigView.VIEW_TXT, "PAN"),
    EXCEPTION_FILE_SERIAL_NO(ConfigView.VIEW_TXT, "Serial No"),

    REVOCATION_IPK_INDEX(ConfigView.VIEW_SELECT, "Index"),
    REVOCATION_IPK_RID(ConfigView.VIEW_TXT, "RID"),
    REVOCATION_IPK_CAPK_ID(ConfigView.VIEW_TXT, "Index"),
    REVOCATION_IPK_SERIAL_NO(ConfigView.VIEW_TXT, "Serial No"),

    RISK_CTL_CHECK(ConfigView.VIEW_SW, "Trans Limit Check"),
    RISK_TRANS_LIMIT(ConfigView.VIEW_TXT, "Contactless Trans Limit"),
    RISK_CVM_CHECK(ConfigView.VIEW_SW, "CVM Limit Check"),
    RISK_CVM_LIMIT(ConfigView.VIEW_TXT, "Contactless CVM Limit"),
    RISK_CFL_CHECK(ConfigView.VIEW_SW, "Floor Limit Check"),
    RISK_CONTACTLESS_CFL_CHECK(ConfigView.VIEW_SW, "Contactless Floor Limit Check"),
    RISK_CONTACTLESS_FLOOR_LIMIT(ConfigView.VIEW_TXT, "Contactless Floor Limit"),
    RISK_FLOOR_LIMIT(ConfigView.VIEW_TXT, "Floor Limit"),
    RISK_DYNAMIC_LIMIT(ConfigView.VIEW_TXT, "Dynamic Trans Limit"),
    RISK_STATUS_CHECK(ConfigView.VIEW_SW, "Status Check Flag"),
    RISK_ZERO_AMOUNT_CHECK(ConfigView.VIEW_SW, "Zero Amount Flag"),
    RISK_ZERO_AMOUNT_PATH(ConfigView.VIEW_SW, "Zero Amount Path"),

    DRL_INDEX(ConfigView.VIEW_SELECT, "Index"),
    DRL_ACTIVE(ConfigView.VIEW_SW, "Active"),
    DRL_PROGRAM_ID(ConfigView.VIEW_TXT, "Program ID"),

    SERVICE_INDEX(ConfigView.VIEW_SELECT, "Index"),
    SERVICE_ACTIVE(ConfigView.VIEW_SW, "Active"),
    SERVICE_ID(ConfigView.VIEW_TXT, "Service ID"),
    SERVICE_PRIORITY(ConfigView.VIEW_TXT, "Priority"),
    SERVICE_MANAGE_INFO(ConfigView.VIEW_TXT, "Service Manage Info"),
    SERVICE_DATA(ConfigView.VIEW_TXT, "Service Data"),
    SERVICE_PRMISS(ConfigView.VIEW_TXT, "PRMiss"),
    SERVICE_PRMACQ(ConfigView.VIEW_SELECT, "PRMacq"),
    SERVICE_PRMACQ_INDEX(ConfigView.VIEW_TXT, "Index"),
    SERVICE_PRMACQ_KEY(ConfigView.VIEW_TXT, "Key"),
    SERVICE_PRMACQ_KCV(ConfigView.VIEW_TXT, "Kcv"),

    VISA_SUPPORT_MAGSTRIPE(ConfigView.VIEW_SW, "Support Magstripe"),
    VISA_SUPPORT_QVSDC(ConfigView.VIEW_SW, "Support QVSDC"),
    VISA_SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    VISA_OFFLINE_ONLY(ConfigView.VIEW_SW, "Offline Only"),
    VISA_SUPPORT_ONLINE_PIN(ConfigView.VIEW_SW, "Support OnlinePIN"),
    VISA_SUPPORT_SIGNATURE(ConfigView.VIEW_SW, "Support Signature"),
    VISA_SUPPORT_ONLINE_ODA(ConfigView.VIEW_SW, "Support OnlineODA"),
    VISA_SUPPORT_ISSUER_SCRIPT_UPDATE(ConfigView.VIEW_SW, "Support IssuerUpdateProcessing"),
    VISA_SUPPORT_CDCVM(ConfigView.VIEW_SW, "Support CDCVM"),
    VISA_SUPPORT_DRL(ConfigView.VIEW_SW, "Support DRL"),

    UNIONPAY_SUPPORT_PBOC(ConfigView.VIEW_SW, "Support PBOC"),
    UNIONPAY_SUPPORT_QPBOC(ConfigView.VIEW_SW, "Support QPBOC"),
    UNIONPAY_SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    UNIONPAY_OFFLINE_ONLY(ConfigView.VIEW_SW, "Offline Only"),
    UNIONPAY_SUPPORT_ONLINE_PIN(ConfigView.VIEW_SW, "Support OnlinePIN"),
    UNIONPAY_SUPPORT_SIGNATURE(ConfigView.VIEW_SW, "Support Signature"),
    UNIONPAY_SUPPORT_CDCVM(ConfigView.VIEW_SW, "Support CDCVM"),

    MASTERCARD_CVM_CAPABILITIES(ConfigView.VIEW_TXT, "Capabilities CVM Required"),
    MASTERCARD_NO_CVM_CAPABILITIES(ConfigView.VIEW_TXT, "Capabilities No CVM Required"),
    MASTERCARD_MAGSTRIPE_CVM_CAPABILITIES(ConfigView.VIEW_TXT, "Magstripe Capabilities CVM Required"),
    MASTERCARD_MAGSTRIPE_NO_CVM_CAPABILITIES(ConfigView.VIEW_TXT, "Magstripe Capabilities No CVM Required"),
    MASTERCARD_MAGSTRIPE_APP_VERSION(ConfigView.VIEW_TXT, "Magstripe Application Version"),
    MASTERCARD_MOBILE_SUPPORT_INDICATOR(ConfigView.VIEW_TXT, "Mobile Support Indicator"),
    MASTERCARD_KERNEL_ID(ConfigView.VIEW_TXT, "Kernel ID"),
    MASTERCARD_DEFAULT_UDOL(ConfigView.VIEW_TXT, "Default UDOL"),
    MASTERCARD_KERNEL_CONFIG(ConfigView.VIEW_TXT, "Kernel Config"),
    MASTERCARD_RRP_MIN_GRACE(ConfigView.VIEW_TXT, "RRP Minimum Grace"),
    MASTERCARD_RRP_MAX_GRACE(ConfigView.VIEW_TXT, "RRP Maximum Grace"),
    MASTERCARD_RRP_CAPDU_EXPECTED(ConfigView.VIEW_TXT, "RRP Expected Transmission Time C-APDU"),
    MASTERCARD_RRP_RAPDU_EXPECTED(ConfigView.VIEW_TXT, "RRP Expected Transmission Time R-APDU"),
    MASTERCARD_RRP_ACCURACY_THRESHOLD(ConfigView.VIEW_TXT, "RRP Accuracy Threshold"),
    MASTERCARD_RRP_MISMATCH_THRESHOLD(ConfigView.VIEW_TXT, "RRP Mismatch Threshold"),

    DISCOVER_SUPPORT_MAGSTRIPE(ConfigView.VIEW_SW, "Support Magstripe"),
    DISCOVER_SUPPORT_EMV(ConfigView.VIEW_SW, "Support EMV"),
    DISCOVER_SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    DISCOVER_OFFLINE_ONLY(ConfigView.VIEW_SW, "Offline Only"),
    DISCOVER_SUPPORT_ONLINE_PIN(ConfigView.VIEW_SW, "Support OnlinePIN"),
    DISCOVER_SUPPORT_SIGNATURE(ConfigView.VIEW_SW, "Support Signature"),
    DISCOVER_SUPPORT_ISSUER_SCRIPT_UPDATE(ConfigView.VIEW_SW, "Support IssuerUpdateProcessing"),
    DISCOVER_SUPPORT_CDCVM(ConfigView.VIEW_SW, "Support CDCVM"),

    AMEX_SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    AMEX_SUPPORT_MAGSTRIPE(ConfigView.VIEW_SW, "Support Magstripe"),
    AMEX_SUPPORT_EMV(ConfigView.VIEW_SW, "Support EMV"),
    AMEX_TRY_ANOTHER_INTERFACE(ConfigView.VIEW_SW, "Try Another Interface"),
    AMEX_SUPPORT_CDCVM(ConfigView.VIEW_SW, "Support CDCVM"),
    AMEX_SUPPORT_ONLINE_PIN(ConfigView.VIEW_SW, "Support OnlinePIN"),
    AMEX_SUPPORT_SIGNATURE(ConfigView.VIEW_SW, "Support Signature"),
    AMEX_OFFLINE_ONLY(ConfigView.VIEW_SW, "Offline Only"),
    AMEX_EXEMPT_NO_CVM_CHECK(ConfigView.VIEW_SW, "Exempt NoCVMCheck"),
    AMEX_DELAYED_AUTHORIZATION(ConfigView.VIEW_SW, "Delayed Authorization"),
    AMEX_SUPPORT_DRL(ConfigView.VIEW_SW, "Support DRL"),

    MIR_SUPPORT_ONLINE_PIN(ConfigView.VIEW_SW, "Support OnlinePIN"),
    MIR_SUPPORT_SIGNATURE(ConfigView.VIEW_SW, "Support Signature"),
    MIR_SUPPORT_CDCVM(ConfigView.VIEW_SW, "Support CDCVM"),
    MIR_UNABLE_ONLINE(ConfigView.VIEW_SW, "Unable Online"),
    MIR_SUPPORT_CONTACT(ConfigView.VIEW_SW, "Support Contact"),
    MIR_OFFLINE_ONLY(ConfigView.VIEW_SW, "Offline Only"),
    MIR_DELAYED_AUTHORIZATION(ConfigView.VIEW_SW, "Delayed Authorization"),
    MIR_ATM(ConfigView.VIEW_SW, "ATM"),

    APPLE_CONFIG(ConfigView.VIEW_SELECT, "Config"),
    APPLE_PROTOCOL(ConfigView.VIEW_SELECT, "Protocol"),
    APPLE_CAPABILITY(ConfigView.VIEW_SELECT, "Capability"),
    APPLE_MERCHANT(ConfigView.VIEW_SELECT, "Merchant"),
    APPLE_MERCHANT_ID(ConfigView.VIEW_TXT, "Merchant Id"),
    APPLE_MERCHANT_URL(ConfigView.VIEW_TXT, "Merchant Url"),
    APPLE_FILTER(ConfigView.VIEW_TXT, "Filter");

    private ConfigView type;
    private String     name;

    Config(final ConfigView type, final String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfigView getType() {
        return type;
    }
}
