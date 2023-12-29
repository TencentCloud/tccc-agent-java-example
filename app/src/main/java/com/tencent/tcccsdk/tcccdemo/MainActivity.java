package com.tencent.tcccsdk.tcccdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.tccc.TCCCDeviceManager;
import com.tencent.tccc.TCCCListener;
import com.tencent.tccc.TCCCTypeDef;
import com.tencent.tccc.TCCCWorkstation;
import com.tencent.tccc.TXCallback;
import com.tencent.tccc.TXValueCallback;
import com.tencent.tcccsdk.tcccdemo.base.TCCCBaseActivity;
import com.tencent.tcccsdk.tcccdemo.databinding.ActivityMainBinding;
import com.tencent.tcccsdk.debug.GenerateTestUserToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TCCCBaseActivity {

    private ActivityMainBinding binding;
    private boolean isShowLogView = false;
    private TCCCWorkstation tcccSDK;
    private List<String> mLogList = new ArrayList<>();
    private static final String TAG = "TCCC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewListener();
        initTCCC();
        if (checkPermission()) {
            //
        }
    }

    @Override
    protected void onPermissionGranted() {
        initTCCC();
    }

    private void initTCCC() {
        tcccSDK = TCCCWorkstation.sharedInstance(this.getApplicationContext());
        tcccSDK.setListener(new TCCCListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle extraInfo) {
                super.onError(errCode, errMsg, extraInfo);
                writeCallBackLog("onError errCode="+errCode+" ,errMsg="+errMsg);
            }

            @Override
            public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
                super.onWarning(warningCode, warningMsg, extraInfo);
                writeCallBackLog("onWarning warningCode="+warningCode+" ,warningMsg="+warningMsg);
            }

            @Override
            public void onNewSession(TCCCTypeDef.ITCCCSessionInfo info) {
                super.onNewSession(info);
                if(info.sessionDirection == TCCCTypeDef.TCCCSessionDirection.CallIn){
                    showOnCallingDialog(info.fromUserId);
                }
                writeCallBackLog("onNewSession sessionId="+info.sessionId+" ,fromUserId="+info.fromUserId+" ,sessionDirection="+info.sessionDirection);
            }

            @Override
            public void onAccepted(String sessionId) {
                super.onAccepted(sessionId);
                writeCallBackLog("onAccepted sessionId="+sessionId);
            }
            @Override
            public void onEnded(TCCCListener.EndedReason reason, String reasonMessage, String sessionId) {
                super.onEnded(reason, reasonMessage, sessionId);
                closeOnCallingDialog();
                writeCallBackLog("onEnded sessionId="+sessionId+" ,reason="+reason.toString()+" ,reasonMessage="+reasonMessage);
            }

            @Override
            public void onAudioVolume(TCCCTypeDef.TCCCVolumeInfo volumeInfo) {
                super.onAudioVolume(volumeInfo);
                Log.i(TAG,"onAudioVolume userId="+volumeInfo.userId+" ,volume ="+volumeInfo.volume);
            }

            @Override
            public void onNetworkQuality(TCCCTypeDef.TCCCQualityInfo localQuality, TCCCTypeDef.TCCCQualityInfo remoteQualitys) {
                super.onNetworkQuality(localQuality, remoteQualitys);
                Log.i(TAG,"onNetworkQuality localQuality="+localQuality.quality+" ,localUserId"+localQuality.userId);
            }

            @Override
            public void onConnectionLost(TCCCListener.TCCCServerType serverType) {
                super.onConnectionLost(serverType);
                writeCallBackLog("onConnectionLost");
            }

            @Override
            public void onTryToReconnect(TCCCListener.TCCCServerType serverType) {
                super.onTryToReconnect(serverType);
                writeCallBackLog("onTryToReconnect");
            }

            @Override
            public void onConnectionRecovery(TCCCListener.TCCCServerType serverType) {
                super.onConnectionRecovery(serverType);
                writeCallBackLog("onConnectionRecovery");
            }
        });
        String version = TCCCWorkstation.getSDKVersion();
        writeCallFunctionLog("TCCC SDK Version:"+version);
    }

    private void startCall(){
        String to =binding.txtTo.getText().toString();
        writeCallFunctionLog("tccc.call, to = "+ to);
        TCCCTypeDef.TCCCStartCallParams params =new TCCCTypeDef.TCCCStartCallParams();
        params.to = to;
        params.remark = "fromAndroid";
        tcccSDK.call(params, new TXCallback() {
            @Override
            public void onSuccess() {
                writeAsynCallBackLog("call success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("call error , errorCode="+code+",desc="+desc);
                showTipsMessage("呼叫失败，"+desc);
            }
        });
    }

    private AlertDialog onCallingDialog;
    private void showOnCallingDialog(String fromUserId) {
        onCallingDialog=new AlertDialog.Builder(this)
                .setTitle("您有新的来电")
                .setCancelable(false)
                .setMessage(fromUserId+" 来电，请您接听")
                .setNegativeButton("拒接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        end();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("接听", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        answer();
                        dialog.dismiss();
                    }
                }).create();
        onCallingDialog.show();
    }

    private void closeOnCallingDialog(){
        if(onCallingDialog == null)
            return;
        onCallingDialog.dismiss();
    }

    private void answer(){
        tcccSDK.answer( new TXCallback() {
            @Override
            public void onSuccess() {
                writeAsynCallBackLog("answer success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("answer error , errorCode="+code+",desc="+desc);
                showTipsMessage("接听失败，"+desc);
            }
        });
    }
    private void logout(){
        tcccSDK.logout(new TXCallback() {
            @Override
            public void onSuccess() {
                writeAsynCallBackLog("logout success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("logout error , errorCode="+code+",desc="+desc);
                showTipsMessage("退出登录失败，"+desc);
            }
        });
    }

    private boolean isSpeakerphone =true;
    private void changeAudioRoute(){
        tcccSDK.getDeviceManager().setAudioRoute(isSpeakerphone? TCCCDeviceManager.TCCCAudioRoute.TCCCAudioRouteSpeakerphone:TCCCDeviceManager.TCCCAudioRoute.TCCCAudioRouteEarpiece);
        isSpeakerphone=!isSpeakerphone;
    }
    private void changeMute(boolean isMute){
        if(isMute)
            tcccSDK.mute();
        else
            tcccSDK.unmute();
    }
    private void login(){
        TCCCTypeDef.TCCCLoginParams params = new TCCCTypeDef.TCCCLoginParams();
        params.userId=binding.txtUserId.getText().toString();
        params.token =binding.txtToken.getText().toString();
        params.type = TCCCTypeDef.TCCCLoginType.Agent;
        params.sdkAppId = GenerateTestUserToken.SDKAPPID;
        writeCallFunctionLog("tcccSDK.login userId="+params.userId);
        tcccSDK.login(params, new TXValueCallback<TCCCTypeDef.TCCCLoginInfo>() {
            @Override
            public void onSuccess(TCCCTypeDef.TCCCLoginInfo tcccLoginInfo) {
                writeAsynCallBackLog(tcccLoginInfo.userId+",login success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("login error, code="+code+" , desc="+desc);
                showTipsMessage("登录失败，"+desc);
            }
        });
    }

    private void end(){
        writeCallFunctionLog("tccc.terminate");
        tcccSDK.terminate();
    }

    private void initViewListener() {
        binding.txtUserId.setText(GenerateTestUserToken.USERID);
        GenerateTestUserToken.genTestUserSig(GenerateTestUserToken.SECRETID, GenerateTestUserToken.SECRETKEY,
                GenerateTestUserToken.SDKAPPID, GenerateTestUserToken.USERID, new GenerateTestUserToken.UserTokenCallBack() {
                    @Override
                    public void onSuccess(String value) {
                        binding.txtToken.setText(value);
                    }

                    @Override
                    public void onError(int code, String desc) {
                        writeAsynCallBackLog("genTestUserSig error, code="+code+" , desc="+desc);
                        showTipsMessage("获取Token失败，"+desc);
                    }
                });
        binding.btnGenTestToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GenerateTestUserToken.genTestUserSig(GenerateTestUserToken.SECRETID, GenerateTestUserToken.SECRETKEY,
                        GenerateTestUserToken.SDKAPPID, GenerateTestUserToken.USERID, new GenerateTestUserToken.UserTokenCallBack() {
                            @Override
                            public void onSuccess(String value) {
                                writeAsynCallBackLog("genTestUserSig success, token="+value);
                                binding.txtToken.setText(value);
                                showTipsMessage("获取token成功，请点击登录");
                            }

                            @Override
                            public void onError(int code, String desc) {
                                writeAsynCallBackLog("genTestUserSig error, code="+code+" , desc="+desc);
                                showTipsMessage("获取Token失败，"+desc);
                            }
                        });
            }
        });
        binding.txtTo.setText(GenerateTestUserToken.TO);
        binding.btnClearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogList.clear();
                refreshTcccLogView();
            }
        });
        binding.btnShowT3CLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowLogView =  !isShowLogView;
                if(isShowLogView){
                    binding.tvTcccLog.setVisibility(View.VISIBLE);
                    String log ="TCCC日志窗口"+"\r\n";
                    for (int i=0;i<mLogList.size();i++){
                        log+=mLogList.get(i)+"\r\n";
                    }
                    binding.tvTcccLog.setText(log);
                    binding.tvTcccLog.setMovementMethod(ScrollingMovementMethod.getInstance());
                }else{
                    binding.tvTcccLog.setVisibility(View.GONE);
                }
            }
        });
        binding.bntCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCall();
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        binding.btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        binding.btnAudioRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAudioRoute();
            }
        });
        binding.btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMute(true);
            }
        });
        binding.btnUnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMute(false);
            }
        });
        binding.btnGetPlayoutVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int volume = tcccSDK.getDeviceManager().getAudioPlayoutVolume();
                writeCallFunctionLog("getAudioPlayoutVolume volume="+volume);
            }
        });
        binding.btnSetPlayoutVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcccSDK.getDeviceManager().setAudioPlayoutVolume(70);
                writeCallFunctionLog("setAudioPlayoutVolume volume=70");
            }
        });
    }

    private void writeCallFunctionLog(String msg){
        msg = "==> "+msg;
        mLogList.add(msg);
        refreshTcccLogView();
    }

    private void writeAsynCallBackLog(String msg){
        msg ="== "+msg;
        mLogList.add(msg);
        refreshTcccLogView();
    }

    private void showTipsMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void writeCallBackLog(String msg){
        msg ="<== "+msg;
        mLogList.add(msg);
        refreshTcccLogView();
    }

    private void refreshTcccLogView(){
        if(!isShowLogView)
            return;
        binding.tvTcccLog.setVisibility(View.VISIBLE);
        String log ="TCCC日志窗口"+"\r\n";
        for (int i=0;i<mLogList.size();i++){
            log+=mLogList.get(i)+"\r\n";
        }
        binding.tvTcccLog.setText(log);
        binding.tvTcccLog.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

}