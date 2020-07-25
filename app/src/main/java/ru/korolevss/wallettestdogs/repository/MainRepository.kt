package ru.korolevss.wallettestdogs.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.korolevss.wallettestdogs.contract.ImagesContract
import ru.korolevss.wallettestdogs.contract.MainContract
import ru.korolevss.wallettestdogs.exception.GetAllBreedsException
import ru.korolevss.wallettestdogs.exception.GetImagesByBreedException
import ru.korolevss.wallettestdogs.model.Breed
import ru.korolevss.wallettestdogs.model.BreedImage
import ru.korolevss.wallettestdogs.model.Breeds
import ru.korolevss.wallettestdogs.model.Images

private const val BASE_URL = "https://dog.ceo/"

class MainRepository(context: Context) : MainContract.Repository, ImagesContract.Repository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

        }
    }

    private val database = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
        .addMigrations(MIGRATION_1_2)
        .build()

    private val api: API by lazy { retrofit.create(API::class.java) }

    override suspend fun getAllBreeds(): Breeds {
        val result = api.getAllBreeds()
        if (result.isSuccessful) {
            return result.body()!!
        } else throw GetAllBreedsException()
    }

    override suspend fun getFavorites() = database.getBreedDao().getAllBreed()

    override suspend fun getImagesByBreed(breedName: String): Images {
        val result = api.getImagesByBreed(breedName)
        if (result.isSuccessful) {
            return result.body()!!
        } else throw GetImagesByBreedException()
    }

    override suspend fun getImagesBySubBreed(breedName: String, subbreedName: String): Images {
        val result = api.getImagesBySubbreed(breedName, subbreedName)
        if (result.isSuccessful) {
            return result.body()!!
        } else throw GetImagesByBreedException()
    }

    override suspend fun saveBreed(breed: Breed) = database.getBreedDao().insertBreed(breed)

    override suspend fun getBreed(breedName: String) = database.getBreedDao().getBreed(breedName)

    override suspend fun deleteBreed(breed: Breed) = database.getBreedDao().deleteBreed(breed)

    override suspend fun saveBreedImage(breedImage: BreedImage) =
        database.getBreedImageDao().insertBreedImage(breedImage)


    override suspend fun deleteBreedImage(breedImage: BreedImage) =
        database.getBreedImageDao().deleteBreedImage(breedImage)

    override suspend fun getFavoriteImagesByBreed(breedName: String) =
        database.getBreedImageDao().getBreedImagesForBreed(breedName)
}