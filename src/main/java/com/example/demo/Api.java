package com.example.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    private OtherService otherService;

    public Api(OtherService otherService) {
        this.otherService = otherService;
    }

    @GetMapping("/safe")
    public String safe() {
        return new com.netflix.hystrix.HystrixCommand<String>(setter()) {
            @Override
            protected String run() throws Exception {
                otherService.run();
                return "OK";
            }
        }.execute();
    }

    @GetMapping("/unsafe")
    public String unsafe() {
        return otherService.run();
    }

    private com.netflix.hystrix.HystrixCommand.Setter setter() {
        return HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("External"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("/safe"));
    }
}
