# ye-hello

A tiny, throwaway Cloudflare Worker used to test the delivery pipeline.

- `GET /` returns `{"name": "ye-hello", "environment": "<ENVIRONMENT>", "ref": "<BUILD_REF>"}`
- Any other path returns `404`.

No dependencies besides `wrangler` (dev), no secrets, no routes or custom domains (workers.dev only).

## Run locally

```sh
cd workers/ye-hello
npm install
npm test                 # unit tests (node's built-in test runner)
npm run dev              # wrangler dev, then: curl http://localhost:8787/
```

## Deployment

| Target     | Worker             | `ENVIRONMENT` |
|------------|--------------------|---------------|
| staging    | `ye-hello-staging` | `staging`     |
| production | `ye-hello`         | `production`  |

`BUILD_REF` defaults to `dev` in `wrangler.toml`; at deploy time it is set to the merged commit sha.

After merge, the same commit is deployed in two stages:

1. Deploy to staging (`ye-hello-staging`) and verify `GET /` reports `environment: "staging"` and the expected `ref`.
2. Deploy to production (`ye-hello`) with exactly the same commit sha.

Equivalent manual commands:

```sh
npx wrangler deploy --env staging --var BUILD_REF:<sha>
npx wrangler deploy --var BUILD_REF:<sha>
```

Deploys go through the team's Cloudflare connection.
