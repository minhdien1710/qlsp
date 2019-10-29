package com.codegym.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneralService<E> {
    Page<E> findAll(Pageable pageable);
    E findById(Long id);
    void remove(Long id);
    void save(E e);
}
