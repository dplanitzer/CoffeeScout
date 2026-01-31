package com.example.coffeescout.repository

sealed class BusinessAddress {
    data class Address(val data: String) : BusinessAddress()
    data class Location(val latitude: Double, val longitude: Double) : BusinessAddress()
}
