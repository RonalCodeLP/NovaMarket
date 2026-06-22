package com.upeu.producto.seed;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public final class ArticuloCatalog {

    private ArticuloCatalog() {}

    public static Map<Integer, List<ArticuloSeed>> porRubro() {
        return Map.ofEntries(
                Map.entry(1, abarrotesBasicos()),
                Map.entry(2, arroz()),
                Map.entry(3, menestras()),
                Map.entry(4, pastas()),
                Map.entry(5, aceites()),
                Map.entry(6, salsas()),
                Map.entry(7, lacteos()),
                Map.entry(8, yogurt()),
                Map.entry(9, quesos()),
                Map.entry(10, huevos()),
                Map.entry(11, embutidos()),
                Map.entry(12, panaderia()),
                Map.entry(13, gaseosas()),
                Map.entry(14, aguas()),
                Map.entry(15, jugos()),
                Map.entry(16, cervezas()),
                Map.entry(17, vinos()),
                Map.entry(18, licores()),
                Map.entry(19, snacks()),
                Map.entry(20, golosinas()),
                Map.entry(21, galletas()),
                Map.entry(22, conservas()),
                Map.entry(23, frutasVerduras()),
                Map.entry(24, carnes()),
                Map.entry(25, pollo()),
                Map.entry(26, pescados()),
                Map.entry(27, congelados()),
                Map.entry(28, helados()),
                Map.entry(29, limpieza()),
                Map.entry(30, higiene()),
                Map.entry(31, bebe()),
                Map.entry(32, mascotas()),
                Map.entry(33, desechables()),
                Map.entry(34, salud()),
                Map.entry(35, electroHogar())
        );
    }

    private static ArticuloSeed a(String nombre, String precio, int stock, int stockMinimo) {
        return new ArticuloSeed(nombre, new BigDecimal(precio), stock, stockMinimo);
    }

    // 1 Abarrotes básicos
    private static List<ArticuloSeed> abarrotesBasicos() {
        return List.of(
                a("Azúcar blanca Cartavio 1 kg", "4.90", 120, 15),
                a("Sal de mesa Maras 1 kg", "2.50", 150, 20),
                a("Harina Blanca Flor 1 kg", "4.20", 100, 12),
                a("Levadura Fleischmann 11 g", "1.80", 80, 10),
                a("Maicena Molino Victoria 200 g", "3.50", 90, 10),
                a("Avena Quaker tradicional 500 g", "6.90", 75, 8),
                a("Maíz cancha Costeño 500 g", "5.40", 60, 8),
                a("Sémola Molino Bellavista 1 kg", "4.80", 85, 10),
                a("Gelatina Gloria sabor fresa 130 g", "3.20", 70, 8),
                a("Flan Gloria vainilla 130 g", "3.20", 65, 8),
                a("Cacao Nesquik 400 g", "12.90", 55, 6),
                a("Canela en polvo McCormick 45 g", "5.90", 45, 5)
        );
    }

    // 2 Arroz
    private static List<ArticuloSeed> arroz() {
        return List.of(
                a("Arroz Costeño Extra 1 kg", "4.50", 200, 25),
                a("Arroz Paisana Superior 5 kg", "19.90", 150, 20),
                a("Arroz Graneadito Valle Norte 1 kg", "4.80", 180, 22),
                a("Arroz Integral Costeño 1 kg", "6.90", 90, 10),
                a("Arroz Basmati Oriente 500 g", "8.50", 70, 8),
                a("Arroz Chaufa Don Vittorio 250 g", "3.90", 85, 10),
                a("Arroz Rojo Doña Pepa 1 kg", "5.20", 95, 12),
                a("Arroz Parboil Paisana 5 kg", "21.50", 80, 10),
                a("Arroz Japonés para sushi 1 kg", "14.90", 50, 6),
                a("Arroz con leche Gloria listo 200 g", "4.50", 60, 8),
                a("Arroz inflado Nature's Heart 200 g", "7.90", 55, 6),
                a("Arroz orgánico Valle Sagrado 1 kg", "9.90", 40, 5)
        );
    }

    // 3 Menestras
    private static List<ArticuloSeed> menestras() {
        return List.of(
                a("Lentejas Costeño 500 g", "4.20", 130, 15),
                a("Frijoles canarios Costeño 500 g", "4.50", 125, 15),
                a("Arvejas secas Costeño 500 g", "4.80", 110, 12),
                a("Garbanzos Costeño 500 g", "5.20", 100, 12),
                a("Haba seca Costeño 500 g", "5.50", 95, 10),
                a("Pallares secos Costeño 500 g", "6.90", 85, 10),
                a("Quinua blanca Inca Sur 500 g", "12.90", 70, 8),
                a("Quinua roja y blanca 500 g", "13.50", 65, 8),
                a("Kiwicha molida 500 g", "8.90", 60, 6),
                a("Alverjón partido 500 g", "3.90", 90, 10),
                a("Frijol rojo selecto 1 kg", "8.50", 80, 10),
                a("Menestras mix Costeño 500 g", "5.90", 75, 8)
        );
    }

    // 4 Pastas
    private static List<ArticuloSeed> pastas() {
        return List.of(
                a("Fideos spaghetti Don Vittorio 400 g", "3.50", 160, 20),
                a("Fideos tallarín Don Vittorio 400 g", "3.50", 155, 20),
                a("Macarrones Don Vittorio 400 g", "3.20", 150, 18),
                a("Coditos Don Vittorio 400 g", "3.20", 145, 18),
                a("Lasagna Don Vittorio 500 g", "5.90", 80, 10),
                a("Ravioles de carne Don Vittorio 500 g", "8.90", 60, 8),
                a("Fideos instantáneos Aji-no-men pollo 85 g", "2.50", 200, 25),
                a("Fideos instantáneos Aji-no-men carne 85 g", "2.50", 195, 25),
                a("Fideos chinos oriental 400 g", "4.20", 90, 10),
                a("Ñoquis Don Vittorio 500 g", "6.50", 70, 8),
                a("Fetuccini Don Vittorio 400 g", "3.80", 100, 12),
                a("Fideos cabello de ángel 400 g", "3.60", 95, 10)
        );
    }

    // 5 Aceites
    private static List<ArticuloSeed> aceites() {
        return List.of(
                a("Aceite Primor vegetal 1 L", "9.90", 140, 18),
                a("Aceite Cil vegetal 900 ml", "8.50", 130, 15),
                a("Aceite de oliva Primor 500 ml", "18.90", 70, 8),
                a("Aceite de girasol Cil 1 L", "10.50", 100, 12),
                a("Aceite de coco Orgánica 500 ml", "22.90", 45, 5),
                a("Aceite de ajonjolí 250 ml", "12.50", 50, 6),
                a("Aceite de maíz Primor 1 L", "10.90", 85, 10),
                a("Aceite en spray Primor 200 ml", "14.90", 55, 6),
                a("Manteca de cerdo Laive 200 g", "6.90", 75, 8),
                a("Aceite de palma Primor 1 L", "9.50", 90, 10),
                a("Aceite de aguacate 250 ml", "24.90", 35, 5),
                a("Aceite mezcla Primor 900 ml", "8.90", 110, 12)
        );
    }

    // 6 Salsas
    private static List<ArticuloSeed> salsas() {
        return List.of(
                a("Mayonesa Alacena 395 g", "6.90", 120, 15),
                a("Ketchup Hellmann's 397 g", "7.50", 115, 12),
                a("Mostaza McCormick 250 g", "5.90", 90, 10),
                a("Salsa de tomate Gloria 400 g", "3.80", 130, 15),
                a("Salsa de tomate Aji-no-men 400 g", "3.50", 125, 15),
                a("Ají amarillo Dona Isabel 400 g", "5.50", 100, 10),
                a("Ají panca Dona Isabel 400 g", "5.50", 95, 10),
                a("Salsa de soya Aji-no-men 150 ml", "4.20", 85, 10),
                a("Salsa inglesa Savora 250 ml", "6.50", 80, 8),
                a("Salsa BBQ Hellmann's 250 g", "8.90", 70, 8),
                a("Salsa tártara Alacena 250 g", "7.90", 65, 6),
                a("Comino molido McCormick 50 g", "4.50", 75, 8)
        );
    }

    // 7 Lácteos
    private static List<ArticuloSeed> lacteos() {
        return List.of(
                a("Leche Gloria entera UHT 1 L", "4.90", 180, 25),
                a("Leche Laive entera UHT 1 L", "4.70", 175, 25),
                a("Leche entera Pura Vida 1 L", "4.50", 170, 22),
                a("Leche deslactosada Gloria 1 L", "5.90", 120, 15),
                a("Leche evaporada Gloria 410 ml", "4.20", 150, 18),
                a("Leche condensada Gloria 397 g", "6.50", 140, 15),
                a("Leche en polvo Gloria 400 g", "18.90", 90, 10),
                a("Leche en polvo Laive 400 g", "17.90", 85, 10),
                a("Leche chocolate Gloria 1 L", "5.50", 100, 12),
                a("Leche light Laive 1 L", "5.20", 95, 10),
                a("Crema de leche Gloria 200 ml", "4.90", 110, 12),
                a("Leche de almendras Nature's Heart 1 L", "12.90", 55, 6)
        );
    }

    // 8 Yogurt
    private static List<ArticuloSeed> yogurt() {
        return List.of(
                a("Yogurt Gloria fresa 1 kg", "8.90", 100, 12),
                a("Yogurt Laive vainilla 1 L", "7.50", 95, 12),
                a("Yogurt Griego Gloria natural 150 g", "3.90", 120, 15),
                a("Yogurt bebible Gloria fresa 1 L", "9.50", 85, 10),
                a("Yogurt con cereal Gloria 150 g", "4.20", 90, 10),
                a("Yogurt deslactosado Laive 1 kg", "9.90", 75, 8),
                a("Yogurt light Gloria durazno 1 kg", "8.50", 80, 10),
                a("Manjar blanco Gloria 250 g", "5.90", 70, 8),
                a("Mousse Gloria chocolate 80 g", "2.90", 110, 12),
                a("Yogurt probiótico Laive 1 kg", "10.90", 65, 8),
                a("Yogurt griego Laive 500 g", "12.90", 55, 6),
                a("Postre lácteo Sublime 80 g", "3.50", 95, 10)
        );
    }

    // 9 Quesos
    private static List<ArticuloSeed> quesos() {
        return List.of(
                a("Queso fresco Laive 500 g", "8.90", 90, 10),
                a("Queso edam Laive 400 g", "12.90", 75, 8),
                a("Queso parmesano Laive 100 g", "9.90", 60, 6),
                a("Queso mozzarella Laive 400 g", "11.50", 70, 8),
                a("Queso andino Laive 500 g", "10.90", 65, 8),
                a("Queso crema Laive 200 g", "7.90", 80, 10),
                a("Queso fundido Laive lonchas 144 g", "6.50", 85, 10),
                a("Mantequilla Laive 200 g", "8.50", 95, 12),
                a("Margarina Miraflores 250 g", "5.90", 100, 12),
                a("Queso paria Laive 500 g", "13.90", 50, 6),
                a("Queso cottage Laive 250 g", "7.50", 55, 6),
                a("Queso gouda Laive 400 g", "14.90", 45, 5)
        );
    }

    // 10 Huevos
    private static List<ArticuloSeed> huevos() {
        return List.of(
                a("Huevos San Fernando AA x12", "11.90", 150, 20),
                a("Huevos San Fernando A x30", "24.90", 80, 10),
                a("Huevos Don Kiko AA x12", "11.50", 140, 18),
                a("Huevos Don Kiko A x6", "5.90", 160, 20),
                a("Huevos orgánicos Granja del Sol x12", "16.90", 60, 8),
                a("Huevos de codorniz x12", "8.90", 70, 8),
                a("Margarina Miraflores 500 g", "9.90", 85, 10),
                a("Mermelada Gloria durazno 320 g", "6.90", 90, 10),
                a("Mermelada Gloria fresa 320 g", "6.90", 88, 10),
                a("Mantequilla de maní Manix 340 g", "9.50", 75, 8),
                a("Crema untable Laive 250 g", "7.90", 80, 10),
                a("Huevos San Fernando jumbo x12", "13.90", 100, 12)
        );
    }

    // 11 Embutidos
    private static List<ArticuloSeed> embutidos() {
        return List.of(
                a("Jamón pierna San Fernando 250 g", "8.90", 100, 12),
                a("Jamón pierna Sublime 200 g", "7.50", 95, 10),
                a("Salchicha San Fernando hot dog x6", "6.90", 110, 12),
                a("Salchicha parrillera San Fernando 500 g", "9.90", 85, 10),
                a("Mortadela San Fernando 250 g", "5.90", 90, 10),
                a("Chorizo San Fernando parrillero 500 g", "12.90", 70, 8),
                a("Tocino San Fernando 200 g", "10.90", 75, 8),
                a("Jamón serrano importado 100 g", "18.90", 45, 5),
                a("Salami San Fernando 200 g", "9.50", 65, 8),
                a("Pavo ahumado San Fernando 250 g", "11.90", 60, 6),
                a("Chorizo español San Fernando 300 g", "14.90", 50, 6),
                a("Salchicha frankfurter Sublime x10", "8.50", 80, 10)
        );
    }

    // 12 Panadería
    private static List<ArticuloSeed> panaderia() {
        return List.of(
                a("Pan francés Bimbo 600 g", "5.90", 120, 15),
                a("Pan de molde Bimbo integral 680 g", "8.90", 100, 12),
                a("Pan de molde Bimbo blanco 680 g", "7.90", 105, 12),
                a("Tostadas Bimbo 260 g", "6.50", 90, 10),
                a("Pan hot dog Bimbo x8", "5.50", 95, 10),
                a("Pan hamburguesa Bimbo x4", "5.90", 85, 10),
                a("Bizcocho Sublime vainilla 300 g", "4.90", 110, 12),
                a("Bizcocho Sublime chocolate 300 g", "4.90", 108, 12),
                a("Panetón Gloria mini 80 g", "3.50", 130, 15),
                a("Wafers Field vainilla 140 g", "3.90", 100, 12),
                a("Pan pita Bimbo x6", "6.90", 75, 8),
                a("Croissant Bimbo x4", "7.50", 70, 8)
        );
    }

    // 13 Gaseosas
    private static List<ArticuloSeed> gaseosas() {
        return List.of(
                a("Inca Kola 2.25 L", "7.90", 160, 20),
                a("Coca-Cola 2.25 L", "7.90", 155, 20),
                a("Coca-Cola Zero 2.25 L", "7.90", 140, 18),
                a("Sprite 2.25 L", "7.50", 130, 15),
                a("Fanta naranja 2.25 L", "7.50", 125, 15),
                a("Pepsi 2.25 L", "7.50", 120, 15),
                a("Kola Escocesa 2.25 L", "6.90", 100, 12),
                a("Inca Kola lata 355 ml x6", "14.90", 90, 10),
                a("Crush piña 2.25 L", "7.20", 85, 10),
                a("Guaraná Backus 2.25 L", "7.50", 80, 10),
                a("Inca Kola 500 ml", "2.50", 200, 25),
                a("Coca-Cola 500 ml", "2.50", 195, 25)
        );
    }

    // 14 Aguas
    private static List<ArticuloSeed> aguas() {
        return List.of(
                a("Agua San Luis sin gas 625 ml x6", "8.90", 150, 18),
                a("Agua San Luis con gas 625 ml x6", "8.90", 140, 18),
                a("Agua San Mateo sin gas 2.5 L", "3.50", 180, 22),
                a("Agua San Mateo con gas 2.5 L", "3.50", 170, 22),
                a("Agua Cielo sin gas 625 ml", "1.50", 200, 25),
                a("Agua Cielo con gas 625 ml", "1.50", 195, 25),
                a("Agua San Luis 7 L", "6.90", 100, 12),
                a("Soda San Luis 2.25 L", "4.50", 90, 10),
                a("Agua Benedictino sin gas 625 ml x6", "7.90", 85, 10),
                a("Agua mineral San Mateo 625 ml", "1.80", 160, 20),
                a("Agua saborizada San Luis limón 1.5 L", "3.90", 110, 12),
                a("Agua saborizada San Luis frutilla 1.5 L", "3.90", 105, 12)
        );
    }

    // 15 Jugos
    private static List<ArticuloSeed> jugos() {
        return List.of(
                a("Néctar Gloria durazno 1 L", "4.50", 130, 15),
                a("Néctar Gloria piña 1 L", "4.50", 125, 15),
                a("Néctar Gloria mango 1 L", "4.50", 120, 15),
                a("Jugo Pulp naranja 1 L", "5.90", 110, 12),
                a("Jugo Pulp piña 1 L", "5.90", 105, 12),
                a("Refresco Kola Inglesa 1.5 L", "4.90", 100, 12),
                a("Chicha Morada Don Manuel 1 L", "5.50", 95, 10),
                a("Néctar Gloria maracuyá 1 L", "4.90", 90, 10),
                a("Jugo de naranja natural Tampico 1 L", "6.50", 85, 10),
                a("Refresco de limón Sporade 500 ml", "2.90", 140, 15),
                a("Néctar Gloria guanábana 1 L", "4.90", 80, 10),
                a("Jugo de manzana Gloria 1 L", "5.20", 75, 8)
        );
    }

    // 16 Cervezas
    private static List<ArticuloSeed> cervezas() {
        return List.of(
                a("Cerveza Pilsen Callao 630 ml x6", "28.90", 80, 10),
                a("Cerveza Cristal 630 ml x6", "28.90", 78, 10),
                a("Cerveza Cusqueña dorada 630 ml x6", "32.90", 70, 8),
                a("Cerveza Cusqueña negra 630 ml x6", "32.90", 65, 8),
                a("Cerveza Arequipeña 630 ml x6", "26.90", 75, 10),
                a("Cerveza Corona 355 ml x6", "38.90", 55, 6),
                a("Cerveza Heineken 330 ml x6", "42.90", 50, 6),
                a("Cerveza Budweiser 355 ml x6", "36.90", 52, 6),
                a("Cerveza Pilsen lata 355 ml x6", "24.90", 85, 10),
                a("Cerveza Cristal lata 355 ml x6", "24.90", 82, 10),
                a("Cerveza Cusqueña trigo 630 ml x6", "34.90", 60, 8),
                a("Cerveza Pilsen sin alcohol 630 ml x6", "26.90", 45, 5)
        );
    }

    // 17 Vinos
    private static List<ArticuloSeed> vinos() {
        return List.of(
                a("Vino Tabernero Gran Tinto 750 ml", "18.90", 70, 8),
                a("Vino Tabernero Gran Blanco 750 ml", "18.90", 65, 8),
                a("Vino Tacama Gran Tinto 750 ml", "22.90", 60, 6),
                a("Vino Tacama Gran Blanco 750 ml", "22.90", 58, 6),
                a("Vino Vista Alegre tinto 750 ml", "15.90", 75, 8),
                a("Vino Vista Alegre blanco 750 ml", "15.90", 72, 8),
                a("Vino Intipalka Malbec 750 ml", "28.90", 45, 5),
                a("Vino Queirolo Cabernet 750 ml", "24.90", 50, 6),
                a("Espumante Tabernero demi-sec 750 ml", "26.90", 40, 5),
                a("Vino rosé Tabernero 750 ml", "19.90", 55, 6),
                a("Vino Santiaguina tinto 750 ml", "12.90", 80, 10),
                a("Vino San Felipe tinto 750 ml", "14.90", 68, 8)
        );
    }

    // 18 Licores
    private static List<ArticuloSeed> licores() {
        return List.of(
                a("Pisco Quebranta Tabernero 750 ml", "32.90", 55, 6),
                a("Pisco Acholado Portón 750 ml", "45.90", 40, 5),
                a("Pisco Italia Ocucaje 750 ml", "28.90", 50, 6),
                a("Ron Cartavio Black 750 ml", "38.90", 45, 5),
                a("Ron Cartavio Solera 750 ml", "42.90", 42, 5),
                a("Whisky Johnnie Walker Red 750 ml", "89.90", 30, 5),
                a("Whisky Old Parr 750 ml", "95.90", 28, 5),
                a("Vodka Smirnoff 750 ml", "45.90", 35, 5),
                a("Gin Beefeater 750 ml", "65.90", 25, 5),
                a("Tequila Olmeca 750 ml", "55.90", 30, 5),
                a("Licor de crema Baileys 750 ml", "72.90", 25, 5),
                a("Pisco mosto verde Ocucaje 750 ml", "35.90", 38, 5)
        );
    }

    // 19 Snacks
    private static List<ArticuloSeed> snacks() {
        return List.of(
                a("Papitas Lay's clásicas 140 g", "5.90", 150, 18),
                a("Papitas Lay's pollo 140 g", "5.90", 145, 18),
                a("Chizitos Inka Chips queso 140 g", "4.90", 130, 15),
                a("Camote frito Inka Chips 120 g", "5.50", 120, 12),
                a("Maní salado Costeño 200 g", "4.50", 140, 15),
                a("Maní japonés Costeño 150 g", "4.20", 135, 15),
                a("Chifles de plátano Costeño 150 g", "4.80", 125, 12),
                a("Papitas Pringles original 124 g", "9.90", 90, 10),
                a("Nachos Doritos queso 150 g", "6.50", 100, 12),
                a("Mix de frutos secos Costeño 200 g", "8.90", 85, 10),
                a("Habas tostadas Costeño 150 g", "3.90", 110, 12),
                a("Popcorn para microondas Act II 85 g", "3.50", 95, 10)
        );
    }

    // 20 Golosinas
    private static List<ArticuloSeed> golosinas() {
        return List.of(
                a("Chocolate Sublime clásico 38 g", "2.50", 200, 25),
                a("Chocolate Princesa 30 g", "1.80", 210, 25),
                a("Chocolate Bon o Bon 15 g x6", "5.90", 150, 18),
                a("Gomitas Mogul ositos 150 g", "4.90", 130, 15),
                a("Caramelos Ambrosoli surtidos 400 g", "6.90", 110, 12),
                a("Chupetín Ambrosoli 12 unidades", "3.50", 140, 15),
                a("Chocolate Kit Kat 41 g", "3.90", 160, 18),
                a("Chocolate Snickers 50 g", "4.20", 155, 18),
                a("Chocolate M&M's 49 g", "4.50", 145, 15),
                a("Turron de Doña Pepa 200 g", "5.90", 100, 12),
                a("Alfajor Sublime 6 unidades", "8.90", 90, 10),
                a("Chocolates surtidos Nestlé 180 g", "9.90", 85, 10)
        );
    }

    // 21 Galletas
    private static List<ArticuloSeed> galletas() {
        return List.of(
                a("Galletas Soda Field 180 g", "3.50", 160, 20),
                a("Galletas María Field 170 g", "3.20", 155, 18),
                a("Galletas Casino vainilla 120 g", "3.90", 140, 15),
                a("Galletas Casino chocolate 120 g", "3.90", 138, 15),
                a("Galletas Oreo original 108 g", "4.50", 130, 15),
                a("Galletas Ritz saladas 200 g", "5.90", 120, 12),
                a("Galletas Chocolates Field 150 g", "4.20", 125, 12),
                a("Galletas animalitos Field 200 g", "3.80", 115, 12),
                a("Cereal Corn Flakes Kellogg's 500 g", "14.90", 80, 10),
                a("Cereal Zucaritas Kellogg's 450 g", "15.90", 75, 8),
                a("Granola Costeño 400 g", "8.90", 90, 10),
                a("Galletas integrales Field 170 g", "4.50", 100, 10)
        );
    }

    // 22 Conservas
    private static List<ArticuloSeed> conservas() {
        return List.of(
                a("Atún Florida aceite 170 g", "6.90", 130, 15),
                a("Atún Florida agua 170 g", "6.50", 125, 15),
                a("Atún Gloria aceite 170 g", "6.90", 120, 15),
                a("Sardinas Florida en aceite 125 g", "4.50", 110, 12),
                a("Duraznos en almíbar Gloria 820 g", "8.90", 95, 10),
                a("Piña en rodajas Gloria 580 g", "7.50", 100, 10),
                a("Choclo en grano Gloria 300 g", "4.90", 105, 12),
                a("Arvejas Gloria 300 g", "4.50", 100, 10),
                a("Pasta de tomate Gloria 130 g", "2.90", 140, 15),
                a("Palmitos Gloria 400 g", "12.90", 60, 6),
                a("Champiñones Gloria 400 g", "8.50", 70, 8),
                a("Aceitunas verdes Gloria 200 g", "6.90", 75, 8)
        );
    }

    // 23 Frutas/verduras
    private static List<ArticuloSeed> frutasVerduras() {
        return List.of(
                a("Papa amarilla kg", "3.90", 200, 30),
                a("Papa blanca kg", "2.50", 200, 30),
                a("Cebolla roja kg", "3.20", 180, 25),
                a("Tomate kg", "4.50", 170, 25),
                a("Limón kg", "5.90", 150, 20),
                a("Plátano de seda kg", "3.80", 160, 22),
                a("Manzana roja kg", "8.90", 120, 15),
                a("Zanahoria kg", "2.90", 140, 18),
                a("Lechuga americana unidad", "2.50", 100, 15),
                a("Palta Hass kg", "12.90", 90, 12),
                a("Camote kg", "3.50", 130, 18),
                a("Ají limo kg", "6.90", 80, 10)
        );
    }

    // 24 Carnes
    private static List<ArticuloSeed> carnes() {
        return List.of(
                a("Carne molida de res kg", "32.90", 80, 10),
                a("Bistec de res kg", "38.90", 70, 8),
                a("Asado de tira kg", "35.90", 65, 8),
                a("Chuleta de cerdo kg", "28.90", 75, 10),
                a("Costilla de cerdo kg", "26.90", 70, 8),
                a("Lomo de cerdo kg", "30.90", 68, 8),
                a("Carne para guiso kg", "29.90", 72, 10),
                a("Anticuchos de res kg", "34.90", 55, 6),
                a("Hígado de res kg", "18.90", 60, 8),
                a("Bife angosto kg", "42.90", 50, 6),
                a("Pulpa de res kg", "36.90", 58, 6),
                a("Carne para seco kg", "33.90", 62, 8)
        );
    }

    // 25 Pollo
    private static List<ArticuloSeed> pollo() {
        return List.of(
                a("Pollo entero San Fernando kg", "12.90", 100, 12),
                a("Pechuga de pollo kg", "18.90", 90, 10),
                a("Muslo de pollo kg", "14.90", 95, 12),
                a("Alita de pollo kg", "13.50", 85, 10),
                a("Pollo sin menudencia kg", "13.90", 88, 10),
                a("Milanesa de pollo San Fernando 500 g", "15.90", 70, 8),
                a("Nuggets San Fernando 400 g", "12.90", 75, 8),
                a("Pavo entero kg", "16.90", 60, 8),
                a("Pechuga de pavo kg", "22.90", 50, 6),
                a("Pollo deshuesado kg", "19.90", 65, 8),
                a("Pollo troceado kg", "14.50", 80, 10),
                a("Hígado de pollo kg", "10.90", 55, 6)
        );
    }

    // 26 Pescados
    private static List<ArticuloSeed> pescados() {
        return List.of(
                a("Filete de bonito kg", "18.90", 70, 8),
                a("Filete de merluza kg", "22.90", 65, 8),
                a("Jurel entero kg", "12.90", 75, 10),
                a("Caballa entera kg", "14.90", 70, 8),
                a("Trucha fresca kg", "28.90", 50, 6),
                a("Camarones kg", "45.90", 40, 5),
                a("Pulpo kg", "52.90", 35, 5),
                a("Conchas de abanico kg", "38.90", 45, 5),
                a("Atún fresco kg", "32.90", 48, 6),
                a("Pescado congelado surtido 500 g", "15.90", 80, 10),
                a("Langostinos congelados 400 g", "24.90", 55, 6),
                a("Mejillones kg", "16.90", 60, 8)
        );
    }

    // 27 Congelados
    private static List<ArticuloSeed> congelados() {
        return List.of(
                a("Nuggets San Fernando 400 g", "12.90", 90, 10),
                a("Papa prefrita McCain 600 g", "9.90", 100, 12),
                a("Pizza congelada Razzeto familiar", "18.90", 70, 8),
                a("Vegetales mixtos congelados 500 g", "8.90", 85, 10),
                a("Hamburguesas San Fernando x4", "14.90", 75, 8),
                a("Empanadas de pollo congeladas x6", "16.90", 65, 8),
                a("Papa entera congelada 1 kg", "7.90", 95, 10),
                a("Brocoli congelado 500 g", "8.50", 80, 10),
                a("Arvejas congeladas 500 g", "6.90", 88, 10),
                a("Choclo congelado 500 g", "7.50", 82, 10),
                a("Tequeños congelados x12", "15.90", 60, 6),
                a("Pizza personal Razzeto 350 g", "9.90", 78, 8)
        );
    }

    // 28 Helados
    private static List<ArticuloSeed> helados() {
        return List.of(
                a("Helado D'Onofrio vainilla 1 L", "12.90", 80, 10),
                a("Helado D'Onofrio chocolate 1 L", "12.90", 78, 10),
                a("Helado Holanda fresa 900 ml", "11.90", 75, 10),
                a("Helado Holanda menta 900 ml", "11.90", 72, 10),
                a("Paleta Donofrio triple 90 ml", "3.50", 150, 18),
                a("Paleta Holanda frutilla 80 ml", "2.90", 145, 18),
                a("Helado Magnum clásico 90 ml", "6.90", 100, 12),
                a("Helado Cornetto vainilla 120 ml", "4.50", 110, 12),
                a("Helado D'Onofrio lucuma 1 L", "13.90", 65, 8),
                a("Helado Holanda cookies 900 ml", "12.90", 60, 8),
                a("Helado D'Onofrio family pack 2 L", "22.90", 50, 6),
                a("Helado Holanda mini paletas x6", "14.90", 55, 6)
        );
    }

    // 29 Limpieza
    private static List<ArticuloSeed> limpieza() {
        return List.of(
                a("Detergente Ariel polvo 750 g", "12.90", 100, 12),
                a("Detergente Omo líquido 800 ml", "14.90", 90, 10),
                a("Suavizante Downy 900 ml", "11.90", 85, 10),
                a("Cloro Clorox 1 L", "5.90", 120, 15),
                a("Desinfectante Sapolio 900 ml", "8.90", 95, 12),
                a("Limpiador Cif crema 750 g", "7.50", 88, 10),
                a("Lavaloza Sapolio limón 750 ml", "6.90", 100, 12),
                a("Desengrasante Mr Músculo 500 ml", "9.90", 80, 10),
                a("Papel higiénico Elite doble hoja x4", "8.90", 110, 12),
                a("Servilletas Elite x100", "4.50", 130, 15),
                a("Bolsa de basura Rey 10 unidades", "5.90", 105, 12),
                a("Esponja Scotch-Brite x3", "6.50", 95, 10)
        );
    }

    // 30 Higiene
    private static List<ArticuloSeed> higiene() {
        return List.of(
                a("Shampoo Head & Shoulders 375 ml", "18.90", 85, 10),
                a("Shampoo Pantene 400 ml", "16.90", 80, 10),
                a("Acondicionador Sedal 340 ml", "12.90", 75, 8),
                a("Jabón Protex 110 g x3", "9.90", 100, 12),
                a("Pasta dental Colgate 150 ml", "8.90", 110, 12),
                a("Cepillo dental Colgate", "5.90", 120, 15),
                a("Desodorante Rexona 150 ml", "12.90", 90, 10),
                a("Jabón líquido Dove 220 ml", "11.90", 85, 10),
                a("Papel higiénico Suave doble hoja x4", "9.50", 95, 12),
                a("Toallas femeninas Always x10", "8.90", 88, 10),
                a("Crema dental Oral-B 140 g", "9.50", 92, 10),
                a("Gel de baño Nivea 250 ml", "14.90", 70, 8)
        );
    }

    // 31 Bebé
    private static List<ArticuloSeed> bebe() {
        return List.of(
                a("Pañales Huggies active sec T3 x30", "39.90", 70, 8),
                a("Pañales Huggies active sec T4 x28", "42.90", 65, 8),
                a("Pañales Babysec premium T3 x30", "35.90", 72, 8),
                a("Toallitas húmedas Huggies x80", "12.90", 90, 10),
                a("Fórmula NAN 1 400 g", "45.90", 50, 6),
                a("Fórmula Nido 1+ 400 g", "32.90", 55, 6),
                a("Fórmula Similac 1 400 g", "48.90", 45, 5),
                a("Papilla Nestum arroz 200 g", "8.90", 80, 10),
                a("Papilla Nestum 5 cereales 200 g", "8.90", 78, 10),
                a("Shampoo Johnson's bebé 200 ml", "14.90", 75, 8),
                a("Jabón Johnson's bebé 75 g", "4.90", 85, 10),
                a("Crema Johnson's bebé 200 ml", "16.90", 65, 8)
        );
    }

    // 32 Mascotas
    private static List<ArticuloSeed> mascotas() {
        return List.of(
                a("Alimento Pedigree adulto 1 kg", "12.90", 90, 10),
                a("Alimento Pedigree cachorro 1 kg", "13.90", 85, 10),
                a("Alimento Dog Chow adulto 1 kg", "11.90", 88, 10),
                a("Alimento Cat Chow adulto 1 kg", "14.90", 80, 10),
                a("Alimento Whiskas adulto 1 kg", "15.90", 78, 10),
                a("Alimento Felix gato 1 kg", "13.50", 82, 10),
                a("Alimento Ganador perro 1 kg", "10.90", 92, 10),
                a("Snacks Pedigree dentastix x7", "9.90", 75, 8),
                a("Arena sanitaria gato 4 kg", "18.90", 65, 8),
                a("Alimento Pro Plan perro 1 kg", "22.90", 55, 6),
                a("Alimento Pro Plan gato 1 kg", "24.90", 50, 6),
                a("Hueso masticable perro", "6.90", 80, 10)
        );
    }

    // 33 Desechables
    private static List<ArticuloSeed> desechables() {
        return List.of(
                a("Vasos desechables Rey 50 unidades", "6.90", 100, 12),
                a("Platos desechables Rey 20 unidades", "8.90", 90, 10),
                a("Cubiertos desechables Rey 24 sets", "7.90", 85, 10),
                a("Servilletas Elite 200 unidades", "7.50", 95, 12),
                a("Bolsas de basura Rey 20 unidades", "9.90", 88, 10),
                a("Papel aluminio Rey 7.5 m", "8.50", 80, 10),
                a("Film plástico Rey 30 m", "7.90", 82, 10),
                a("Papel encerado Rey 5 m", "5.90", 75, 8),
                a("Pitillos Rey 100 unidades", "3.50", 110, 12),
                a("Bandejas desechables Rey x10", "6.50", 78, 8),
                a("Mantel desechable Rey", "4.90", 70, 8),
                a("Bolsas ziploc Rey 20 unidades", "9.50", 72, 8)
        );
    }

    // 34 Salud
    private static List<ArticuloSeed> salud() {
        return List.of(
                a("Paracetamol Genfar 500 mg x20", "4.90", 100, 12),
                a("Ibuprofeno Genfar 400 mg x20", "5.90", 95, 12),
                a("Antigripal Tapsin x10", "8.90", 85, 10),
                a("Vitamina C Redoxon x10", "12.90", 80, 10),
                a("Alcohol medicinal 500 ml", "6.90", 90, 10),
                a("Agua oxigenada 120 ml", "3.50", 95, 12),
                a("Curitas Band-Aid x20", "7.90", 88, 10),
                a("Gasa estéril 10 unidades", "5.50", 85, 10),
                a("Suero oral Electrolit 625 ml", "4.90", 92, 10),
                a("Multivitamínico Centrum x30", "45.90", 45, 5),
                a("Jarabe para tos Bisolvon 120 ml", "18.90", 55, 6),
                a("Protector solar Nivea FPS50 200 ml", "39.90", 50, 6)
        );
    }

    // 35 Electro hogar
    private static List<ArticuloSeed> electroHogar() {
        return List.of(
                a("Pilas Duracell AA x4", "12.90", 100, 12),
                a("Pilas Duracell AAA x4", "11.90", 95, 12),
                a("Foco LED Osram 9W", "8.90", 110, 12),
                a("Foco LED Philips 12W", "9.90", 105, 12),
                a("Extension eléctrica 3 metros", "18.90", 70, 8),
                a("Extension eléctrica 5 metros", "24.90", 60, 8),
                a("Adaptador enchufe doble", "6.90", 85, 10),
                a("Cinta aislante 3M 10 m", "4.90", 90, 10),
                a("Pilas recargables Energizer x4", "28.90", 45, 5),
                a("Linterna LED básica", "15.90", 55, 6),
                a("Foco ahorrador 15W", "7.90", 98, 10),
                a("Protector de voltaje 4 tomas", "32.90", 40, 5)
        );
    }
}
