package com.jacob.vertx.test.verticles;

import io.vertx.core.AbstractVerticle;

/**
 * @author Jacob
 * @create 2019-11-21 20:13
 * @desc
 */
public class HttpServerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            //获取到response对象
            req.response()
                    //设置响应头
                    .putHeader("content-type", "text/plain")
                    //响应数据
                    .end("Hello from Vert.x!");
        }).listen(8899);
    }

}
