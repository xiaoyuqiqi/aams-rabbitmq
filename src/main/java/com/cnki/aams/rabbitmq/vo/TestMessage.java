package com.cnki.aams.rabbitmq.vo;

import java.io.Serializable;

public class TestMessage implements Serializable {
    private String name;
    private Integer age;
    private Integer number;

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

    @Override
    public String toString() {
        return "TestMessage{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", number=" + number +
                '}';
    }
}
