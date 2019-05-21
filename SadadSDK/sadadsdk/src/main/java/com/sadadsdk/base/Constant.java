package com.sadadsdk.base;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class Constant {


    public static final int REQUEST_CODE_PAY_NOW = 1001;
    public static final int REQUEST_CODE_CREDIT_CARD = 1003;
    public static final int REQUEST_CODE_DEBIT_CARD = 1004;
    public static final int REQUEST_CODE_PAY_VIA_SADAD = 1005;

    public static final int RESULT_CODE_PAYMENT_CANCEL = 1002;
    public static final int RESULT_CODE_PAYMENT_FAILED = 1006;
    public static final int SDK_RESULT_CODE = 1007;
    public static final int RESULT_CODE_PAYMENT_SUCCESS = 1008;

    public static final String CARD_TYPE = "cardType";
    public static final int CREDIT_CARD = 1;
    public static final int DEBIT_CARD = 2;

    public static final String SADAD_PACKAGE_NAME = "com.sadadqa";
    public static final String IS_FROM_SDK = "isFromSdk";


    public static final int DEBIT_CARD_TRANSACTION_MODE_ID = 2;
    public static final int CREDIT_CARD_TRANSACTION_MODE_ID = 1;
    public static final int SADAD_TRANSACTION_MODE_ID = 3;


    public static final int TRANSACTION_STATUS_ID_INPROGRESS = 1;
    public static final int TRANSACTION_STATUS_ID_FAILED = 2;
    public static final int TRANSACTION_STATUS_ID_SUCCESS = 3;
    public static final int TRANSACTION_STATUS_ID_REFUNDED = 4;
    public static final int TRANSACTION_STATUS_ID_PENDING = 5;

    public static final int TRANSACTION_ENTITY_SDK = 9;

    public static final String TRANSACTION_RESPONSE = "transactionResponse";
    public static final String AMOUNT = "amount";
    public static final String COMMA_CURRENCY_FORMAT = "%1$,.2f";
    public static final String TRANSACTION_MODE = "transactionMode";
    public static final String INVOICE_NUMBER = "invoiceNumber";
    public static final String CARD_NUMBER = "cardNumber";
    public static final String CARD_EXPIRY_DATE = "cardExpiryDate";
    public static final String CARD_CVV_NUMBER = "cardCVVNumber";

    public static final String SDK_DATA = "sdkData";

    public static final String INTERFACE = "interface";

    public static final String STATUS_CODE = "statusCode";
    public static final String MESSAGE = "message";
}

