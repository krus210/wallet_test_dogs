package ru.korolevss.wallettestdogs.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Breed(
    @PrimaryKey
    val breedName: String,
    val countOfLikes: Int
)