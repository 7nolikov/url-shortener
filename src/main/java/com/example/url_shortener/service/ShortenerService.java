package com.example.url_shortener.service;

public interface ShortenerService {

  /**
   * Create a short URL for the given URL.
   * @return the short URL
   */
  String createShortUrl(String originalUrl);

  /**
   * Get the original URL for the given short URL.
   * @param shortUrl the short URL
   * @return the original URL
   */
  String getOriginalUrl(String shortUrl);
}
