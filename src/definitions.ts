export interface HciAsrPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  /**
   * @since 0.0.1
   */
  sdkInit(options: InitOptions): Promise<{ value: string }>;
}

export interface InitOptions {
  appKey: string;
  secret: string;
  sysUrl: string;
  capUrl: string;
}
