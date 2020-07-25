package ru.korolevss.wallettestdogs.contract

import ru.korolevss.wallettestdogs.model.Breed
import ru.korolevss.wallettestdogs.model.BreedForList
import ru.korolevss.wallettestdogs.model.Breeds

interface MainContract {
    interface View {
        fun showError(message: Int)
        fun createList(breeds: List<BreedForList>)
        fun updateList(breeds: List<BreedForList>)
        fun showLoading(isVisible: Boolean)
        fun stopRefreshing()
    }

    interface Presenter {
        fun onCreateAllBreeds()
        fun onCreateFavoriteBreeds()
        fun onCreateSubBreeds(subBreeds: Array<String>)
        fun onRefreshClicked()
    }

    interface Repository {
        suspend fun getAllBreeds(): Breeds
        suspend fun getFavorites(): List<Breed>?
    }
}