package com.example.myqtapp;


import java.util.concurrent.ExecutorService;
import dji.v5.common.error.DJIPipeLineError;
import dji.v5.common.error.IDJIError;
import dji.v5.manager.mop.DataResult;
import dji.v5.manager.mop.Pipeline;
import dji.v5.manager.mop.PipelineManager;
import dji.v5.utils.common.DJIExecutor;
import io.reactivex.rxjava3.disposables.Disposable;
import dji.sdk.keyvalue.value.mop.TransmissionControlType;
import dji.sdk.keyvalue.value.mop.PipelineDeviceType;
import androidx.lifecycle.MutableLiveData;
import java.util.Map;
import java.util.Arrays;
import android.os.Handler;
import android.os.Looper;



public class MopVM{
    private boolean isStop = false;
    private final MutableLiveData<Map<Integer, Pipeline>> pipelineMapLiveData = new MutableLiveData<>();
    //用于存放接收到的数据
    private final byte[]data = new byte[19004];
    private final ExecutorService executorService = DJIExecutor.getExecutorFor(DJIExecutor.Purpose.URGENT);
    private Disposable mReadDataDisposable;
    //当前的管道
    private Pipeline pipeline;
    private Param currentConnectParam;

    public void initListener(){
        PipelineManager.getInstance().addPipelineConnectionListener(pipeline ->{
            pipelineMapLiveData.postValue(pipeline);
        });
    }
    public interface DataCallback {
        void onDataReceived(byte[] data, int length);
    }

    private DataCallback dataCallback;

    public void setDataCallback(DataCallback callback) {
       this.dataCallback = callback;
    }

    //建立管道连接(同步获取管道管道列表)
    public void connect(int id, PipelineDeviceType deviceType, TransmissionControlType transmissionControlType, boolean isUseForDown) {
        executorService.execute(() -> {
            IDJIError error = PipelineManager.getInstance().connectPipeline(id, deviceType, transmissionControlType);
            //管道连接成功
            if (error == null) {
                currentConnectParam = new Param(id, transmissionControlType, deviceType);
                isStop = false;
                pipeline = PipelineManager.getInstance().getPipelines().get(id);
                if (!isUseForDown) {
                    readData();
                }
            }
        });
    }

    public void readData(){
        if (pipeline == null){
            return;
        }
        DataResult result = pipeline.readData(data);
        int len = result.getLength();

        if (len > 0 && dataCallback != null) {
            byte[] received = Arrays.copyOf(data, len);
            new Handler(Looper.getMainLooper()).post(() -> {
                dataCallback.onDataReceived(received, len);
            });
        }
        //如果读到的数据长度小于0，说明出现错误，就终止连接
        if (len < 0){
            if (!isStop && !result.getError().errorCode().equals(DJIPipeLineError.TIMEOUT)){
                stopMop();
            }
        }
        if (!isStop){
            readData();
        }
    }

    public void sendData(byte[] byteArray){
        executorService.submit(() ->{
            if (pipeline == null){
                return;
            }
        DataResult result = pipeline.writeData(byteArray);
        });
    }



    private void disconnectMop() {
        executorService.execute(() -> {
            if (pipeline == null || currentConnectParam == null){
                return;
            }
            isStop = true;
            IDJIError error = PipelineManager.getInstance().disconnectPipeline(
                    currentConnectParam.getId(),
                    currentConnectParam.getDeviceType(),
                    currentConnectParam.getTransmissionControlType()
            );
        });
    }

    private void stopReadDataTimer(){
        if (mReadDataDisposable != null){
            mReadDataDisposable.dispose();
            mReadDataDisposable = null;
        }
    }

    public void stopMop(){
        if (!isStop){
            stopReadDataTimer();
            disconnectMop();
        }
    }
    public static class Param {
        private final int id;
        private final TransmissionControlType transmissionControlType;
        private final PipelineDeviceType deviceType;

        public Param(int id, TransmissionControlType transmissionControlType, PipelineDeviceType deviceType) {
            this.id = id;
            this.transmissionControlType = transmissionControlType;
            this.deviceType = deviceType;
        }

        public int getId() {
            return id;
        }

        public TransmissionControlType getTransmissionControlType() {
            return transmissionControlType;
        }

        public PipelineDeviceType getDeviceType() {
            return deviceType;
        }
    }
}
