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

    return new Response("Not Found", { status: 404 });
  },
};
