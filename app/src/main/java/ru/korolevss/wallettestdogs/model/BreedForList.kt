package ru.korolevss.wallettestdogs.model

data class BreedForList(
    val breedName: String,
    val subBreeds: List<String>,
    val countOfPhotos: Int
)
