package com.story5.plugins.hci.asr;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "HciAsr")
public class HciAsrPlugin extends Plugin {

    private HciAsr implementation = new HciAsr();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void sdkInit(PluginCall call) {
        String appKey = call.getString("appKey");
        String secret = call.getString("secret");
        String sysUrl = call.getString("sysUrl");
        String capUrl = call.getString("capUrl");
        call.resolve();
    }
}
