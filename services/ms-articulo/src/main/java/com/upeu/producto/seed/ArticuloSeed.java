package com.upeu.producto.seed;

import java.math.BigDecimal;

public record ArticuloSeed(String nombre, BigDecimal precio, int stock, int stockMinimo) {}
