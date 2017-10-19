package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class OtherService {
    public String run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
