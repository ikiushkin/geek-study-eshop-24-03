package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.service.Util;

@Controller
@RequestMapping
public class ProductController {

    private Util util;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(Util util) {
        this.util = util;
    }

    @GetMapping
    public String productListPage(Model model) {
        logger.info("Product list page");
        model.addAttribute("productList", util.getAllProduct());
        return "categories-left-sidebar";
    }

    @GetMapping("/product-details")
    public String viewProductDetails(Model model, @RequestParam("id") Long id) {
        Product product = util.findProductById(id);
        logger.info("View product: " + product.getName());
        model.addAttribute("product", product);
        return "product-details";
    }
}
