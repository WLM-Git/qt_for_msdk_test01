QT       += core gui
QT       += androidextras

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# The following define makes your compiler emit warnings if you use
# any Qt feature that has been marked deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

ANDROID_MIN_SDK_VERSION = 24

ANDROID_GRADLE_PLUGIN_VERSION = 7.4.2


SOURCES += \
    dji_bridge.cpp \
    main.cpp \
    widget.cpp

HEADERS += \
    dji_bridge.h \
    widget.h

FORMS += \
    widget.ui

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

DISTFILES += \
    android/AndroidManifest.xml \
    android/build.gradle \
    android/gradle.properties \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradlew \
    android/gradlew.bat \
    android/libs/appcompat-1.6.1.aar \
    android/libs/dji-sdk-v5-aircraft-provided-5.12.0.jar \
    android/libs/jetified-rxandroid-3.0.0.aar \
    android/libs/lifecycle-common-2.5.1.jar \
    android/libs/lifecycle-livedata-core-2.5.1-sources.jar \
    android/res/drawable/ic_launcher_background.xml \
    android/res/values/accessory_filter.xml \
    android/res/values/attrs.xml \
    android/res/values/colors.xml \
    android/res/values/libs.xml \
    android/res/values/styles.xml \
    android/res/values/themes.xml \
    android/res/values/themes/themes.xml \
    android/res/xml/accessory_filter.xml \
    android/src/com/example/myqtapp/MopVM.java \
    android/src/com/example/myqtapp/QtActivity.java \
    android/src/com/example/myqtapp/QtApplication.java

ANDROID_PACKAGE_SOURCE_DIR = $$PWD/android


OTHER_FILES += $$PWD/android/AndroidManifest.xml
