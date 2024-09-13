package com.example.url_shortener;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

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
    String originalUrl = "https://www.example.com";
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
}
