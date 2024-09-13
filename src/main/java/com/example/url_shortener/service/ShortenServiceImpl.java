package com.example.url_shortener.service;

import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.UrlContainer;
import com.example.url_shortener.repository.UrlContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortenServiceImpl implements ShortenerService {

  private final UrlContainerRepository urlContainerRepository;
  private final ShortUrlGenerator shortUrlGenerator;

  @Override
  public String createShortUrl(String originalUrl) {
    UrlContainer urlContainer = new UrlContainer();
    urlContainer.setShortUrl(shortUrlGenerator.generateShortUrl(originalUrl));
    urlContainer.setOriginalUrl(originalUrl);
    UrlContainer savedUrlContainer = urlContainerRepository.save(urlContainer);
    return savedUrlContainer.getShortUrl();
  }

  @Override
  public String getOriginalUrl(String shortUrl) {
    UrlContainer urlContainerProbe = new UrlContainer();
    urlContainerProbe.setShortUrl(shortUrl);
    return urlContainerRepository
        .findOne(Example.of(urlContainerProbe))
        .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortUrl))
        .getOriginalUrl();
  }
}
