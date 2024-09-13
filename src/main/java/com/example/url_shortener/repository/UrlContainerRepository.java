package com.example.url_shortener.repository;

import com.example.url_shortener.model.UrlContainer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the URL container.
 */
public interface UrlContainerRepository extends JpaRepository<UrlContainer, Long> {}
