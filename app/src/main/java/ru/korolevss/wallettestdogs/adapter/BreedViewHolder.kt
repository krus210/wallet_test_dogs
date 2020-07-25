package ru.korolevss.wallettestdogs.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.breed_card.view.*
import ru.korolevss.wallettestdogs.R
import ru.korolevss.wallettestdogs.model.BreedForList

class BreedViewHolder(private val adapter: BreedAdapter, private val view: View) :
    RecyclerView.ViewHolder(view) {

    init {
        view.buttonNext.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val breed = adapter.list[adapterPosition]
                adapter.btnClickListener?.onBtnClicked(breed)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(breed: BreedForList) {
        with (view) {
            textViewBreed.text = breed.breedName
            textViewSnippet.text = when {
                breed.subBreeds.isNotEmpty() -> {
                    "(${breed.subBreeds.size} ${context.getString(R.string.subbreeds)})"
                }
                breed.countOfPhotos > 0 -> {
                    "(${breed.countOfPhotos} ${context.getString(R.string.photos)})"
                }
                else -> ""
            }
        }
    }

}
