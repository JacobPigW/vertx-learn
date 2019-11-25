package com.jacob.vertx.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jacob
 * @create 2019-11-22 11:08
 * @desc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    private int id;

    private String title;

    private Boolean completed;

    private Integer order;

    private String url;

}
