package com.example.demo;

import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.hystrix.HystrixFeign;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("github")
public interface GithubService {

    @RequestLine("GET /{git}/master/{filename}")
    String fetchRawFile(@Param("git") String git, @Param("filename") String filename);

    static GithubService get(String baseUrl) {
        return HystrixFeign.builder()
            .logLevel(Logger.Level.BASIC)
            .target(GithubService.class, baseUrl);
    }
}
