package com.example.demo;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GitHubServiceTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(10087));
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private AnotherGitHubService serviceWithFallback;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void should_fetch_meta_file_success() throws Exception {
        String rawMetaFileContent = "content";

        stubFor(
            get(urlEqualTo("/aisensiy/hello-project/master/meta.yml"))
                .willReturn(ok(rawMetaFileContent)));

        String metaFile = gitHubService.fetchRawFile("aisensiy/hello-project", "meta.yml");
        assertThat(metaFile, is(rawMetaFileContent));
    }


    @Test(expected = HystrixRuntimeException.class)
    public void should_fail_for_fetching_file() throws Exception {
        String rawMetaFileContent = "content";

        stubFor(
            get(urlEqualTo("/aisensiy/hello-project/master/meta.yml"))
                .willReturn(ok(rawMetaFileContent).withFixedDelay(5000)));

        gitHubService.fetchRawFile("aisensiy/hello-project", "meta.yml");
    }

    @Test
    public void should_get_fallback_result() throws Exception {

        String rawMetaFileContent = "content";

        stubFor(
            get(urlEqualTo("/aisensiy/hello-project/master/meta.yml"))
                .willReturn(ok(rawMetaFileContent).withFixedDelay(5000)));

        String result = serviceWithFallback.fetchRawFile("aisensiy/hello-project", "meta.yml");
        assertThat(result, is(new GitHubServiceFallback().fetchRawFile("aisensiy/hello-project", "meta.yml")));
    }
}
