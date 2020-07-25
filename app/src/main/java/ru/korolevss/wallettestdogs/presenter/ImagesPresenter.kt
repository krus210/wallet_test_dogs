package ru.korolevss.wallettestdogs.presenter

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.korolevss.wallettestdogs.R
import ru.korolevss.wallettestdogs.contract.ImagesContract
import ru.korolevss.wallettestdogs.exception.GetAllBreedsException
import ru.korolevss.wallettestdogs.exception.GetImagesByBreedException
import ru.korolevss.wallettestdogs.model.Breed
import ru.korolevss.wallettestdogs.model.BreedForList
import ru.korolevss.wallettestdogs.model.BreedImage
import ru.korolevss.wallettestdogs.repository.MainRepository
import java.io.IOException

class ImagesPresenter(
    private val imagesView: ImagesContract.View,
    private val imagesRepository: ImagesContract.Repository = MainRepository(imagesView as Context)
):ImagesContract.Presenter {

    private val scope by lazy { CoroutineScope(Dispatchers.Main) }

    override fun onCreateAllImages(breedName: String) {
        scope.launch {
            try {
                imagesView.showLoading(true)
                val images = imagesRepository.getImagesByBreed(breedName)
                if (images.status != "success") {
                    throw GetImagesByBreedException()
                } else {
                    val listOfBreedImages = mutableListOf<BreedImage>()
                    images.message.forEach {
                        listOfBreedImages.add(
                            BreedImage(
                                breedName,
                                it
                            )
                        )
                    }
                    imagesView.createImages(listOfBreedImages)
                }
            } catch (e: GetImagesByBreedException) {
                imagesView.showError(R.string.fail_loading_from_server)
                imagesView.createImages(emptyList())
            } catch (e: IOException) {
                imagesView.showError(R.string.fail_connecting_to_server)
                imagesView.createImages(emptyList())
            } finally {
                imagesView.showLoading(false)
            }
        }
    }

    override fun onCreateFavoriteImages(breedName: String) {
        scope.launch {
            try {
                imagesView.showLoading(true)

                val breedImages = async{
                    imagesRepository.getFavoriteImagesByBreed(breedName)
                }
                if (breedImages.await() == null) {
                    imagesView.createImages(emptyList())
                } else {
                    imagesView.createImages(breedImages.await()!!)
                }
            } catch (e: IOException) {
                imagesView.showError(R.string.fail_work_with_data)
            } finally {
                imagesView.showLoading(false)
            }
        }
    }

    override fun onCreateSubbreedImages(breedName: String, subbreedName: String) {
        scope.launch {
            try {
                imagesView.showLoading(true)
                val images = imagesRepository.getImagesBySubBreed(breedName, subbreedName)
                if (images.status != "success") {
                    throw GetImagesByBreedException()
                } else {
                    val listOfBreedImages = mutableListOf<BreedImage>()
                    images.message.forEach {
                        listOfBreedImages.add(
                            BreedImage(
                                subbreedName,
                                it
                            )
                        )
                    }
                    imagesView.createImages(listOfBreedImages)
                }
            } catch (e: GetImagesByBreedException) {
                imagesView.showError(R.string.fail_loading_from_server)
                imagesView.createImages(emptyList())
            } catch (e: IOException) {
                imagesView.showError(R.string.fail_connecting_to_server)
                imagesView.createImages(emptyList())
            } finally {
                imagesView.showLoading(false)
            }
        }
    }

    override fun onLikeClicked(breedImage: BreedImage, position: Int) {
        scope.launch {
            try {
                val likedBreedImage = breedImage.copy(isLiked = 1)
                val breed = async(Dispatchers.IO) {
                    imagesRepository.getBreed(breedImage.breedName)
                }
                if (breed.await() == null) {
                    launch(Dispatchers.IO) {
                        imagesRepository.saveBreed(Breed(breedImage.breedName, 1))
                        imagesRepository.saveBreedImage(likedBreedImage)
                    }
                } else {
                    launch(Dispatchers.IO) {
                        val countOfLikes = breed.await()!!.countOfLikes + 1
                        imagesRepository.saveBreed(Breed(breedImage.breedName, countOfLikes))
                        imagesRepository.saveBreedImage(likedBreedImage)
                    }
                }
                imagesView.fillOrOutlineLikeBtn(likedBreedImage, position)
            } catch (e: IOException) {
                imagesView.showError(R.string.fail_work_with_data)
            }
        }
    }

    override fun onUnlikeClicked(breedImage: BreedImage, position: Int) {
        scope.launch {
            try {
                val unLikedBreedImage = breedImage.copy(isLiked = 0)
                val breed = async(Dispatchers.IO) {
                    imagesRepository.getBreed(breedImage.breedName)
                }
                if (breed.await()!!.countOfLikes == 1) {
                    launch(Dispatchers.IO) {
                        imagesRepository.deleteBreed(breed.await()!!)
                        imagesRepository.deleteBreedImage(unLikedBreedImage)
                    }
                } else {
                    launch(Dispatchers.IO) {
                        val countOfLikes = breed.await()!!.countOfLikes - 1
                        imagesRepository.saveBreed(Breed(breedImage.breedName, countOfLikes))
                        imagesRepository.deleteBreedImage(unLikedBreedImage)
                    }
                }
                imagesView.fillOrOutlineLikeBtn(unLikedBreedImage, position)
            } catch (e: IOException) {
                imagesView.showError(R.string.fail_work_with_data)
            }
        }
    }
}