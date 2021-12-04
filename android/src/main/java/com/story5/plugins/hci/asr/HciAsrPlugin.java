package com.story5.plugins.hci.asr;

import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import com.sinovoice.sdk.HciSdk;
import com.sinovoice.sdk.android.AudioRecorder;
import com.sinovoice.sdk.android.IAudioRecorderHandler;
import com.sinovoice.sdk.asr.FreetalkResult;
import com.sinovoice.sdk.asr.FreetalkShortAudio;
import com.sinovoice.sdk.asr.IShortAudioCB;
import com.sinovoice.sdk.asr.ShortAudioConfig;
import com.sinovoice.sdk.asr.Warning;

import java.nio.ByteBuffer;

@CapacitorPlugin(
        name = "HciAsr",
        permissions = {
                @Permission(
                        alias = "access_network_state",
                        strings = {Manifest.permission.ACCESS_NETWORK_STATE}
                ),
                @Permission(
                        alias = "internet",
                        strings = {Manifest.permission.INTERNET}
                ),
                @Permission(
                        alias = "microphone",
                        strings = {Manifest.permission.RECORD_AUDIO}
                ),
                @Permission(
                        alias = "storage",
                        strings = {Manifest.permission.WRITE_EXTERNAL_STORAGE}
                )
        }
)
public class HciAsrPlugin extends Plugin implements IAudioRecorderHandler {

    private static final String TAG = "HciAsrPlugin";

    private HciSdk sdk;
    private FreetalkShortAudio freetalkShortAudio;
    private boolean recognizing = false;

    private AudioRecorder audioRecorder;
    private boolean recording = false;
    private boolean hasRecord = false;

    private HciAsr implementation = new HciAsr();
    private PluginCall tempCall;

    @Override
    public void load() {
        super.load();
        Log.d(TAG, "load: ");
        Toast.makeText(getActivity(), "android HciAsrPlugin load", Toast.LENGTH_SHORT).show();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        Toast.makeText(getActivity(), "android echo", Toast.LENGTH_SHORT).show();

        String value = call.getString("value");
        Log.d(TAG, "echo: " + value);
        JSObject ret = new JSObject();
        ret.put("value", value + "android HciAsrPlugin");
        call.resolve(ret);
    }

    @PluginMethod
    public void sdkInit(PluginCall call) {
        Log.d(TAG, "sdkInit: ");
        String appKey = call.getString("appKey");
        String secret = call.getString("secret");
        String sysUrl = call.getString("sysUrl");
        String capUrl = call.getString("capUrl");

        sdk = HciAsrHelper.createSdk(getActivity(), appKey, secret, sysUrl, capUrl);
        freetalkShortAudio = HciAsrHelper.createFreetalkShortAudio(sdk);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    @PluginMethod
    public void createRecorder(PluginCall call) {
        Log.d(TAG, "createRecorder: ");
        int second = call.getInt("second");

        audioRecorder = HciAsrHelper.createAudioRecorder(getActivity(), second);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    @PluginMethod
    public void startRecord(PluginCall call) {
        Log.d(TAG, "startRecord: ");
        if (getPermissionState("microphone") != PermissionState.GRANTED) {
            requestPermissionForAlias("microphone", call, "microphonePermsCallback");
        } else {
            _startRecord(call);
        }
    }

    @PermissionCallback
    private void microphonePermsCallback(PluginCall call) {
        if (getPermissionState("microphone") == PermissionState.GRANTED) {
            _startRecord(call);
        } else {
            call.reject("Permission is required to microphone");
        }
    }

    private void _startRecord(PluginCall call) {
        if (recording) {
            call.reject("A record session is on the progress");
            return;
        }
        if (recognizing) {
            call.reject("A recognize session is on the progress");
            return;
        }
        recording = audioRecorder.start(HciAsrHelper.recorderOptions, this);
        tempCall = call;
    }

    @PluginMethod
    public void stopRecord(PluginCall call) {
        boolean cancel = call.getBoolean("cancel");

        JSObject ret = new JSObject();

        if (!recording) {
            call.reject("No working recorder");
            return;
        }
        audioRecorder.stop(cancel);
        ret.put("success", true);
        if (!cancel) {
            int timeLength = audioRecorder.bufferTimeLen();
            ret.put("result", timeLength);
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void startRecognize(PluginCall call) {
        String language = call.getString("language");
        if (!hasRecord) {
            call.reject("no record file");
            return;
        }

        ByteBuffer audio_data = audioRecorder.readAll();
        ShortAudioConfig config = HciAsrHelper.createShortAudioConfig(language);
        recognizing = true;
        freetalkShortAudio.recognize(config, audio_data, new IShortAudioCB() {
            @Override
            public void run(FreetalkShortAudio freetalkShortAudio, int i, FreetalkResult freetalkResult, Warning[] warnings) {
                recognizing = false;
                if (i != 0) {
                    call.reject("recognize failure", String.valueOf(i));
                } else {
                    JSObject ret = new JSObject();
                    ret.put("success", true);
                    ret.put("result", freetalkResult.toString());
                    call.resolve(ret);
                }
            }
        });
    }

    @PluginMethod
    public  void cancelRecognize(PluginCall call) {
        if (!recognizing) {
            call.reject("no recognize session");
            return;
        }

        freetalkShortAudio.cancel();
        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    /**
     * IAudioRecorderHandler
     */

    @Override
    public void onStart(AudioRecorder recorder) {
        Log.d(TAG, "onStart: ");
        if (tempCall != null) {
            JSObject ret = new JSObject();
            ret.put("success", true);
            tempCall.resolve();
            tempCall = null;
        }
    }

    @Override
    public void onStartFail(AudioRecorder recorder, String message) {
        Log.d(TAG, "onStartFail: " + message);
        if (tempCall != null) {
            JSObject ret = new JSObject();
            ret.put("success", false);
            ret.put("message", message);
            tempCall.resolve();
            tempCall = null;
        }
    }

    @Override
    public void onAudio(AudioRecorder recorder, ByteBuffer audio) {
        // 反馈录音数据，调用频率较高，一般不做处理
    }

    @Override
    public void onBufferFull(AudioRecorder recorder) {
        Log.d(TAG, "onBufferFull: ");
        recorder.stop(false);
    }

    @Override
    public void onError(AudioRecorder recorder, String message) {
        Log.d(TAG, "onError: " + message);
        recorder.stop(true);
    }

    @Override
    public void onSourceEnded(AudioRecorder recorder) {
        Log.d(TAG, "onSourceEnded: ");
        recorder.stop(true);
    }

    @Override
    public void onStop(AudioRecorder audioRecorder, boolean b) {
        Log.d(TAG, "onStop: ");
        recording = false;
        hasRecord = true;
    }
}
