export default {
  async fetch(request, env) {
    const url = new URL(request.url);

    if (request.method === "GET" && url.pathname === "/") {
      return Response.json({
        name: "ye-hello",
        environment: env.ENVIRONMENT,
        ref: env.BUILD_REF,
      });
    }

    if (request.method === "GET" && url.pathname === "/health") {
      return Response.json({ ok: true, environment: env.ENVIRONMENT });
    }

    if (request.method === "GET" && url.pathname === "/version") {
      return Response.json({
        name: "ye-hello",
        ref: env.BUILD_REF,
        environment: env.ENVIRONMENT,
      });
    }

    return new Response("Not Found", { status: 404 });
  },
};
