package ru.korolevss.wallettestdogs.repository

import androidx.room.*
import ru.korolevss.wallettestdogs.model.Breed
import ru.korolevss.wallettestdogs.model.BreedImage

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreed(breed: Breed)

    @Query("SELECT * FROM breed")
    suspend fun getAllBreed(): List<Breed>?

    @Query("SELECT * FROM breed WHERE breedName = :breedName")
    suspend fun getBreed(breedName: String): Breed?

    @Delete
    suspend fun deleteBreed(breed: Breed)

}