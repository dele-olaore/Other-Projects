import { test } from "node:test";
import assert from "node:assert/strict";
import worker from "../src/index.js";

const env = { ENVIRONMENT: "staging", BUILD_REF: "abc123" };
const call = (path, method = "GET") =>
  worker.fetch(new Request(`https://example.com${path}`, { method }), env);

test("GET / returns name, environment and ref as JSON", async () => {
  const res = await call("/");
  assert.equal(res.status, 200);
  assert.match(res.headers.get("content-type"), /application\/json/);
  assert.deepEqual(await res.json(), {
    name: "ye-hello",
    environment: "staging",
    ref: "abc123",
  });
});

test("GET /health returns ok and environment as JSON", async () => {
  const res = await call("/health");
  assert.equal(res.status, 200);
  assert.match(res.headers.get("content-type"), /application\/json/);
  assert.deepEqual(await res.json(), { ok: true, environment: "staging" });
});

test("GET /version returns name, ref and environment as JSON", async () => {
  const res = await call("/version");
  assert.equal(res.status, 200);
  assert.match(res.headers.get("content-type"), /application\/json/);
  assert.deepEqual(await res.json(), {
    name: "ye-hello",
    ref: "abc123",
    environment: "staging",
  });
});

test("non-GET on /version returns 404", async () => {
  assert.equal((await call("/version", "POST")).status, 404);
});

test("non-GET on /health returns 404", async () => {
  assert.equal((await call("/health", "POST")).status, 404);
});

test("any other path returns 404", async () => {
  for (const path of ["/foo", "/index.html", "/health/"]) {
    const res = await call(path);
    assert.equal(res.status, 404, path);
  }
});

test("non-GET on / returns 404", async () => {
  assert.equal((await call("/", "POST")).status, 404);
});
