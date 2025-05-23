#include "dji_bridge.h"

// Java类和方法签名
const char kJniQtActivityClass[] = "com/example/myqtapp/QtActivity";
const char kJniDataReceived[] = "([BI)V";

DJIBridge::DJIBridge(QObject *parent) : QObject(parent)
{
    // 注册JNI方法
    JNINativeMethod methods[] =
    {
        {"nativeDataReceived", "([BI)V", reinterpret_cast<void*>(nativeDataReceived)}
    };

    QAndroidJniEnvironment env;
    jclass clazz = env->FindClass(kJniQtActivityClass);
    env->RegisterNatives(clazz, methods, sizeof(methods)/sizeof(JNINativeMethod));
    env->DeleteLocalRef(clazz);
}

void DJIBridge::connect(int channelId, int deviceType, int transferType, bool useDown)
{
    QAndroidJniObject::callStaticMethod<void>(
        kJniQtActivityClass,
        "jniConnect",
        "(IIIZ)V",
        channelId, deviceType, transferType, useDown
    );
}

void DJIBridge::send(const QByteArray &data)
{
    QAndroidJniEnvironment env;
    jbyteArray jData = env->NewByteArray(data.size());
    env->SetByteArrayRegion(jData, 0, data.size(),
        reinterpret_cast<const jbyte*>(data.constData()));

    QAndroidJniObject::callStaticMethod<void>(
        kJniQtActivityClass,
        "jniSend",
        "([B)V",
        jData
    );

    env->DeleteLocalRef(jData);
}

void DJIBridge::disconnect()
{
    QAndroidJniObject::callStaticMethod<void>(
        kJniQtActivityClass,
        "jniDisconnect",
        "()V"
    );
}

// JNI回调实现
void JNICALL DJIBridge::nativeDataReceived(JNIEnv *env, jobject, jbyteArray data, jint length)
{
    jbyte *bytes = env->GetByteArrayElements(data, nullptr);
    QByteArray arr(reinterpret_cast<char*>(bytes), length);
    emit instance()->dataReceived(arr);
    env->ReleaseByteArrayElements(data, bytes, JNI_ABORT);
}

// 单例访问
DJIBridge* DJIBridge::instance()
{
    static DJIBridge instance;
    return &instance;
}
