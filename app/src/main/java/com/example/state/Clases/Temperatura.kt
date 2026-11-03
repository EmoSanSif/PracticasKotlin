package com.example.state.Clases

class Temperatura(var celsius: Double) {
    var farenheit: Double

    init {
        require(celsius >= -273.15) { "La temperatura no puede ser menor a -273.15 grados "}
        farenheit = (celsius * 9/5) + 32
        println("Temperatura en Celsius: $celsius Temperatura en farenheit: $farenheit" )
    }

}