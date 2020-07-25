package ru.korolevss.wallettestdogs.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.korolevss.wallettestdogs.model.Breed
import ru.korolevss.wallettestdogs.model.BreedImage

@Database(entities = [BreedImage::class, Breed::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getBreedImageDao(): BreedImageDao
    abstract fun getBreedDao(): BreedDao
}