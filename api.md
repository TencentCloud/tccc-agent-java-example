
## API 概览
### 创建实例和事件回调
| API | 描述 |
|-----|-----|
| [sharedInstance](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#aa655301f5a9244169aabeed734fcd77f) | 创建 TCCCWorkstation 实例（单例模式） |
| [destroySharedInstance](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#ae8202b52e97a32875798195d8b07b73d) | 销毁 TCCCWorkstation 实例（单例模式）  |
| [setListener](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a59a51a936cb6cdbae96ba04ba06fb218) | 设置 TCCCWorkstation 事件回调 |

#### 创建实例和设置事件回调示例代码
```java
// 创建实例和设置事件回调
TCCCWorkstation tcccSDK = TCCCWorkstation.sharedInstance(getApplicationContext());
tcccSDK.setListener(new TCCCListener() {});
```

### 登录相关接口函数
| API | 描述 |
|-----|-----|
| [login](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a83f875ae3f2c68a395ed0d3b53c0590d) | SDK 登录 |
| [checkLogin](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#af16b64c6aeb9f8d1ed4309a35526a5e7) | 检查 SDK 是否已登录 |
| [logout](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a8d55d40e155d93503d119d6e1f1af613) | SDK 退出登录 |

#### 登录示例代码
```java
TCCCTypeDef.TCCCLoginParams loginParams = new TCCCTypeDef.TCCCLoginParams();
// 格式 <scheme> : <user> @<host>。 如 sip:3013@1400xxxx.tccc.qcloud.com，其中3013为分机号，1400xxxx为你的tccc应用ID
loginParams.userId = "sip:3013@1400xxxx.tccc.qcloud.com";
loginParams.password = "xxxx";
tcccSDK.login(loginParams, new TXCallback() {
    @Override
    public void onSuccess() {
      // login success
    }

    @Override
    public void onError(int code, String desc) {
      // login error
    }
});
```

### 呼叫相关接口函数
| API | 描述 |
|-----|-----|
| [call](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#aaff86e265bd5a9b9a5f47c8a9da66aaa) | 发起通话 |
| [answer](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a0af46405c2158441776812c9954e05af) | 接听来电 |
| [terminate ](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a7b85cee7a6071154c0a901d1477a6173) | 结束通话 |
| [sendDTMF](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a9f59f5fb1fa18ec5e952b32cd22e8654) | 发送 DTMF（双音多频信号）|
| [mute](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a784e96d19003cdec2157ccaf23350948) | 静音 |
| [unmute](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a42caf46a4aa4372bb6c198e772994e36) | 取消静音 |
#### 发起呼叫和结束呼叫示例代码
```java
TCCCTypeDef.TCCCStartCallParams callParams =new TCCCTypeDef.TCCCStartCallParams();
//格式 <scheme> : <user> @<host>，如 sip:1343xxxx@1400xxxx.tccc.qcloud.com，其中1343xxxx为手机号，1400xxxx为你的tccc应用ID
callParams.to = "sip:1343xxxx@1400xxxx.tccc.qcloud.com";
// 发起通话
tcccSDK.call(callParams, new TXCallback() {
    @Override
    public void onSuccess() {
        // call success
    }

    @Override
    public void onError(int code, String desc) {
      // call error
    }
});
// 结束通话
tcccSDK.terminate();
```


### 音频设备接口函数
| API | 描述 |
|-----|-----|
| [setAudioCaptureVolume](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/interfacecom_1_1tencent_1_1tccc_1_1_t_c_c_c_device_manager.html#ac5817acb0bf96786916fa449dbac927e) | 设定本地音频的采集音量 |
| [getAudioCaptureVolume](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/interfacecom_1_1tencent_1_1tccc_1_1_t_c_c_c_device_manager.html#a32f30d9876558618541db95c3ca5c4ee) | 获取本地音频的采集音量 |
| [setAudioPlayoutVolume](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/interfacecom_1_1tencent_1_1tccc_1_1_t_c_c_c_device_manager.html#a9ce503b8be54269a85fccccab12ee05f) | 设定远端音频的播放音量 |
| [getAudioPlayoutVolume](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/interfacecom_1_1tencent_1_1tccc_1_1_t_c_c_c_device_manager.html#a7f8bae6a7847d440f5ec2d513887323f) | 获取远端音频的播放音量 |
| [setAudioRoute](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/interfacecom_1_1tencent_1_1tccc_1_1_t_c_c_c_device_manager.html#a3fc968a31ed5130fb960d0b594b7ac9b) | 设置音频路由 |


### 调试相关接口
| API | 描述 |
|-----|-----|
| [getSDKVersion](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#ad402328eeb69886302f6f3e4febcbd6d) | 获取 SDK 版本信息 |
| [setLogLevel](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#adfeba8249d1d23dffa23bda6d479059c) | 设置 Log 输出级别 |
| [setConsoleEnabled](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a8459314326f67e074ac03979840a726a) | 启用/禁用控制台日志打印 |
| [callExperimentalAPI](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_workstation.html#a9764afd5bd18709166a013f2895d9f7f) | 调用实验性接口 |

#### 获取SDK版本示例代码
```java
// 获取SDK 版本号
TCCCWorkstation.getSDKVersion();
```


### 错误和警告事件
| API | 描述 |
|-----|-----|
| [onError](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#ae7d53c3064920b7895196614390a3f01) | 错误事件回调 |
| [onWarning](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#aa7463a2d2d9e40272bd50cc0f4b42d69) | 警告事件回调 |
#### 处理错误回调事件回调示例代码
```java
tcccSDK.setListener(new TCCCListener() {
    /**
        * 错误事件回调
        * 错误事件，表示 SDK 抛出的不可恢复的错误，比如进入房间失败或设备开启失败等。
        * @param errCode   错误码
        * @param errMsg    错误信息
        * @param extraInfo 扩展信息字段，个别错误码可能会带额外的信息帮助定位问题
        */
    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        super.onError(errCode, errMsg, extraInfo);
    }

    /**
        * 警告事件回调
        * 警告事件，表示 SDK 抛出的提示性问题，比如音频出现卡顿或 CPU 使用率太高等。
        * @param warningCode 警告码
        * @param warningMsg  警告信息
        * @param extraInfo   扩展信息字段，个别警告码可能会带额外的信息帮助定位问题
        */
    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
        super.onWarning(warningCode, warningMsg, extraInfo);
    }
});
```

### 呼叫相关事件回调
| API | 描述 |
|-----|-----|
| [onNewSession](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#ae6292cca28f1e9609a63c4692be883ab) | 新会话事件。包括呼入和呼出 |
| [onEnded](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#a201098832778d3286a7b5deaecce1d8c) | 会话结束事件 |
| [onAudioVolume](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#a98beb712b688ae2a3a96cf9f572485c5) | 音量大小的反馈回调 |
| [onNetworkQuality ](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#ae3e69dfdd311a257fb569331b7ca1a46) | 网络质量的实时统计回调 |
#### 处理接听和坐席挂断事件回调示例代码
```java
tcccSDK.setListener(new TCCCListener() {
    @Override
    public void onNewSession(TCCCTypeDef.ITCCCSessionInfo info) {
      super.onNewSession(info);
      // 新会话事件。包括呼入和呼出，可通过 info.sessionDirection 判断是呼入还是呼出
    }

    @Override
    public void onEnded(int reason, String reasonMessage, String sessionId) {
      super.onEnded(reason, reasonMessage, sessionId);
      // 会话结束
    }

    @Override
    public void onAccepted(String sessionId) {
      super.onAccepted(sessionId);
      // 对端接听
    }
});
```



### 与云端连接情况的事件回调
| API | 描述 |
|-----|-----|
| [onConnectionLost](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#ad3889066d4f673c62555d136562b71bb) | SDK 与云端的连接已经断开 |
| [onTryToReconnect](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#a83e29a2d9655a65c700a389fafdb1cc5) | SDK 正在尝试重新连接到云端 |
| [onConnectionRecovery](https://tccc.qcloud.com/assets/doc/Agent/JavaAPI/classcom_1_1tencent_1_1tccc_1_1_t_c_c_c_listener.html#ab012055c19c713d50d34210956ebd12e) | SDK 与云端的连接已经恢复 |

#### 与云端连接情况的事件回调示例代码

```java
tcccSDK.setListener(new TCCCListener() {
    /**
        * SDK 与云端的连接已经断开
        * SDK 会在跟云端的连接断开时抛出此事件回调，导致断开的原因大多是网络不可用或者网络切换所致，
        * 比如用户在通话中走进电梯时就可能会遇到此事件。 在抛出此事件之后，SDK 会努力跟云端重新建立连接，
        * 重连过程中会抛出 onTryToReconnect，连接恢复后会抛出 onConnectionRecovery 。
        * 所以，SDK 会在如下三个连接相关的事件中按如下规律切换：
        */
    @Override
    public void onConnectionLost(TCCCServerType serverType) {
        super.onConnectionLost(serverType);
    }

    /**
        * SDK 正在尝试重新连接到云端
        * SDK 会在跟云端的连接断开时抛出 onConnectionLost，之后会努力跟云端重新建立连接并抛出本事件，
        * 连接恢复后会抛出 onConnectionRecovery。
        */
    @Override
    public void onTryToReconnect(TCCCServerType serverType) {
        super.onTryToReconnect(serverType);
    }

    /**
        * SDK 与云端的连接已经恢复
        * SDK 会在跟云端的连接断开时抛出 onConnectionLost，之后会努力跟云端重新建立连接并抛出onTryToReconnect，
        * 连接恢复后会抛出本事件回调。
        */
    @Override
    public void onConnectionRecovery(TCCCServerType serverType) {
        super.onConnectionRecovery(serverType);
    }
});

```



## API 错误码
### 基础错误码

| 符号 | 值 | 含义 |
|---|---|---|
|ERR_SIP_SUCCESS|200|成功|
|ERR_UNRIGIST_FAILURE|20001|登录失败|
|ERR_ANSWER_FAILURE|20002|接听失败，通常是trtc进房失败|
|ERR_SIPURI_WRONGFORMAT|20003|URI 格式错误。|


### SIP相关错误码

| 符号 | 值 | 含义 |
|---|---|---|
|ERR_SIP_BAD_REQUEST|400|错误请求|
|ERR_SIP_UNAUTHORIZED|401|未授权（用户名密码不对情况）|
|ERR_SIP_AUTHENTICATION_REQUIRED|407|代理需要认证，请检查是否已经调用登录接口|
|ERR_SIP_REQUESTTIMEOUT|408|请求超时（网络超时）|
|ERR_SIP_REQUEST_TERMINATED|487|请求终止（网络异常，网络中断场景下）|
|ERR_SIP_SERVICE_UNAVAILABLE|503|服务不可用|
|ERR_SIP_SERVER_TIMEOUT|504|服务超时|


### 音频设备相关错误码
| 符号 | 值 | 含义 |
|---|---|---|
|ERR_MIC_START_FAIL|-1302|打开麦克风失败。设备，麦克风的配置程序（驱动程序）异常，禁用后重新启用设备，或者重启机器，或者更新配置程序|
|ERR_MIC_NOT_AUTHORIZED|-1317|麦克风设备未授权，通常在移动设备出现，可能是权限被用户拒绝了|
|ERR_MIC_SET_PARAM_FAIL|-1318|麦克风设置参数失败|
|ERR_MIC_OCCUPY|-1319|麦克风正在被占用中，例如移动设备正在通话时，打开麦克风会失败|
|ERR_MIC_STOP_FAIL|-1320|停止麦克风失败|
|ERR_SPEAKER_START_FAIL|-1321|打开扬声器失败，例如在 Windows 或 Mac|
|ERR_SPEAKER_SET_PARAM_FAIL|-1322|扬声器设置参数失败|
|ERR_SPEAKER_STOP_FAIL|-1323|停止扬声器失败|
|ERR_UNSUPPORTED_SAMPLERATE|-1306|不支持的音频采样率|

### 网络相关错误码
| 符号 | 值 | 含义 |
|---|---|---|
|ERR_RTC_ENTER_ROOM_FAILED|-3301|进入房间失败，请查看 onError 中的 -3301 对应的 msg 提示确认失败原因|
|ERR_RTC_REQUEST_IP_TIMEOUT|-3307|请求 IP 和 sig 超时，请检查网络是否正常，或网络防火墙是否放行 UDP。|
|ERR_RTC_CONNECT_SERVER_TIMEOUT|-3308|请求进房超时，请检查是否断网或者是否开启vpn，您也可以切换4G进行测试确认|
|ERR_RTC_ENTER_ROOM_REFUSED|-3340|进房请求被拒绝，请检查是否连续调用 enterRoom 进入相同 Id 的房间|