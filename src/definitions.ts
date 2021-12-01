import type { PermissionState } from '@capacitor/core';

export interface HciAsrPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
  /**
   * @since 0.0.1
   */
  sdkInit(options: InitOptions): Promise<{ value: string }>;
  record(): Promise<void>;
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