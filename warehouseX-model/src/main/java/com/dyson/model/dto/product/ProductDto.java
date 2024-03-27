package com.dyson.model.dto.product;

import com.dyson.model.entity.base.BaseEntity;
import lombok.Data;

@Data
public class ProductDto extends BaseEntity {
    private Long category1Id;
    private Long category2Id;
}