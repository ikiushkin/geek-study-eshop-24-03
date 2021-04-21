package ru.geekbrains.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.repr.ProductRepr;
import ru.geekbrains.service.model.LineItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService, Serializable {

    private final Map<LineItem, Integer> lineItems;

    public CartServiceImpl() {
        this.lineItems = new HashMap<>();
    }

    @JsonCreator
    public CartServiceImpl(@JsonProperty("lineItems") List<LineItem> lineItems) {
        this.lineItems = lineItems.stream().collect(Collectors.toMap(li -> li, LineItem::getQty));
    }

    @Override
    public void addProductQty(ProductRepr productRepr, String color, String material, int qty) {
        LineItem lineItem = new LineItem(productRepr, color, material);
        lineItems.put(lineItem, lineItems.getOrDefault(lineItem, 0) + qty);
    }

    @Override
    public void removeProductQty(ProductRepr productRepr, String color, String material, int qty) {
        LineItem lineItem = new LineItem(productRepr, color, material);
        int currentQty = lineItems.getOrDefault(lineItem, 0);
        if (currentQty - qty > 0) {
            lineItems.put(lineItem, currentQty - qty);
        } else {
            lineItems.remove(lineItem);
        }
    }

    @Override
    public List<LineItem> getLineItems() {
        lineItems.forEach(LineItem::setQty);
        return new ArrayList<>(lineItems.keySet());
    }

    @Override
    public Integer getTotalPrice() {
        int totalSum = 0;
        for (Map.Entry<LineItem, Integer> entry : lineItems.entrySet()) {
            int price = entry.getKey().getProductRepr().getPrice().intValue();
            int qty = entry.getValue();

            totalSum +=  price * qty;
        }
        return totalSum;
    }

    @Override
    public void updateAllQty(Map<String, String> productQtyMap) {
        long productID;
        int newQty;

        if (productQtyMap.size() > 0) {
            for(Map.Entry<String, String> newMap : productQtyMap.entrySet()) {
                productID = Long.parseLong(newMap.getKey());
                newQty = Integer.parseInt(newMap.getValue());

                for (Map.Entry<LineItem, Integer> oldMap : lineItems.entrySet()) {
                    LineItem lineItem = oldMap.getKey();
                    if (oldMap.getKey().getProductId().equals(productID)) {
                        lineItems.put(lineItem, newQty);
                    }
                }
            }
        }
    }

    @JsonIgnore
    @Override
    public BigDecimal getSubTotal() {
        lineItems.forEach(LineItem::setQty);
        return lineItems.keySet().stream()
                .map(LineItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void removeProductById(Long id) {
        lineItems.keySet().removeIf(key -> key.getProductId().equals(id));
    }
}
