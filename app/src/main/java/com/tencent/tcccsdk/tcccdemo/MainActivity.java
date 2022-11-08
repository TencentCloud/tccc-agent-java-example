package com.tencent.tcccsdk.tcccdemo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.tccc.TCCCListener;
import com.tencent.tccc.TCCCTypeDef;
import com.tencent.tccc.TCCCWorkstation;
import com.tencent.tccc.TXCallback;
import com.tencent.tcccsdk.tcccdemo.base.TCCCBaseActivity;
import com.tencent.tcccsdk.tcccdemo.databinding.ActivityMainBinding;

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
                Toast.makeText(MainActivity.this, "onNewSession sessionId="+info.sessionId , Toast.LENGTH_SHORT).show();
                writeCallBackLog("onNewSession sessionId="+info.sessionId+" ,fromUserId="+info.fromUserId+" ,sessionDirection="+info.sessionDirection);
            }

            @Override
            public void onEnded(int reason, String reasonMessage, String sessionId) {
                super.onEnded(reason, reasonMessage, sessionId);
                writeCallBackLog("onEnded sessionId="+sessionId+" ,reason="+reason+" ,reasonMessage="+reasonMessage);
            }

            @Override
            public void onAccepted(String sessionId) {
                super.onAccepted(sessionId);
                writeCallBackLog("onAccepted sessionId="+sessionId);
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
            public void onConnectionLost(int serverType) {
                super.onConnectionLost(serverType);
                writeCallBackLog("onConnectionLost");
            }

            @Override
            public void onTryToReconnect(int serverType) {
                super.onTryToReconnect(serverType);
                writeCallBackLog("onTryToReconnect");
            }

            @Override
            public void onConnectionRecovery(int serverType) {
                super.onConnectionRecovery(serverType);
                writeCallBackLog("onConnectionRecovery");
            }
        });
    }

    private void startCall(){
        String to =binding.txtTo.getText().toString();
        writeFunCallLog("tccc.call, to = "+ to);
        TCCCTypeDef.TCCCStartCallParams params =new TCCCTypeDef.TCCCStartCallParams();
        params.to =to;
        tcccSDK.call(params, new TXCallback() {
            @Override
            public void onSuccess() {
                writeFunCallValueLog("call success");
            }

            @Override
            public void onError(int code, String desc) {
                writeFunCallValueLog("call error , errorCode="+code+",desc="+desc);
            }
        });
    }
    private void answer(){
        tcccSDK.answer("", new TXCallback() {
            @Override
            public void onSuccess() {
                writeFunCallValueLog("answer success");
            }

            @Override
            public void onError(int code, String desc) {
                writeFunCallValueLog("answer error , errorCode="+code+",desc="+desc);
            }
        });
    }
    private void logout(){
        tcccSDK.logout(new TXCallback() {
            @Override
            public void onSuccess() {
                writeFunCallValueLog("logout success");
            }

            @Override
            public void onError(int code, String desc) {
                writeFunCallValueLog("logout error , errorCode="+code+",desc="+desc);
            }
        });
    }
    private boolean isSpeakerphone =true;
    private void changeAudioRoute(){
        tcccSDK.getDeviceManager().setAudioRoute(isSpeakerphone?0:1);
        isSpeakerphone=!isSpeakerphone;
    }
    private void changeMute(boolean isMute){
        if(isMute)
            tcccSDK.mute("");
        else
            tcccSDK.unmute("");
    }
    private void login(){
        TCCCTypeDef.TCCCLoginParams params = new TCCCTypeDef.TCCCLoginParams();
        params.userId=binding.txtUserId.getText().toString();
        params.password =binding.txtPsw.getText().toString();
        writeFunCallLog("tcccSDK.login userId="+params.userId);
        tcccSDK.login(params, new TXCallback() {
            @Override
            public void onSuccess() {
                writeFunCallValueLog("login success");
            }

            @Override
            public void onError(int code, String desc) {
                writeFunCallValueLog("login error, code="+code+" , desc="+desc);
            }
        });
    }

    private void end(){
        writeFunCallLog("tccc.terminate");
        tcccSDK.terminate("");
    }

    private void initViewListener() {
        binding.txtUserId.setText(DebugUserInfo.TestLoginUserId);
        binding.txtPsw.setText(DebugUserInfo.TestLoginPassword);
        binding.txtTo.setText(DebugUserInfo.TestCallToUserId);
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
        binding.btnSslTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcccSDK.callExperimentalAPI("setOutboundProxy","sip:sip-dev.tccc.qcloud.com:5061 tls");
                writeFunCallLog("tcccSDK.callExperimentalAPI setOutboundProxy sip:sip-dev.tccc.qcloud.com:5061 tls");
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
        binding.bntAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer();
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
                writeFunCallLog("getAudioPlayoutVolume volume="+volume);
            }
        });
        binding.btnSetPlayoutVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcccSDK.getDeviceManager().setAudioPlayoutVolume(70);
                writeFunCallLog("setAudioPlayoutVolume volume=70");
            }
        });
    }

    private void writeFunCallLog(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        msg = "==> "+msg;
        mLogList.add(msg);
        refreshTcccLogView();
    }

    private void writeFunCallValueLog(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        msg ="== "+msg;
        mLogList.add(msg);
        refreshTcccLogView();
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