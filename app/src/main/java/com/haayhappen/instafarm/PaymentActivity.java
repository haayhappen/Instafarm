package com.haayhappen.instafarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.entity.mime.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PaymentActivity extends Activity /*implements BillingProcessor.IBillingHandler */ {

    //private static final int REQUEST_CODE = 1;
    private static final int BRAINTREE_REQUEST_CODE = 4949;
    BillingProcessor bp;
    Button goldButton;
    Button platinumButton;
    Button createToken;
    boolean billingIsReady = false;
    Activity act;
    Object purchases;
    Context con;
    public String clientToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //get views
        goldButton = (Button) findViewById(R.id.goldbutton);
        goldButton.setOnClickListener(onGoldClicked);
        platinumButton = (Button) findViewById(R.id.platinumbutton);
        platinumButton.setOnClickListener(onPlatinumClicked);
        createToken = (Button) findViewById(R.id.tokebutton);
        createToken.setOnClickListener(onCreateTokenClicked);

//        act = PaymentActivity.this;
//
//        //bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtSyA+1LP+MezZ2vSsduUCEKCCi9Hc7T4aOE3a2CheciAzBLpD87YwPQiDlISTR5Z0OGXcTjOuBEHSVNbo1EOPMZkfmWdtJ3dNFJjs65MJY0h2XTlhKof7mnGXflqZ+TnlFuEqt9kaKaeSbTjE18GjsxCFWBjCcwR7g+1p5H/BlPcWSM+ycTA6ZQFD60uZpbe2HyQyqkEuvuIam19ocLPEb0EXl7LqUPMjsmh941GkuRYY4LCu1EUKYaBEnFGd9P7bbLPRrlh9y4Es0nzzGLPhGi/v3mtStUPbUyhBEahw2N5steLP0sO/yws2wb+Chdg5jxsX1X+CEL31w/2LfhWNwIDAQAB", this);
//        bp = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtSyA+1LP+MezZ2vSsduUCEKCCi9Hc7T4aOE3a2CheciAzBLpD87YwPQiDlISTR5Z0OGXcTjOuBEHSVNbo1EOPMZkfmWdtJ3dNFJjs65MJY0h2XTlhKof7mnGXflqZ+TnlFuEqt9kaKaeSbTjE18GjsxCFWBjCcwR7g+1p5H/BlPcWSM+ycTA6ZQFD60uZpbe2HyQyqkEuvuIam19ocLPEb0EXl7LqUPMjsmh941GkuRYY4LCu1EUKYaBEnFGd9P7bbLPRrlh9y4Es0nzzGLPhGi/v3mtStUPbUyhBEahw2N5steLP0sO/yws2wb+Chdg5jxsX1X+CEL31w/2LfhWNwIDAQAB", this); // doesn't bind
//        bp.initialize(); // binds

//        getOwnedPurchases();
        con = PaymentActivity.this;

    }

    void postNonceToServer(String nonce) {
        ApiInterface mApiService = this.getInterfaceService();
        Call<Nonce> mService = mApiService.sendNonce(nonce);
        mService.enqueue(new Callback<Nonce>() {
            @Override
            public void onResponse(Call<Nonce> call, Response<Nonce> response) {
                Nonce nonce = response.body();
                String success = nonce.success;

                if (success == "1") {
                    //... success
                } else
                    Toast.makeText(PaymentActivity.this, "Sending Nonce failed.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Nonce> call, Throwable t) {
                call.cancel();
                Toast.makeText(PaymentActivity.this, "Sending Nonce failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    View.OnClickListener onGoldClicked = new View.OnClickListener() {
        public void onClick(View v) {
//            boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
//            if (isSubsUpdateSupported) {
//                bp.subscribe(act, "weekly2");
//            }

            //TODO get refreshed token

            DropInRequest dropInRequest = new DropInRequest()
                    .clientToken(clientToken);
            startActivityForResult(dropInRequest.getIntent(con), BRAINTREE_REQUEST_CODE);
        }
    };
    View.OnClickListener onPlatinumClicked = new View.OnClickListener() {
        public void onClick(View v) {
//            boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
//            if (isSubsUpdateSupported) {
//                bp.subscribe(act, "weekly5");
//            }
        }
    };

    View.OnClickListener onCreateTokenClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createToken();
        }
    };

    //Builder for the Retrofit Interface
    private ApiInterface getInterfaceService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://62.75.253.50/API/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    public void setToken(String token) {
        this.clientToken = token;
    }

    public void createToken() {
        ApiInterface mApiService = this.getInterfaceService();
        Call<ClientToken> mService = mApiService.gettoken();
        mService.enqueue(new Callback<ClientToken>() {
            @Override
            public void onResponse(Call<ClientToken> call, Response<ClientToken> response) {
                ClientToken ct = response.body();
                String clientToken = ct.token;

                if (clientToken == null) {
                    //...
                } else
                    setToken(clientToken);
            }

            @Override
            public void onFailure(Call<ClientToken> call, Throwable t) {
                call.cancel();
                Toast.makeText(PaymentActivity.this, "Creating token failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public Object getOwnedPurchases() {
//        purchases = bp.loadOwnedPurchasesFromGoogle();
//        return purchases;
//    }

    // IBillingHandler implementation
/*
    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase

        billingIsReady = true;
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased

        //TODO SAFE PREMIUM STATE IN DATABASE
        boolean subActive = details.purchaseInfo.purchaseData.autoRenewing;
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
		 *
		 * Note - this includes handling the case where the user canceled the buy dialog:
		 * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED

    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
		 * was loaded from Google Play

    }
*/
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data))
//            super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String paymentNonce = result.getPaymentMethodNonce().getNonce();
                postNonceToServer(paymentNonce);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled


            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("PayPalError", "ERROR MESSAGE: " + error.getMessage());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        createToken();
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }
}