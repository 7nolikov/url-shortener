package com.example.url_shortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UrlContainer {

  @Id @GeneratedValue private Long id;

  @Column(unique = true)
  private String shortUrl;

  private String originalUrl;
}
