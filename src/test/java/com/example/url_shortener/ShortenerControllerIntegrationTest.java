package com.example.url_shortener;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  public void setup() {
    baseUrl = "http://localhost:" + port + "/";
  }

  @Test
  void testCreateShortUrl() {
    String originalUrl = "https://www.example.com";
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "text/plain");

    HttpEntity<String> request = new HttpEntity<>(originalUrl, headers);
    ResponseEntity<String> response = restTemplate.exchange(baseUrl, POST, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void testGetOriginalUrl() {
    // Step 1: Create Short URL
    String originalUrl = "https://www.second.com";
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "text/plain");

    HttpEntity<String> request = new HttpEntity<>(originalUrl, headers);
    ResponseEntity<String> response = restTemplate.exchange(baseUrl, POST, request, String.class);

    // Check if short URL is created successfully
    assertThat(response.getStatusCode()).isEqualTo(OK);
    String shortUrl = response.getBody();
    assertThat(shortUrl).isNotEmpty();

    // Step 2: Retrieve the original URL using the short URL
    ResponseEntity<String> originalUrlResponse = restTemplate.getForEntity(baseUrl + shortUrl, String.class);

    // Check if the original URL is retrieved successfully
    assertThat(originalUrlResponse.getStatusCode()).isEqualTo(OK);
    assertThat(originalUrlResponse.getBody()).isEqualTo(originalUrl);
  }

  @Test
  void testCreateThroughput() throws InterruptedException {
    // Define the number of requests
    int totalRequests = 20000;
    ExecutorService executorService = Executors.newFixedThreadPool(10);  // Simulating concurrent requests

    // Track the start time
    long startTime = System.currentTimeMillis();

    // Run requests concurrently
    for (int i = 0; i < totalRequests; i++) {
      executorService.submit(() -> {
        String originalUrl = "https://example.com/" + RandomString.make(10);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");

        HttpEntity<String> request = new HttpEntity<>(originalUrl, headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, POST, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
      });
    }

    // Shutdown the executor and wait for tasks to complete
    executorService.shutdown();
    boolean finished = executorService.awaitTermination(5, TimeUnit.MINUTES);  // Wait up to 5 minutes for the tasks to complete

    // Track the end time
    long endTime = System.currentTimeMillis();

    // Calculate and print throughput (requests per second)
    long durationMillis = endTime - startTime;
    double requestsPerSecond = (double) totalRequests / (durationMillis / 1000.0);

    System.out.println("Total Requests: " + totalRequests);
    System.out.println("Total Time (ms): " + durationMillis);
    System.out.println("Requests Per Second (RPS): " + requestsPerSecond);

    assertThat(finished).isTrue();  // Ensure all tasks are finished
    assertThat(requestsPerSecond).isGreaterThan(10.0);  // Adjust this threshold as per requirement
  }
}
