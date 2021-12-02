package com.story5.plugins.hci.asr;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

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
public class HciAsrPlugin extends Plugin {

    private static final String TAG = "HciAsrPlugin";

    private HciAsr implementation = new HciAsr();

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
        call.resolve();
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void record(PluginCall call) {
        Toast.makeText(getActivity(), "record", Toast.LENGTH_SHORT).show();

        if (getPermissionState("microphone") != PermissionState.GRANTED) {
            requestPermissionForAlias("microphone", call, "microphonePermsCallback");
        } else {
            startRecord(call);
        }
    }

    @PermissionCallback
    private void microphonePermsCallback(PluginCall call) {
        if (getPermissionState("microphone") == PermissionState.GRANTED) {
            startRecord(call);
        } else {
            call.reject("Permission is required to microphone");
        }
    }

    private void startRecord(PluginCall call) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        // Start the Activity for result using the name of the callback method
        startActivityForResult(call, intent, "pickImageResult");
    }

    @ActivityCallback
    private void pickImageResult(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }

        // Do something with the result data
    }

    @PluginMethod
    public void methodThatUsesNewAndroidAPI(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO implementation
        } else {
            call.unavailable("Not available on Android API 25 or earlier.");
        }
    }

    @PluginMethod
    public void methodThatRequiresIOS(PluginCall call) {
        call.unimplemented("Not implemented on Android.");
    }
}
