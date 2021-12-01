# hci-asr

capacitor plugin for hci-asr

## Install

```bash
npm install hci-asr
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`sdkInit(...)`](#sdkinit)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### sdkInit(...)

```typescript
sdkInit(options: InitOptions) => Promise<{ value: string; }>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#initoptions">InitOptions</a></code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

**Since:** 0.0.1

--------------------


### Interfaces


#### InitOptions

| Prop         | Type                |
| ------------ | ------------------- |
| **`appKey`** | <code>string</code> |
| **`secret`** | <code>string</code> |
| **`sysUrl`** | <code>string</code> |
| **`capUrl`** | <code>string</code> |

</docgen-api>
