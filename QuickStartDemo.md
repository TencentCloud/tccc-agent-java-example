# 快速跑通坐席端Android Demo

本文主要介绍如何快速跑通腾讯云呼叫中心访客端 Android TCCC Demo，只要按照如下步骤进行配置，就可以跑通访客端的 Android Demo。

## 开发环境要求
- Android Studio 3.5+。
- Android 4.1（SDK API 16）及以上系统。

## 前提条件
- 您已 [注册腾讯云](https://cloud.tencent.com/document/product/378/17985) 账号，并完成 [实名认证](https://cloud.tencent.com/document/product/378/3629) 。
- 您已 [开通云呼叫中心](https://cloud.tencent.com/document/product/679/48028#.E6.AD.A5.E9.AA.A41.EF.BC.9A.E5.87.86.E5.A4.87.E5.B7.A5.E4.BD.9C) 服务，并创建了 [云呼叫中心实例](https://cloud.tencent.com/document/product/679/48028#.E6.AD.A5.E9.AA.A42.EF.BC.9A.E5.88.9B.E5.BB.BA.E4.BA.91.E5.91.BC.E5.8F.AB.E4.B8.AD.E5.BF.83.E5.AE.9E.E4.BE.8B) 。

## 操作步骤
[](id:step1)
### 步骤1：SIP 话机注册
- 参考[文章](https://cloud.tencent.com/document/product/679/79223)创建话机注册话机

[](id:step2)
### 步骤2：下载 SDK 和 tccc-agent-java-example 源码
1. 根据实际业务需求 [tccc-agent-java-example](https://github.com/TencentCloud/tccc-agent-java-example) 源码。

[](id:step3)
### 步骤3：配置 tccc-agent-java-example 工程文件
1. 配置sip话机信息,替换自己的sip话机的用户名和密码。
![](https://qcloudimg.tencent-cloud.cn/raw/5a439875f45bb4fbc6a1f9fa4ed4850b.png)


### 步骤4：编译运行
使用 Android Studio（3.5及以上的版本）打开源码工程 `tccc-agent-java-example`，单击**运行**即可。

### 运行效果
基本功能如下图所示
| 呼叫中效果 | 接听效果 |
|-----|-----|



