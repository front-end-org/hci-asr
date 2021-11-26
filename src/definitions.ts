export interface HciAsrPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  sdkInit(options: InitOptions): Promise<{ value: string }>;
}

export interface InitOptions {
  appKey: string;
  secret: string;
  sysUrl: string;
  capUrl: string;
}
