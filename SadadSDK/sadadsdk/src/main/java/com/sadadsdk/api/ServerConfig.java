package com.sadadsdk.api;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public class ServerConfig {

    /*Live AWS - server URL*/
    public static final String SERVER_LIVE_URL = "https://mapi.sadadqa.com/sdk-api/";
    public static final String CREDIT_CARD_LIVE_URL = "https://sadadqa.com/ccapi/pay.php";
    public static final String DEBIT_CARD_LIVE_URL = "https://sadadqa.com/napsapi/pay.php"; //
    public static final String SADAD_LOGIN_LIVE_URL = "https://staging.sadadqa.com/login";
//==================================================================================================

    /*Live AWS - Sandbox URL*/
    public static final String SERVER_SANDBOX_URL = "https://msandboxapi.sadadqa.com/sdk-api/";
//    public static final String CREDIT_CARD_SANDBOX_URL = "https://bankapi.sadadqa.com/25/PHP_VPC_3DS%202.5%20Party_Order.php";
    public static final String CREDIT_CARD_SANDBOX_URL = "https://bankapi.sadadqa.com/ccapi/pay.php";
//    public static final String DEBIT_CARD_SANDBOX_URL = "https://bankapi.sadadqa.com/ezpay/step2.php";
    public static final String DEBIT_CARD_SANDBOX_URL = "https://bankapi.sadadqa.com/napsapi/pay.php";
    public static final String SADAD_LOGIN_SANDBOX_URL = "https://sadadsdk.sadadqa.com/login";
//==================================================================================================

    /*3001- server URL*/
    public static final String SERVER_TEST_URL = "http://176.58.99.102:3001/sdk-api/";
    public static final String CREDIT_CARD_TEST_URL = "http://sadad.de/bankapi/25/PHP_VPC_3DS%202.5%20Party_Order.php";
    public static final String DEBIT_CARD_TEST_URL = "http://sadad.de/bankapi/ezpay/step2.php";
    public static final String SADAD_LOGIN_TEST_URL = "http://sadad.de/sadadsdk/login";
//==================================================================================================

    /*Local - server URL*/
    public static final String SERVER_LOCAL_URL = "http://192.168.1.174:3002/sdk-api/";
    public static final String CREDIT_CARD_LOCAL_URL = "http://sadad.de/bankapi/25/PHP_VPC_3DS%202.5%20Party_Order.php";
    public static final String DEBIT_CARD_LOCAL_URL = "http://sadad.de/bankapi/ezpay/step2.php";
    public static final String SADAD_LOGIN_LOCAL_URL = "http://192.168.1.106/sadadsdk/login";
//==================================================================================================
}
