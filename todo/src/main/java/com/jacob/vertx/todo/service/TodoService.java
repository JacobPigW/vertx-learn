package com.jacob.vertx.todo.service;

import com.jacob.vertx.todo.entity.Todo;
import io.vertx.core.Future;

import java.util.List;
import java.util.Optional;

/**
 * @author Jacob
 * @create 2019-11-22 14:16
 * @desc
 */
public interface TodoService {

    Future<Boolean> insert(Todo todo);

    Future<List<Todo>> getAll();

    Future<Optional<Todo>> getCertain(String todoID);

    Future<Todo> update(String todoId, Todo newTodo);

    Future<Boolean> delete(String todoId);

    Future<Boolean> deleteAll();

}
