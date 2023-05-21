package com.example.springwithtechie.repository;

import com.example.springwithtechie.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
