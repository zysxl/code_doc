package com.springall.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author york
 * @since 2021/7/15
 * com.springall.model
 * MySource
 */
@PropertySource("classpath:test.properties")
@Component
@ConfigurationProperties(prefix = "test")
public class MySource {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "MySource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
