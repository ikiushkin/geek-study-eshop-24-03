package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repo.BrandRepository;
import ru.geekbrains.persist.repo.CategoryRepository;
import ru.geekbrains.persist.repo.ProductRepository;

import java.util.List;

@Service
public class Util {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public Util(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).get();
    }
}
