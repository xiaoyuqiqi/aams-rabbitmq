package com.cnki.aams.rabbitmq.vo;

import java.io.Serializable;

public class TestMessageRabbitHandler implements Serializable {
    private String name;
    private Integer age;
    private Integer number;
    private String error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "TestMessageRabbitHandler{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", number=" + number +
                ", error='" + error + '\'' +
                '}';
    }
}
