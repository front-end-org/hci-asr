import { WebPlugin } from '@capacitor/core';

import type {
  HciAsrPlugin,
  InitOptions,
} from './definitions';

export class HciAsrWeb extends WebPlugin implements HciAsrPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  async sdkInit(options: InitOptions): Promise<{ value: string }> {
    console.log('sdkInit', options);
    return { value: "success" };
  }
}
