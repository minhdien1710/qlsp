package com.codegym.service.impl;

import com.codegym.model.Product;
import com.codegym.repository.ProductRepo;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepo.findAll(pageable);
    }
    @Override
    public Product findById(Long id) {
        return productRepo.findOne(id);
    }
    @Override
    public void remove(Long id) {
        productRepo.delete(id);
    }
    @Override
    public void save(Product product) {
        productRepo.save(product);
    }
}
