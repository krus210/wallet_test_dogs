package ru.korolevss.wallettestdogs.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreedImage(
    val breedName: String,
    @PrimaryKey
    val image: String,
    val isLiked: Int = 0
)