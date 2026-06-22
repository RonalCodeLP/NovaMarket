-- Precio, inventario y código de barras (alineado con entidad Articulo)
ALTER TABLE articulos ADD COLUMN IF NOT EXISTS precio DECIMAL(12,2) NOT NULL DEFAULT 0;
ALTER TABLE articulos ADD COLUMN IF NOT EXISTS stock INTEGER NOT NULL DEFAULT 0;
ALTER TABLE articulos ADD COLUMN IF NOT EXISTS stock_minimo INTEGER NOT NULL DEFAULT 5;
ALTER TABLE articulos ADD COLUMN IF NOT EXISTS codigo_barras VARCHAR(50);
ALTER TABLE articulos ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(500);

CREATE UNIQUE INDEX IF NOT EXISTS ux_articulos_codigo_barras
    ON articulos (codigo_barras)
    WHERE codigo_barras IS NOT NULL;
