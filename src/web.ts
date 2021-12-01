import { WebPlugin } from '@capacitor/core';
import { PermissionStatus } from './definitions';

import type {
  HciAsrPlugin,
  InitOptions,
} from './definitions';

export class HciAsrWeb extends WebPlugin implements HciAsrPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', JSON.stringify(options));
    return { value: options.value + "HciAsrWeb plugin" };
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
  async sdkInit(options: InitOptions): Promise<{ value: string }> {
    console.log('sdkInit', options);
    return { value: "success" };
  }
  async record(): Promise<{ state: string }> {
    throw this.unimplemented('Not implemented on web.');
  }
}
