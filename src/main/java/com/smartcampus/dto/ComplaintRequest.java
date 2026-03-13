package com.smartcampus.dto;

import lombok.Data;

@Data
public class ComplaintRequest {
    private String category;
    private String description;
    private String imageUrl;
    private String priority;
    private String location;
}
