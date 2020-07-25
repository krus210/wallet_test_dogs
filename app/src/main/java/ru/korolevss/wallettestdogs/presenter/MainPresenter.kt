package ru.korolevss.wallettestdogs.presenter

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.korolevss.wallettestdogs.R
import ru.korolevss.wallettestdogs.contract.MainContract
import ru.korolevss.wallettestdogs.exception.GetAllBreedsException
import ru.korolevss.wallettestdogs.model.BreedForList
import ru.korolevss.wallettestdogs.repository.MainRepository
import java.io.IOException
import java.net.SocketTimeoutException

class MainPresenter(
    context: Context,
    private val mainView: MainContract.View,
    private val mainRepository: MainContract.Repository = MainRepository(context)
) : MainContract.Presenter {

    private val scope by lazy { CoroutineScope(Dispatchers.Main) }

    override fun onCreateAllBreeds() {
        scope.launch {
            try {
                mainView.showLoading(true)
                val breeds = mainRepository.getAllBreeds()
                if (breeds.status != "success") {
                    throw GetAllBreedsException()
                } else {
                    val listOfBreeds = mutableListOf<BreedForList>()
                    breeds.message.keys.forEach {
                        val breedForList = BreedForList(
                            it,
                            breeds.message[it] ?: emptyList(),
                            0
                        )
                        listOfBreeds.add(breedForList)
                    }
                    mainView.createList(listOfBreeds)
                }
            } catch (e: GetAllBreedsException) {
                mainView.showError(R.string.fail_loading_from_server)
                mainView.createList(emptyList())
            } catch (e: IOException) {
                mainView.showError(R.string.fail_connecting_to_server)
                mainView.createList(emptyList())
            } finally {
                mainView.showLoading(false)
            }
        }
    }

    override fun onRefreshClicked() {
        scope.launch {
            try {
                val breeds = mainRepository.getAllBreeds()
                if (breeds.status != "success") {
                    throw GetAllBreedsException()
                } else {
                    val listOfBreeds = mutableListOf<BreedForList>()
                    breeds.message.keys.forEach {
                        val breedForList = BreedForList(
                            it,
                            breeds.message[it] ?: emptyList(),
                            0
                        )
                        listOfBreeds.add(breedForList)
                    }
                    mainView.createList(listOfBreeds)
                }
            } catch (e: GetAllBreedsException) {
                mainView.showError(R.string.fail_loading_from_server)
            } catch (e: IOException) {
                mainView.showError(R.string.fail_connecting_to_server)
            } finally {
                mainView.stopRefreshing()
            }
        }
    }

    override fun onCreateSubBreeds(subBreeds: Array<String>) {
        val listOfBreeds = mutableListOf<BreedForList>()
        subBreeds.forEach {
            val breedForList = BreedForList(
                it,
                emptyList(),
                0
            )
            listOfBreeds.add(breedForList)
        }
        mainView.createList(listOfBreeds)
    }

    override fun onCreateFavoriteBreeds() {
        scope.launch {
            try {
                mainView.showLoading(true)

                val breeds = async{
                    mainRepository.getFavorites()
                }
                if (breeds.await() == null) {
                    mainView.createList(emptyList())
                } else {
                    val breedsForList = mutableListOf<BreedForList>()
                    breeds.await()!!.forEach {
                        breedsForList.add(
                            BreedForList(
                                it.breedName,
                                emptyList(),
                                it.countOfLikes
                            )
                        )
                    }
                    mainView.createList(breedsForList)
                }
            } catch (e: IOException) {
                mainView.showError(R.string.fail_work_with_data)
            } finally {
                mainView.showLoading(false)
            }
        }
    }

}