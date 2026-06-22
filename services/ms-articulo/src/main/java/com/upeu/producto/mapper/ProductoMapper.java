package com.upeu.producto.mapper;

import com.upeu.producto.dto.ProductoRequest;
import com.upeu.producto.dto.ProductoResponse;
import com.upeu.producto.entity.Producto;
import com.upeu.producto.util.CodigoBarrasUtil;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        if (request == null) {
            return null;
        }

        return Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .idRubro(request.getIdRubro())
                .precio(request.getPrecio())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .stockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : 5)
                .codigoBarras(CodigoBarrasUtil.normalizar(request.getCodigoBarras()))
                .imagenUrl(request.getImagenUrl())
                .build();
    }

    public ProductoResponse toResponse(Producto entity) {
        if (entity == null) {
            return null;
        }

        boolean stockBajo = entity.getStock() != null && entity.getStockMinimo() != null
                && entity.getStock() <= entity.getStockMinimo();

        return ProductoResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .idRubro(entity.getIdRubro())
                .precio(entity.getPrecio())
                .stock(entity.getStock())
                .stockMinimo(entity.getStockMinimo())
                .codigoBarras(entity.getCodigoBarras())
                .imagenUrl(entity.getImagenUrl())
                .stockBajo(stockBajo)
                .build();
    }

    public void updateEntityFromRequest(Producto entity, ProductoRequest request) {
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setIdRubro(request.getIdRubro());
        entity.setPrecio(request.getPrecio());
        if (request.getStock() != null) {
            entity.setStock(request.getStock());
        }
        if (request.getStockMinimo() != null) {
            entity.setStockMinimo(request.getStockMinimo());
        }
        entity.setCodigoBarras(CodigoBarrasUtil.normalizar(request.getCodigoBarras()));
        entity.setImagenUrl(request.getImagenUrl());
    }
}
