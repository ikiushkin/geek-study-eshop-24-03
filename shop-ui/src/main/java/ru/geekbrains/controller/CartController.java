package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.repr.CartItemRepr;
import ru.geekbrains.controller.repr.ProductRepr;
import ru.geekbrains.service.CartService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.service.model.LineItem;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    public final CartService cartService;

    public final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("lineItems", cartService.getLineItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "shopping-cart";
    }

    @PostMapping
    public String addToCart(CartItemRepr cartItemRepr) {
        ProductRepr productRepr = productService.findById(cartItemRepr.getProductId())
                .orElseThrow(NotFoundException::new);
        cartService.addProductQty(productRepr, "", "", cartItemRepr.getQty());
        return "redirect:/cart";
    }

    @PostMapping(path = "/update_all_qty")
    public String updateAllQty(@RequestParam Map<String, String> productQtyMap) {
        logger.info("Product Qty Map: {}", productQtyMap);

        cartService.updateAllQty(productQtyMap);

        return "redirect:/cart";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String removeFromCart(@RequestParam(name = "productId") Long productID) {
        cartService.removeProductById(productID);
        if (cartService.getLineItems().size() < 1) {
            return "redirect:/";
        }
        return "redirect:/cart";
    }
}
