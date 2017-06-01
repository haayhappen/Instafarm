package com.haayhappen.instafarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.vending.billing.IInAppBillingService;

public class PaymentActivity extends AppCompatActivity {

    //Variables Declarations



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
