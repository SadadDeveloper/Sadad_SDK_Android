package com.sadadsdk.transaction;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.sadadsdk.sadad.SadadFragment;
import com.sadadsdk.utils.Debug;

public class JavaScriptInterface {

    private BankFragment bankFragment;
    private SadadFragment sadadFragment;

    JavaScriptInterface(BankFragment fragment) {
        bankFragment = fragment;
    }

    public JavaScriptInterface(SadadFragment fragment) {
        sadadFragment = fragment;
    }

    @JavascriptInterface
    public void setResult(String val) {
        Debug.trace("JavaScriptInterfaceCalled : Credit", val);
        this.bankFragment.javascriptCallForCreditCardFinished(val);
    }

    @JavascriptInterface
    public void setResultDebitCard(String val) {
        Debug.trace("JavaScriptInterfaceCalled : Debit", val);
        this.bankFragment.javascriptCallForDebitCardFinished(val);
    }

    @JavascriptInterface
    public void setResultsdk(String val) {
        Debug.trace("JavaScriptInterfaceCalled : SDK", val);
        this.sadadFragment.javascriptCallForSadadLoginFinished(val);
    }
}
