import { WebPlugin } from '@capacitor/core';
import type {
  HciAsrPlugin,
  InitOptions,
  PermissionStatus,
  PromiseResult,
  RecognizeLanguage,
} from './definitions';

export class HciAsrWeb extends WebPlugin implements HciAsrPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', JSON.stringify(options));
    return { value: options.value + 'HciAsrWeb plugin' };
  }
  async checkPermissions(): Promise<PermissionStatus> {
    if (typeof navigator === 'undefined' || !navigator.permissions) {
      throw this.unavailable('Permissions API not available in this browser.');
    }

    // const permission = await navigator.permissions.query(... );
    throw this.unimplemented('Not implemented on web.');
    // TODO
  }

  async requestPermissions(): Promise<PermissionStatus> {
    // TODO: does the web support requesting permissions for my plugin?
    throw this.unimplemented('Not implemented on web.');
  }
  async sdkInit(options: InitOptions): Promise<PromiseResult> {
    console.log(options);
    throw this.unimplemented('Not implemented on web.');
  }
  async createRecorder(second: number): Promise<PromiseResult> {
    console.log(second);
    throw this.unimplemented('Not implemented on web.');
  }
  async startRecord(): Promise<PromiseResult> {
    throw this.unimplemented('Not implemented on web.');
  }
  async stopRecord(cancel: boolean): Promise<PromiseResult> {
    console.log(cancel);
    throw this.unimplemented('Not implemented on web.');
  }
  async startRecognize(language: RecognizeLanguage): Promise<PromiseResult> {
    console.log(language);
    throw this.unimplemented('Not implemented on web.');
  }
  async cancelRecognize(): Promise<PromiseResult> {
    throw this.unimplemented('Not implemented on web.');
  }
}
