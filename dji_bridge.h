#ifndef DJI_BRIDGE_H
#define DJI_BRIDGE_H

#include <QObject>
#include <QAndroidJniEnvironment>
#include <QAndroidJniObject>

class DJIBridge : public QObject
{
    Q_OBJECT
public:
    explicit DJIBridge(QObject *parent = nullptr);
    static DJIBridge* instance();

    Q_INVOKABLE void connect(int channelId, int deviceType, int transferType, bool useDown);
    Q_INVOKABLE void send(const QByteArray &data);
    Q_INVOKABLE void disconnect();

signals:
    void dataReceived(const QByteArray &data);

private:
    static void JNICALL nativeDataReceived(JNIEnv *env, jobject thiz, jbyteArray data, jint length);
};
#endif // DJI_BRIDGE_H
