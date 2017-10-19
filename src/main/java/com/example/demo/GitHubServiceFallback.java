package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class GitHubServiceFallback implements AnotherGitHubService {
    @Override
    public String fetchRawFile(@PathVariable("git") String git, @PathVariable("filename") String filename) {
        return "NONE";
    }
}
