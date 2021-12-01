package com.story5.plugins.hci.asr;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "HciAsr")
public class HciAsrPlugin extends Plugin {

    private String TAG = "HciAsrPlugin";

    private HciAsr implementation = new HciAsr();

    @Override
    public void load() {
        super.load();
        Log.d(TAG, "load: ");
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");
        Log.d(TAG, "echo: "+value);
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
}
