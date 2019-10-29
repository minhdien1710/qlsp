package com.codegym.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product,Integer> products = new HashMap<>();
    public Cart(){}
    public Cart(Map<Product,Integer> products){
        this.products = products;
    }
    public Map<Product,Integer> getProducts(){
        return products;
    }
    private boolean checkItem(Product product){
        for (Map.Entry<Product,Integer> entry : products.entrySet()){
            if(entry.getKey().getId().equals(product.getId())){
                return true;
            }
        }
        return false;
    }
    private Map.Entry<Product,Integer> selectItem(Product product){
        for (Map.Entry<Product,Integer> entry : products.entrySet()){
            if(entry.getKey().getId().equals(product.getId())){
                return entry;
            }
        }
        return null;
    }
    public void addItem(Product product){
        if(!checkItem(product)){
            products.put(product,1);
        }else {
            Map.Entry<Product,Integer> itemEntry = selectItem(product);
            Integer newQuantity = itemEntry.getValue() + 1;
            products.replace(itemEntry.getKey(),newQuantity);
        }
    }
    public void removeItem(Product product){
        if(checkItem(product)){
            Map.Entry<Product,Integer> itemEntry = selectItem(product);
            if(itemEntry.getValue() > 1){
                Integer newQuantity = itemEntry.getValue() - 1;
                products.replace(itemEntry.getKey(),newQuantity);
            }else {
                products.remove(itemEntry.getKey());
            }
        }
    }
    public Integer countProduct(){
        Integer quantity = 0;
        for (Map.Entry<Product,Integer> entry : products.entrySet()){
            quantity += entry.getValue();
        }
        return quantity;
    }
    public Integer countItem(){
        return products.size();
    }
    public Float payment(){
        float payment = 0;
        for (Map.Entry<Product,Integer> entry : products.entrySet()){
            payment += entry.getKey().getPrice() * (float)entry.getValue();
        }
        return payment;
    }
    @Override
    public String toString() {
        return "Cart{" +
                ", products=" + products +
                '}';
    }
}
