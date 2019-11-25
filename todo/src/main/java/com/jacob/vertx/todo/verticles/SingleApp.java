package com.jacob.vertx.todo.verticles;

import com.google.common.collect.Sets;
import com.jacob.vertx.todo.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jacob
 * @create 2019-11-22 11:12
 * @desc
 */
public class SingleApp extends AbstractVerticle {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int HTTP_PORT = 8082;
    private static final int REDIS_PORT = 6479;

    @Override
    public void start(Future<Void> future) throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders())
                .allowedMethods(allowMethods()));
        router.route().handler(BodyHandler.create());

        router.get(Constants.API_GET);
        router.get(Constants.API_LIST_ALL);
        router.post(Constants.API_CREATE);
        router.put(Constants.API_UPDATE);
        router.delete(Constants.API_DELETE);
        router.delete(Constants.API_DELETE_ALL);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(HTTP_PORT, HTTP_HOST);
    }

    private Set<String> allowHeaders() {
        Set<String> ret = Sets.newHashSet();
        ret.add("x-requested-with");
        ret.add("Access-Control-Allow-Origin");
        ret.add("origin");
        ret.add("Content-Type");
        ret.add("accept");

        return ret;
    }

    private Set<HttpMethod> allowMethods() {
        Set<HttpMethod> ret = Sets.newHashSet();
        ret.add(HttpMethod.GET);
        ret.add(HttpMethod.POST);
        ret.add(HttpMethod.DELETE);
        ret.add(HttpMethod.PATCH);
        ret.add(HttpMethod.PUT);

        return ret;
    }

}
