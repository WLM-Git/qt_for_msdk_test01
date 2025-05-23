package com.example.myqtapp;

import dji.sdk.keyvalue.value.mop.PipelineDeviceType;
import dji.sdk.keyvalue.value.mop.TransmissionControlType;

public class MopWrapper
{
    private MopVM mopVM;

    public MopWrapper()
    {
        mopVM = new MopVM();
    }

    public void connectToPSDK()
    {
        int channelId = 1;
        PipelineDeviceType deviceType = PipelineDeviceType.PAYLOAD;
        TransmissionControlType transferType = TransmissionControlType.STABLE;
        boolean isUseForDown = false;
        mopVM.connect(channelId, deviceType, transferType, isUseForDown);
    }

    public void sendDataToPSDK(String message)
    {
        byte[] data = message.getBytes();
        mopVM.sendData(data);
    }

    public void disconnectFromPSDK()
    {
        mopVM.stopMop();
    }
}
