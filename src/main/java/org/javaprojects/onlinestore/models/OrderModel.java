package org.javaprojects.onlinestore.models;

import java.math.BigDecimal;
import java.util.List;

public record OrderModel(
    Long id,
    List<ItemModel> items,
    BigDecimal totalSum
) {}
