package com.codegym.repository;

import com.codegym.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepo extends PagingAndSortingRepository<Product,Long> {

}
