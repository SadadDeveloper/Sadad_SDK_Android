package com.sadadsdk.transaction;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.sadadsdk.sadad.SadadFragment;
import com.sadadsdk.utils.Debug;

public class JavaScriptInterface {

    private BankFragment bankFragment;
    private SadadFragment sadadFragment;

    private WebView mWebView;

    JavaScriptInterface(BankFragment fragment, WebView _webView) {
        bankFragment = fragment;
        mWebView = _webView;
    }

    public JavaScriptInterface(SadadFragment fragment, WebView _webView) {
        sadadFragment = fragment;
        mWebView = _webView;
    }

    @JavascriptInterface
    public void setResult(String val) {
        Debug.trace("JavaScriptInterfaceCalled : Credit", val);
        this.bankFragment.javascriptCallFinished(val);
    }

    @JavascriptInterface
    public void setResultDebitCard(String val) {
        Debug.trace("JavaScriptInterfaceCalled : Debit", val);
        this.bankFragment.javascriptCallForDebitFinished(val);
    }

    @JavascriptInterface
    public void setResultsdk(String val) {
        Debug.trace("JavaScriptInterfaceCalled : SDK", val);
        this.sadadFragment.javascriptCallForSadadLoginFinished(val);
    }
}
