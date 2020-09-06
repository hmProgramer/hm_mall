package com.hm.mistakes.httpinvoke.ribbonretry;


import common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {

        Utils.loadPropertySource(CommonMistakesApplication.class,"default-ribbon.properties");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

