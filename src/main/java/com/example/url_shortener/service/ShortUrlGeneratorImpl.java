package com.example.url_shortener.service;

import java.nio.charset.StandardCharsets;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlGeneratorImpl implements ShortUrlGenerator{

  @Override
  public String generateShortUrl(String originalUrl) {
    byte[] decodedBytes = Base64.encode(originalUrl.getBytes());
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }
}
