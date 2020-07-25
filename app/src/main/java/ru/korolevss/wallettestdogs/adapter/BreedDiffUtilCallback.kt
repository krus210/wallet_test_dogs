package ru.korolevss.wallettestdogs.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.korolevss.wallettestdogs.model.BreedForList

class BreedDiffUtilCallback(
    private val oldList: List<BreedForList>,
    private val newList: List<BreedForList>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.breedName == newModel.breedName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.subBreeds.size == newModel.subBreeds.size
                && oldModel.countOfPhotos == newModel.countOfPhotos
    }

}