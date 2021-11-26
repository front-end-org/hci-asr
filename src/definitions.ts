export interface HciAsrPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
