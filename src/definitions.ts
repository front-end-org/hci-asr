import type { PermissionState } from '@capacitor/core';

export interface HciAsrPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
  /**
   * @since 0.0.1
   */
  sdkInit(options: InitOptions): Promise<PromiseResult>;
  createRecorder(second: number): Promise<PromiseResult>;
  startRecord(): Promise<{
    success: boolean;
  }>;
  stopRecord(cancel: boolean): Promise<PromiseResult>;
  startRecognize(language: RecognizeLanguage): Promise<PromiseResult>;
  cancelRecognize(): Promise<PromiseResult>;
}

export interface PromiseResult {
  success: boolean;
  message?: string;
  result?: any;
}

export enum RecognizeLanguage {
  chinese = 'cn_16k_common',
  shanghai = 'x-shanghai_16k_common',
}

export interface InitOptions {
  appKey: string;
  secret: string;
  sysUrl: string;
  capUrl: string;
}

export interface PermissionStatus {
  access_network_state: PermissionState;
  internet: PermissionState;
  microphone: PermissionState;
  storage: PermissionState;
}
