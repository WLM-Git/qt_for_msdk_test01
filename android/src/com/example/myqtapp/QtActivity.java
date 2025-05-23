package com.example.myqtapp;

import android.os.Bundle;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import androidx.appcompat.app.AppCompatActivity;
import dji.sdk.keyvalue.key.KeyTools;
import dji.sdk.keyvalue.key.ProductKey;
import dji.sdk.keyvalue.value.mop.PipelineDeviceType;
import dji.sdk.keyvalue.value.mop.TransmissionControlType;
import dji.v5.manager.KeyManager;
import dji.v5.utils.common.ContextUtil;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class QtActivity extends org.qtproject.qt5.android.bindings.QtActivity {
    private MopVM mopVM;
    private static QtActivity instance;

    // 初始化时设置实例
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mopVM = new MopVM();
        mopVM.setDataCallback((data, length) -> {
            nativeDataReceived(data, length);
        });
    }

    // JNI回调方法（由C++实现）
    private native void nativeDataReceived(byte[] data, int length);

    // JNI接口方法
    public static void jniConnect(int channelId, int deviceType, int transferType, boolean isUseDown) {
        instance.connectToPSDK(channelId, deviceType, transferType, isUseDown);
    }

    public static void jniSend(byte[] data) {
        instance.sendDataToPSDK(new String(data));
    }

    public static void jniDisconnect() {
        instance.disconnectFromPSDK();
    }

    public void connectToPSDK(int channelId, int deviceTypeCode, int transferTypeCode, boolean isUseForDown) {
        // 参数验证
        if (deviceTypeCode < 0 || deviceTypeCode > 2) {
            throw new IllegalArgumentException("设备类型参数无效");
        }

        if (transferTypeCode < 0 || transferTypeCode > 2) {
            throw new IllegalArgumentException("传输类型参数无效");
        }

        // 将整型映射为枚举类型
        PipelineDeviceType deviceType = PipelineDeviceType.values()[deviceTypeCode];
        TransmissionControlType transferType = TransmissionControlType.values()[transferTypeCode];

        mopVM.connect(channelId, deviceType, transferType, isUseForDown);
    }

    public void sendDataToPSDK(String message) {
        byte[] data = message.getBytes(); // 将字符串转为字节数组
        mopVM.sendData(data);
    }

    public void disconnectFromPSDK() {
        mopVM.stopMop();
    }
}
