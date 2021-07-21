package com.springall;

import com.springall.model.Dats;
import com.springall.model.MySource;
import com.springall.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author york
 * @since 2021/7/15
 * com.springAll
 * mq
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAllTestApplication {

    @Autowired
    private Person person;
    @Autowired
    private Dats dats;

    @Autowired
    private MySource mySource;
    @Autowired
    private ApplicationContext context;

    @Value("${person.name}")
    private String name;

    @Before
    public void setUp() {
        System.out.println("test");
    }


    @Test
    public void configuration() throws Exception {
        System.out.println(name);
    }
}
