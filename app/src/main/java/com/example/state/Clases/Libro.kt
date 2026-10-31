package com.example.state.Clases

class Libro(val titulo: String, val autor: String, val N_paginas: Int) {

    fun describir(): Libro {
        return this
    }
}