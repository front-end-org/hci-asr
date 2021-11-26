import { registerPlugin } from '@capacitor/core';

import type { HciAsrPlugin } from './definitions';

const HciAsr = registerPlugin<HciAsrPlugin>('HciAsr', {
  web: () => import('./web').then(m => new m.HciAsrWeb()),
});

export * from './definitions';
export { HciAsr };
