package com.wg.quarkus.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

//for resource object example
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    public String content;
}