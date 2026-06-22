package com.upeu.producto.config;

import com.upeu.producto.entity.Producto;
import com.upeu.producto.repository.ProductoRepository;
import com.upeu.producto.seed.ArticuloCatalog;
import com.upeu.producto.seed.ArticuloSeed;
import com.upeu.producto.util.CodigoBarrasUtil;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticuloDataSeeder implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("Articulos ya existen ({}). Seeder omitido.", productoRepository.count());
            return;
        }

        Map<Integer, List<ArticuloSeed>> catalogo = ArticuloCatalog.porRubro();
        int total = 0;

        for (Map.Entry<Integer, List<ArticuloSeed>> entry : catalogo.entrySet()) {
            int rubroId = entry.getKey();
            List<ArticuloSeed> articulos = entry.getValue();
            for (int i = 0; i < articulos.size(); i++) {
                ArticuloSeed seed = articulos.get(i);
                productoRepository.save(Producto.builder()
                        .nombre(seed.nombre())
                        .descripcion("Rubro " + rubroId + " — " + seed.nombre())
                        .idRubro(rubroId)
                        .precio(seed.precio())
                        .stock(seed.stock())
                        .stockMinimo(seed.stockMinimo())
                        .codigoBarras(CodigoBarrasUtil.generarEan13(rubroId, i + 1))
                        .build());
                total++;
            }
        }

        log.info("Seeder articulos: {} articulos cargados en {} rubros.", total, catalogo.size());
    }
}
