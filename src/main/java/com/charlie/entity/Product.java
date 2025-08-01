package com.charlie.entity;


import lombok.Data;

@Data
public class Product {
    private Long id;
    private Integer productId;
    private String name;
    private double price;
    private Integer stock;
}
