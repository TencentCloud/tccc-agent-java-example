# 快速跑通Sip话机Android Demo
腾讯云呼叫中心提供了Sip话机管理，可以让座席通过固话话机进行通话。也可以通过我们提供的SDK来实现在手机端、PC端外呼、呼入来电接听等场景。
本文主要介绍如何快速跑通腾讯云呼叫中心Sip话机 Android Demo，只要按照如下步骤进行配置，就可以跑通基于腾讯云Sip话机相关功能。

## 开发环境要求
- Android Studio 3.5+。
- Android 4.1（SDK API 16）及以上系统。

## 前提条件
- 您已 [注册腾讯云](https://cloud.tencent.com/document/product/378/17985) 账号，并完成 [实名认证](https://cloud.tencent.com/document/product/378/3629) 。
- 您已 [开通云呼叫中心](https://cloud.tencent.com/document/product/679/48028#.E6.AD.A5.E9.AA.A41.EF.BC.9A.E5.87.86.E5.A4.87.E5.B7.A5.E4.BD.9C) 服务，并创建了 [云呼叫中心实例](https://cloud.tencent.com/document/product/679/48028#.E6.AD.A5.E9.AA.A42.EF.BC.9A.E5.88.9B.E5.BB.BA.E4.BA.91.E5.91.BC.E5.8F.AB.E4.B8.AD.E5.BF.83.E5.AE.9E.E4.BE.8B) 。
- 您已购买了号码，[查看购买指南](https://cloud.tencent.com/document/product/679/73526)。并且完成了对应的[IVR配置](https://cloud.tencent.com/document/product/679/73549)

## 操作步骤
[](id:step1)
### 步骤1：注册话机
1. 登录 [呼叫中心管理端](https://tccc.qcloud.com/login)，选择**登录的呼叫中心** > **管理端** > **电话客服** > **话机管理** > **注册话机** 。
![](https://qcloudimg.tencent-cloud.cn/raw/5bc6e796856c953bcbc91f93936f6054.png)

2. 填写话机信息并且记录话机信息
![](https://qcloudimg.tencent-cloud.cn/raw/b1517a0340f2b6c40c0a8f71fa53859e.png)

### 步骤2：下载 tccc-agent-java-example 源码
  根据实际业务需求 [tccc-agent-java-example](https://github.com/TencentCloud/tccc-agent-java-example) 源码。

[](id:step3)
### 步骤3：配置 tccc-agent-java-example 工程文件
1. 找到并打开 src/main/java/com/tencent/tcccsdk/tcccdemo/debug/DebugSipUserInfo.java 文件。
2. 设置 DebugSipUserInfo.java 文件中的相关参数：
<ul>
  <li/>TestSipLoginUserId：请设置为腾讯云呼叫中心的Sip分机号。
  <li/>TestSipLoginPassword：请设置为腾讯云呼叫中心的Sip分机号的密码。
	<li/>TestCallToUserId：请设置为需要呼叫的手机号。
</ul>


![](https://qcloudimg.tencent-cloud.cn/raw/5688eab87dcc1b357e4bff98168ff46f.png)


### 步骤4：编译运行
使用 Android Studio（3.5及以上的版本）打开源码工程 `tccc-agent-java-example`，单击**运行**即可。
1. 点击登录，
2. 登录成功后输入需要拨打的手机号即可完成拨打功能。


### 运行效果
基本功能如下图所示
| 呼叫效果 | 接听效果 |
|-----|-----|
|![](https://qcloudimg.tencent-cloud.cn/raw/f7745447148dc93969f4c110864579e0.jpeg)|![](https://qcloudimg.tencent-cloud.cn/raw/1a8c1de2c30e1f108d31ad46a45aa78f.jpeg)|


