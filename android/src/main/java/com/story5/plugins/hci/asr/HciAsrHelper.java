package com.story5.plugins.hci.asr;

import android.content.Context;
import android.util.Log;

import com.sinovoice.sdk.HciSdk;
import com.sinovoice.sdk.HciSdkConfig;
import com.sinovoice.sdk.LogLevel;
import com.sinovoice.sdk.android.AudioRecorder;
import com.sinovoice.sdk.android.HciAudioManager;
import com.sinovoice.sdk.asr.CloudAsrConfig;
import com.sinovoice.sdk.asr.FreetalkShortAudio;
import com.sinovoice.sdk.asr.ShortAudioConfig;

import java.io.File;

public class HciAsrHelper {
    static private HciAudioManager am; // 单例模式

    public static ShortAudioConfig createShortAudioConfig(String property) {
        ShortAudioConfig config = new ShortAudioConfig();
        config.setProperty(property);
        config.setAudioFormat("pcm_s16le_16k");
        config.setMode(ShortAudioConfig.SHORT_AUDIO_MODE);
        config.setAddPunc(true); // 是否打标点
        config.setTimeout(10000);
        return config;
    }

    public static FreetalkShortAudio createFreetalkShortAudio(HciSdk sdk) {
        CloudAsrConfig cloudAsrConfig = new CloudAsrConfig();
        FreetalkShortAudio freetalkShortAudio = new FreetalkShortAudio(sdk, cloudAsrConfig);
        return freetalkShortAudio;
    }

    /**
     * 构造录音机
     *
     * @param second - 秒
     *               bufferTime - HciAudioBuffer 缓冲区时长，单位: ms。
     *               若录音的同时使用 audioSource() 实时读取录音数据，bufferTime 一般设为 1000ms；
     *               否则，一般设置为待录制音频的时长。
     */
    public static AudioRecorder createAudioRecorder(Context context, int second) {
        if (am == null) {
            // HciAudioManager 只能创建一个实例
            am = HciAudioManager.builder(context).setSampleRate(16000).create();
        }
        int bufferTime = 1000 * second;
        AudioRecorder audioRecorder = new AudioRecorder(am, "pcm_s16le_16k", bufferTime);
        return audioRecorder;
    }

    public static HciSdk createSdk(Context context, String appKey, String secret, String sysUrl, String capUrl) {
        String dataPath = getDataPath(context);
        HciSdkConfig cfg = createHciSdkConfig(appKey, secret, sysUrl, capUrl, dataPath);

        HciSdk sdk = new HciSdk();
        sdk.setLogLevel(LogLevel.D); // 日志级别
        sdk.init(cfg, context);
        return sdk;
    }

    private static String getDataPath(Context context) {
        String path = context.getFilesDir().toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.e("log-path", path);
        return path;
    }

    /**
     * @param appKey   - 平台为应用分配的 appKey
     * @param secret   - 平台为应用分配的密钥 (敏感信息，请勿公开)
     * @param sysUrl
     * @param capUrl
     * @param dataPath
     * @return
     */
    private static HciSdkConfig createHciSdkConfig(String appKey, String secret, String sysUrl, String capUrl, String dataPath) {
        HciSdkConfig cfg = new HciSdkConfig();
        cfg.setAppkey(appKey);
        cfg.setSecret(secret);
        cfg.setSysUrl(sysUrl);
        cfg.setCapUrl(capUrl);
        cfg.setDataPath(dataPath);
        cfg.setVerifySSL(false);
        Log.w("sdk-config", cfg.toString());
        return cfg;
    }
}
