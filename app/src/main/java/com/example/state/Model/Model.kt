package com.example.state.Model

// 1. Modelo de cada producto del catálogo
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
)

// 2. Estado completo del carrito de compras
data class CarritoUiState(
    val productos: List<Producto> = listOf(
        Producto(1, "Tortilla 1 kg", 24.00),
        Producto(2, "Leche 1 L", 27.50),
        Producto(3, "Huevo 12 pz", 52.00),
        Producto(4, "Frijol 1 kg", 38.00)
    ),
    // Mapa que guarda <idProducto, cantidadSeleccionada> (inicia vacío en 0 para todos)
    val cantidades: Map<Int, Int> = emptyMap()
) {
    // Función auxiliar para consultar la cantidad de un producto específico
    fun cantidadDe(idProducto: Int): Int = cantidades[idProducto] ?: 0

    // 3. Cálculos derivados (Reglas de negocio puras)
    val totalArticulos: Int
        get() = cantidades.values.sum()

    val carritoVacio: Boolean
        get() = totalArticulos == 0

    val subtotal: Double
        get() = productos.sumOf { producto ->
            producto.precio * cantidadDe(producto.id)
        }

    // Regla 1: 10% de descuento si el subtotal es de $200 o más
    val aplicaDescuento: Boolean
        get() = subtotal >= 200.0

    val descuento: Double
        get() = if (aplicaDescuento) subtotal * 0.10 else 0.0

    val subtotalConDescuento: Double
        get() = subtotal - descuento

    // Regla 2: Envío gratis si el subtotal (ya con descuento) es >= $150; $0 si está vacío; si no, $25
    val envioGratis: Boolean
        get() = !carritoVacio && subtotalConDescuento >= 150.0

    val costoEnvio: Double
        get() = when {
            carritoVacio -> 0.0
            envioGratis -> 0.0
            else -> 25.0
        }

    // Total final a pagar
    val totalFinal: Double
        get() = subtotalConDescuento + costoEnvio
}