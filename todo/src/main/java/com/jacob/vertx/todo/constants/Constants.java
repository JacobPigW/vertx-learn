package com.jacob.vertx.todo.constants;

/**
 * @author Jacob
 * @create 2019-11-22 11:14
 * @desc
 */
public final class Constants {

    private Constants() {
    }

    /**
     * API Route
     */
    public static final String API_GET = "/todos/:todoId";
    public static final String API_LIST_ALL = "/todos";
    public static final String API_CREATE = "/todos";
    public static final String API_UPDATE = "/todos/:todoId";
    public static final String API_DELETE = "/todos/:todoId";
    public static final String API_DELETE_ALL = "/todos";

}
