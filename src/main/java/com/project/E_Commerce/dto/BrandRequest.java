package com.project.E_Commerce.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Data
public class BrandRequest {

    private String brandName;
    private List<Integer> categoryIds;

}
