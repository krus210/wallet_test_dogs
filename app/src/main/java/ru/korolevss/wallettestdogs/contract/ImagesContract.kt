package ru.korolevss.wallettestdogs.contract

import ru.korolevss.wallettestdogs.model.*

interface ImagesContract {
    interface View {
        fun showError(message: Int)
        fun createImages(images: List<BreedImage>)
        fun showLoading(isVisible: Boolean)
        fun fillOrOutlineLikeBtn(breedImage: BreedImage, position: Int)
    }

    interface Presenter {
        fun onCreateAllImages(breedName: String)
        fun onCreateSubbreedImages(breedName: String, subbreedName: String)
        fun onCreateFavoriteImages(breedName: String)
        fun onLikeClicked(breedImage: BreedImage, position: Int)
        fun onUnlikeClicked(breedImage: BreedImage, position: Int)
    }

    interface Repository {
        suspend fun getImagesByBreed(breedName: String): Images
        suspend fun getImagesBySubBreed(breedName: String, subbreedName: String): Images
        suspend fun getFavoriteImagesByBreed(breedName: String): List<BreedImage>?
        suspend fun saveBreed(breed: Breed)
        suspend fun deleteBreed(breed: Breed)
        suspend fun getBreed(breedName: String): Breed?
        suspend fun saveBreedImage(breedImage: BreedImage)
        suspend fun deleteBreedImage(breedImage: BreedImage)
    }
}