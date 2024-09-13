package com.example.url_shortener.service;

/** A service that generates short URLs. */
public interface ShortUrlGenerator {

  /**
   * Generates a short URL for the given original URL.
   *
   * @param originalUrl the original URL
   * @return the generated short URL
   */
  String generateShortUrl(String originalUrl);
}
