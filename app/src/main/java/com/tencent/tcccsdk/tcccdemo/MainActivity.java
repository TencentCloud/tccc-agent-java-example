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
                showTipsMessage("Call Failed，"+desc);
            }
        });
    }

    private AlertDialog onCallingDialog;
    private void showOnCallingDialog(String fromUserId) {
        onCallingDialog=new AlertDialog.Builder(this)
                .setTitle(R.string.IncomingCall)
                .setCancelable(false)
                .setMessage(String.format(getResources().getString(R.string.IncomingCallFromXX),fromUserId))
                .setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        end();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.answer, new DialogInterface.OnClickListener() {
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
        writeCallFunctionLog("tccc.answer");
        tcccSDK.answer( new TXCallback() {
            @Override
            public void onSuccess() {
                writeAsynCallBackLog("answer success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("answer error , errorCode="+code+",desc="+desc);
                showTipsMessage("answer failed，"+desc);
            }
        });
    }
    private void logout(){
        writeCallFunctionLog("tccc.logout");
        tcccSDK.logout(new TXCallback() {
            @Override
            public void onSuccess() {
                writeAsynCallBackLog("logout success");
            }

            @Override
            public void onError(int code, String desc) {
                writeAsynCallBackLog("logout error , errorCode="+code+",desc="+desc);
                showTipsMessage("logout failed，"+desc);
            }
        });
    }

    private boolean isSpeakerphone =true;
    private void changeAudioRoute(){
        writeCallFunctionLog("tccc.setAudioRoute,"+(isSpeakerphone?"Speaker":"Earpiece"));
        tcccSDK.getDeviceManager().setAudioRoute(isSpeakerphone? TCCCDeviceManager.TCCCAudioRoute.TCCCAudioRouteSpeakerphone:TCCCDeviceManager.TCCCAudioRoute.TCCCAudioRouteEarpiece);
        isSpeakerphone=!isSpeakerphone;
    }
    private void changeMute(boolean isMute){
        if(isMute) {
            writeCallFunctionLog("tccc.mute");
            tcccSDK.mute();
        }
        else {
            writeCallFunctionLog("tccc.unmute");
            tcccSDK.unmute();
        }
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
                showTipsMessage("login failed，"+desc);
            }
        });
    }

    private void end(){
        writeCallFunctionLog("tccc.terminate");
        tcccSDK.terminate();
    }

    private void initViewListener() {
        binding.txtUserId.setText(GenerateTestUserToken.USERID);
        writeCallFunctionLog("genTestUserSig");
        GenerateTestUserToken.genTestUserSig(GenerateTestUserToken.SECRETID, GenerateTestUserToken.SECRETKEY,
                GenerateTestUserToken.SDKAPPID, GenerateTestUserToken.USERID, new GenerateTestUserToken.UserTokenCallBack() {
                    @Override
                    public void onSuccess(String value) {
                        binding.txtToken.setText(value);
                        writeAsynCallBackLog("genTestUserSig success, token="+value);
                    }

                    @Override
                    public void onError(int code, String desc) {
                        writeAsynCallBackLog("genTestUserSig error, code="+code+" , desc="+desc);
                        showTipsMessage("Get Token Failed，"+desc);
                    }
                });
        binding.btnGenTestToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCallFunctionLog("genTestUserSig");
                GenerateTestUserToken.genTestUserSig(GenerateTestUserToken.SECRETID, GenerateTestUserToken.SECRETKEY,
                        GenerateTestUserToken.SDKAPPID, GenerateTestUserToken.USERID, new GenerateTestUserToken.UserTokenCallBack() {
                            @Override
                            public void onSuccess(String value) {
                                writeAsynCallBackLog("genTestUserSig success, token="+value);
                                binding.txtToken.setText(value);
                                showTipsMessage("Get Token Success，please click the Login button");
                            }

                            @Override
                            public void onError(int code, String desc) {
                                writeAsynCallBackLog("genTestUserSig error, code="+code+" , desc="+desc);
                                showTipsMessage("Get Token Failed，"+desc);
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
                writeCallFunctionLog("clear logs");
            }
        });
        binding.btnShowT3CLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowLogView =  !isShowLogView;
                if(isShowLogView){
                    binding.tvTcccLog.setVisibility(View.VISIBLE);
                    String log ="TCCC Logs:"+"\r\n";
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
        binding.btnCheckLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCallFunctionLog("tcccSDK.checkLogin");
                tcccSDK.checkLogin(new TXCallback() {
                    @Override
                    public void onSuccess() {
                        writeAsynCallBackLog("checkLogin onSuccess");
                    }

                    @Override
                    public void onError(int i, String s) {
                        writeAsynCallBackLog("checkLogin onError,errorCode="+i+" ,errorMessage="+s);
                    }
                });
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
                writeCallFunctionLog("tccc.getAudioPlayoutVolume");
                int volume = tcccSDK.getDeviceManager().getAudioPlayoutVolume();
                writeCallBackLog("getAudioPlayoutVolume volume="+volume);
            }
        });
        binding.btnSetPlayoutVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCallFunctionLog("tccc.setAudioPlayoutVolume(70)");
                tcccSDK.getDeviceManager().setAudioPlayoutVolume(70);
                writeCallBackLog("setAudioPlayoutVolume volume=70");
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
        String log ="TCCC Logs:"+"\r\n";
        for (int i=0;i<mLogList.size();i++){
            log+=mLogList.get(i)+"\r\n";
        }
        binding.tvTcccLog.setText(log);
        binding.tvTcccLog.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

}