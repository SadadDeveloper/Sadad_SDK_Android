package cards.sadadsdk.sadad;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sufalamtech.sadad.sdk.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cards.sadadsdk.listener.OnBackPressedEvent;
import cards.sadadsdk.listener.TokenReceiver;
import cards.sadadsdk.model.SadadOrder;
import cards.sadadsdk.paymentselection.SadadService;
import cards.sadadsdk.transaction.JavaScriptInterface;
import cards.sadadsdk.utils.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SadadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SadadFragment extends Fragment implements OnBackPressedEvent {


    private static TokenReceiver mTokenReceiver;
    //    private Unbinder unbinder;
    private static SadadService mSadadService;
    //    @BindView(R2.id.wvSadad)
    private WebView wvSadad;

    public SadadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tokenReceiver Parameter 1.
     * @return A new instance of fragment SadadFragment.
     */

    public static SadadFragment newInstance(TokenReceiver tokenReceiver, SadadService sadadService) {
        SadadFragment fragment = new SadadFragment();
        mTokenReceiver = tokenReceiver;
        mSadadService = sadadService;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sadad, container, false);
//        unbinder = ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initViews(View view) {

        wvSadad = view.findViewById(R.id.wvSadad);

        wvSadad.getSettings().setJavaScriptEnabled(true);
        wvSadad.setWebChromeClient(new WebChromeClient());
        wvSadad.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CustomDialog.getInstance().showProgress(getActivity(), "", false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CustomDialog.getInstance().dismiss();
            }
        });

        wvSadad.addJavascriptInterface(new JavaScriptInterface(this, wvSadad), "MyHandler");

        buildPostData();
    }

    private void buildPostData() {

        StringBuilder response = new StringBuilder();

        Bundle requestParamMap = getArguments();

        if (requestParamMap != null) {

            response.append(SadadOrder.TXN_AMOUNT).append("=").append(requestParamMap.getString(SadadOrder.TXN_AMOUNT)).append("&");
            response.append(SadadOrder.ACCESS_TOKEN).append("=").append(requestParamMap.getString(SadadOrder.ACCESS_TOKEN)).append("&");
            response.append(SadadOrder.ORDER_DETAIL).append("=").append(requestParamMap.getString(SadadOrder.ORDER_DETAIL));

//            if (requestParamMap.getString(SadadOrder.ORDER_DETAIL, "").startsWith("[")) {
//                //response.append(SadadOrder.ORDER_DETAIL).append("=");
//
//                String encoded = getEncodedString(requestParamMap.getString(SadadOrder.ORDER_DETAIL, ""));
//                response.append(encoded);
//            }
            wvSadad.postUrl(mSadadService.getSadadLoginUrl(), EncodingUtils.getBytes(String.valueOf(response), "BASE64"));
        }
    }


    private String getEncodedString(String jsonObject) {

        StringBuilder response = new StringBuilder();
        try {
            JSONArray jsonArray = new JSONArray(jsonObject);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                Iterator<String> iterator = jsonObject1.keys();
                while (iterator.hasNext()) {
                    Object key = iterator.next();
                    response.append("PRODUCT_DETAILS[")
                            .append(i).append("][")
                            .append(key).append("]")
                            .append("value=")
                            .append(jsonObject1.opt(String.valueOf(key)))
                            .append("&");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }


    public void javascriptCallForSadadLoginFinished(String val) {

        mTokenReceiver.onSadadCall(val);
    }

    @Override
    public void onBackPressed() {

    }
}
