package ru.korolevss.wallettestdogs.repository

import androidx.room.*
import ru.korolevss.wallettestdogs.model.BreedImage

@Dao
interface BreedImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreedImage(breedImage: BreedImage)

    @Query("SELECT * FROM breedimage WHERE breedName = :breedName")
    suspend fun getBreedImagesForBreed(breedName: String): List<BreedImage>?

    @Delete
    suspend fun deleteBreedImage(breedImage: BreedImage)

}