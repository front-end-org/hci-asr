package com.story5.plugins.hci.asr;

import android.Manifest;
import android.os.Build;
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

    private String TAG = "HciAsrPlugin";

    private HciAsr implementation = new HciAsr();

    @Override
    public void load() {
        super.load();
        Log.d(TAG, "load: ");
        Toast.makeText(getActivity(), "android HciAsrPlugin load", Toast.LENGTH_SHORT).show();
    }

    @PluginMethod
    public void echo(PluginCall call) {
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

    @PluginMethod()
    public void record(PluginCall call) {
        if (getPermissionState("microphone") != PermissionState.GRANTED) {
            requestPermissionForAlias("microphone", call, "microphonePermsCallback");
        } else {
            loadCamera(call);
        }
    }

    @PermissionCallback
    private void microphonePermsCallback(PluginCall call) {
        if (getPermissionState("microphone") == PermissionState.GRANTED) {
            loadCamera(call);
        } else {
            call.reject("Permission is required to microphone");
        }
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
