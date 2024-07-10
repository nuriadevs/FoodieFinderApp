package com.example.foodiefinder.data.model

data class Recipe(
    val _links: Links,
    val count: Int,
    val from: Int,
    val hits: List<Hit>,
    val to: Int
)