package com.upeu.catalogo.config;

import com.upeu.catalogo.entity.Categoria;
import com.upeu.catalogo.repository.CategoriaRepository;
import com.upeu.catalogo.seed.RubroCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RubroDataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            log.info("Rubros ya existen ({}). Seeder omitido.", categoriaRepository.count());
            return;
        }

        RubroCatalog.RUBROS.forEach(seed -> categoriaRepository.save(Categoria.builder()
                .nombre(seed.nombre())
                .descripcion(seed.descripcion())
                .build()));

        log.info("Seeder rubros: {} rubros cargados (estilo supermercado).", categoriaRepository.count());
    }
}
