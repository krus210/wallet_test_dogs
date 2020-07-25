package ru.korolevss.wallettestdogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korolevss.wallettestdogs.R
import ru.korolevss.wallettestdogs.model.BreedForList

class BreedAdapter(var list: List<BreedForList>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var btnClickListener: OnBtnClickListener? = null

    interface OnBtnClickListener {
        fun onBtnClicked(breed: BreedForList)
    }

    fun updatePosts(newData: List<BreedForList>) {
        this.list = newData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.breed_card, parent, false)
        return BreedViewHolder(this, view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val breed = list[position]
        with(holder as BreedViewHolder) {
            bind(breed)
        }
    }

}