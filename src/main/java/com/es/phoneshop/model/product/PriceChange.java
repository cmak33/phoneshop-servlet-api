package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Date;

public record PriceChange(Date dateOfChange, BigDecimal price) {
}
