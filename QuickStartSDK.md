# 快速集成腾讯云呼叫中心Sip话机 Android SDK

    本文主要介绍如何快速地将腾讯云呼叫中心Sip话机 SDK(Android) 集成到您的项目中，只要按照如下步骤进行配置，就可以完成 SDK 的集成工作。

## 开发环境要求

- Android Studio 3.5+。
- Android 4.1（SDK API 16）及以上系统。

## 集成 SDK（aar、jar）

### 手动下载（aar、jar）
    目前我们暂时还未发布到 mavenCentral ，您只能手动下载 SDK 集成到工程里：

1. 下载最新版本 [TCCC Agent SDK](https://tccc.qcloud.com/assets/doc/Agent/CppSDKRelease/TCCCSDK_android_aar_last.zip)。
2. 将下载到的 aar 文件拷贝到工程的 **app/libs** 目录下。
3. 在工程根目录下的 build.gradle 中，指定本地仓库路径。
![](https://qcloudimg.tencent-cloud.cn/raw/8c99bd1355929c9420d339fbc1c99d4e.png)
```
implementation fileTree(dir: "libs",includes: ['*.aar','*.jar'])
```
4. 在 app/build.gradle的defaultConfig 中，指定 App 使用的 CPU 架构。
```
defaultConfig {
       ndk {
           abiFilters "armeabi", "armeabi-v7a", "arm64-v8a"
       }
}
```
>?目前 TCCC Agent SDK 支持 armeabi ， armeabi-v7a 和 arm64-v8a。
5. 在 app/src/AndroidManifest.xml 中，指定 App 不允许应用参与备份和恢复基础架构。
![](https://qcloudimg.tencent-cloud.cn/raw/5ddbf9424b6f5157b17a61f368b54f20.png)
6. 单击![](https://main.qcloudimg.com/raw/d6b018054b535424bb23e42d33744d03.png)**Sync Now**，完成 TCCC Agent SDK 的集成工作。


## 配置 App 权限
    在 AndroidManifest.xml 中配置 App 的权限，TCCC Agent SDK 需要以下权限：

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```


## 设置混淆规则

在 proguard-rules.pro 文件，将 TCCC SDK 相关类加入不混淆名单：

```
-keep class com.tencent.** { *; }
```

## 代码实现

具体编码实现可参考 [API 概览以及示例](api.md)

## 常见问题

###  如何查看 TCCC 日志？

TCCC 的日志默认压缩加密，后缀为 .log。
- Android：
	- 日志路径：`/sdcard/Android/data/包名/files/tccc`

### TCCC Agent Android 端能不能支持模拟器？

TCCC 目前版本暂时不支持，未来会支持模拟器。


### Android锁屏、切后台后通话被静音了

注意 安卓9.0系统对App退后台的麦克风做了限制，为防止通话的时候程序退后台引起的通话被静音问题，请在App退后台情况下发送前台通知来防止通话被静音。

### 发起呼叫报 408 或者 503 错误，如何处理？

这种情况一般出现在应用程序切后台重新唤醒后，网络状态还未完全恢复。我们强烈建议您在发起呼叫或者是程序切回前台的时候调用接口判断是否是已登录。

```java
// 检查登录状态
tcccSDK.checkLogin(new TXCallback() {
    @Override
    public void onSuccess() {
        // 已登录成功
    }

    @Override
    public void onError(int code, String message) {
        // 登录异常，提醒用户，并且重新登陆。
        if (code == 408 || code==503) {
            // 网络还未恢复，重置网络。
            tcccSDK.resetSip(false);
        }
    }
});
```

### 其他平台如IOS、Windows有没有对应的SDK？

TCCC 提供了全平台SDK，如有需要可联系我们，我们线下提供。