package com.example.url_shortener.controller;

import com.example.url_shortener.service.ShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortenerController {

  private final ShortenerService shortenerService;

  @PostMapping("/")
  public String createShortUrl(@RequestBody String originalUrl) {
    return shortenerService.createShortUrl(originalUrl);
  }

  @GetMapping("/{shortUrl}")
  public String getOriginalUrl(@PathVariable String shortUrl) {
    return shortenerService.getOriginalUrl(shortUrl);
  }
}
