package com.springall.model;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author york
 * @since 2021/7/15
 * com.springall.model
 * dats
 */
//@ConfigurationProperties(prefix = "test")
@Component
@PropertySource("classpath:test1.yml") //引入自定义配置文件
public class Dats {

    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "Dats{" +
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
