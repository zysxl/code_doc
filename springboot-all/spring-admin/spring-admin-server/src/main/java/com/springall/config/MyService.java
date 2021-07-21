package com.springall.config;

/**
 * @author york
 * @since 2021/7/15
 * com.springall.config
 * MyService
 */
public class MyService {
    private String id = "1";
    private String name = "zhangs";

    @Override
    public String toString() {
        return "MyService{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
