package com.jacob.vertx.test;

import com.jacob.vertx.test.verticles.HttpServerVerticle;
import io.vertx.core.Vertx;

/**
 * @author Jacob
 * @create 2019-11-21 18:06
 * @desc
 */
public class Test {

    public static void main(String[] args) {
        HttpServerVerticle server = new HttpServerVerticle();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(server);
    }

}
