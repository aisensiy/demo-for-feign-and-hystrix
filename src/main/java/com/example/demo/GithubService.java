package com.example.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "github", url = "${github.url}")
public interface GitHubService {

    @GetMapping("/{git}/master/{filename}")
    String fetchRawFile(
        @PathVariable("git") String git,
        @PathVariable("filename") String filename);

}
