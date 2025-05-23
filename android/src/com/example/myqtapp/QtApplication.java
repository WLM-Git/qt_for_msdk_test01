package com.example.myqtapp;

import android.content.Context;
import dji.v5.manager.SDKManager;
import dji.v5.manager.interfaces.SDKManagerCallback;
import dji.v5.common.error.IDJIError;
import dji.v5.common.register.DJISDKInitEvent;
import android.util.Log;

public class QtApplication extends org.qtproject.qt5.android.bindings.QtApplication {

    private static final String TAG = QtApplication.class.getSimpleName();
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        com.cySdkyc.clx.Helper.install(this); // 关键初始化
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKManager.getInstance().init(this, new SDKManagerCallback() {

            @Override
            public void onInitProcess(DJISDKInitEvent event, int totalProcess) {
                Log.i(TAG, "onInitProcess: ");
                if (event == DJISDKInitEvent.INITIALIZE_COMPLETE) {
                    SDKManager.getInstance().registerApp();
                }
            }

            @Override
            public void onRegisterSuccess() {
                Log.i(TAG, "onRegisterSuccess: ");
            }

            @Override
            public void onRegisterFailure(IDJIError error) {
                Log.i(TAG, "onRegisterFailure: ");
            }

            @Override
            public void onProductConnect(int productId) {
                Log.i(TAG, "onProductConnect: ");
            }

            @Override
            public void onProductDisconnect(int productId) {
                Log.i(TAG, "onProductDisconnect: ");
            }

            @Override
            public void onProductChanged(int productId) {
                Log.i(TAG, "onProductChanged: ");
            }

            @Override
            public void onDatabaseDownloadProgress(long current, long total) {
                Log.i(TAG, "onDatabaseDownloadProgress: " + (current / total));
            }
        });
    }
}
