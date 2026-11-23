package com.example.state.Model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
)

data class CarritoUiState(
    val productos: List<Producto> = listOf(
        Producto(1, "Tortilla 1 kg", 24.00),
        Producto(2, "Leche 1 L", 27.50),
        Producto(3, "Huevo 12 pz", 52.00),
        Producto(4, "Frijol 1 kg", 38.00)
    ),
    // Mapa que guarda <idProducto, cantidadSeleccionada> (inicia vacío en 0 para todos) [Le entiendo a medias]
    val cantidades: Map<Int, Int> = emptyMap()
) {
    fun cantidadDe(idProducto: Int): Int = cantidades[idProducto] ?: 0

    val totalArticulos: Int
        get() = cantidades.values.sum()

    val carritoVacio: Boolean
        get() = totalArticulos == 0

    val subtotal: Double
        get() = productos.sumOf { producto ->
            producto.precio * cantidadDe(producto.id)
        }

    val aplicaDescuento: Boolean
        get() = subtotal >= 200.0

    val descuento: Double
        get() = if (aplicaDescuento) subtotal * 0.10 else 0.0

    val subtotalConDescuento: Double
        get() = subtotal - descuento

    val envioGratis: Boolean
        get() = !carritoVacio && subtotalConDescuento >= 150.0

    val costoEnvio: Double
        get() = when {
            carritoVacio -> 0.0
            envioGratis -> 0.0
            else -> 25.0
        }

    val totalFinal: Double
        get() = subtotalConDescuento + costoEnvio
}