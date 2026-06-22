package com.upeu.catalogo.seed;

import java.util.List;

public final class RubroCatalog {

    public record RubroSeed(String nombre, String descripcion) {}

    public static final List<RubroSeed> RUBROS = List.of(
            new RubroSeed("Abarrotes básicos", "Azúcar, sal, harina, levadura"),
            new RubroSeed("Arroz", "Arroz extra, superior, integral"),
            new RubroSeed("Menestras y granos", "Lentejas, frijoles, arvejas, garbanzos"),
            new RubroSeed("Pastas", "Fideos, spaghettis, macarrones, lasaña"),
            new RubroSeed("Aceites", "Vegetales, oliva, girasol, aceite de coco"),
            new RubroSeed("Salsas y condimentos", "Mayonesa, ketchup, mostaza, especias"),
            new RubroSeed("Lácteos", "Leche UHT, evaporada, condensada"),
            new RubroSeed("Yogurt y postres lácteos", "Yogurt, manjar, mousse"),
            new RubroSeed("Quesos y mantequillas", "Quesos frescos, edam, parmesano"),
            new RubroSeed("Huevos y untables", "Huevos, margarina, mermeladas"),
            new RubroSeed("Embutidos y fiambres", "Jamones, salchichas, mortadelas"),
            new RubroSeed("Panadería", "Pan, tostadas, bizcochos"),
            new RubroSeed("Bebidas gaseosas", "Gaseosas, colas, sabores"),
            new RubroSeed("Aguas", "Agua sin gas, con gas, soda"),
            new RubroSeed("Jugos y néctares", "Jugos, néctares, refrescos naturales"),
            new RubroSeed("Cervezas", "Cervezas nacionales e importadas"),
            new RubroSeed("Vinos y espumantes", "Vinos tinto, blanco, rosé, espumante"),
            new RubroSeed("Licores y piscos", "Pisco, ron, whisky, vodka"),
            new RubroSeed("Snacks salados", "Papitas, chizitos, maní, camote"),
            new RubroSeed("Golosinas y chocolates", "Caramelos, chocolates, gomitas"),
            new RubroSeed("Galletas y cereales", "Galletas dulces, soda, cereales"),
            new RubroSeed("Conservas y enlatados", "Atún, sardinas, frutas en almíbar"),
            new RubroSeed("Frutas y verduras", "Productos frescos por peso"),
            new RubroSeed("Carnes rojas", "Res, cerdo, molida, cortes"),
            new RubroSeed("Pollo y pavo", "Pollo entero, pechuga, alitas"),
            new RubroSeed("Pescados y mariscos", "Pescado fresco, congelado, mariscos"),
            new RubroSeed("Congelados", "Nuggets, papa, vegetales, pizzas"),
            new RubroSeed("Helados y postres", "Helados, paletas, postres fríos"),
            new RubroSeed("Limpieza del hogar", "Detergentes, cloro, desinfectantes"),
            new RubroSeed("Higiene personal", "Shampoo, jabón, pasta dental"),
            new RubroSeed("Bebé y pañales", "Pañales, toallitas, fórmulas"),
            new RubroSeed("Mascotas", "Alimento perro y gato"),
            new RubroSeed("Desechables", "Vasos, platos, servilletas, bolsas"),
            new RubroSeed("Salud y botiquín", "Analgésicos, vitaminas, primeros auxilios"),
            new RubroSeed("Electro hogar básico", "Pilas, focos, extensiones")
    );

    private RubroCatalog() {}
}
