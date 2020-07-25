package ru.korolevss.wallettestdogs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_page.view.*
import ru.korolevss.wallettestdogs.R
import ru.korolevss.wallettestdogs.model.BreedImage

class ViewPagerAdapter(val list: MutableList<BreedImage>) : RecyclerView.Adapter<PagerVH>() {

    var likeClickListener: OnLikeClickListener? = null

    interface OnLikeClickListener {
        fun onLikeClicked(breedImage: BreedImage, position: Int)
    }

    fun changeItem(breedImage: BreedImage, position: Int) {
        list[position] = breedImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false),
            this
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        holder.bind(list[position])
    }
}

class PagerVH(itemView: View, adapter: ViewPagerAdapter) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.buttonLike.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val breedImage = adapter.list[adapterPosition]
                adapter.likeClickListener?.onLikeClicked(breedImage, adapterPosition)
            }
        }
    }

    fun bind(breedImage: BreedImage) {
        with(itemView) {
            if (breedImage.isLiked == 1) {
                buttonLike.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                buttonLike.setImageResource(R.drawable.ic_favorite_outlined)
            }
            Glide.with(this)
                .load(breedImage.image)
                .placeholder(R.drawable.ic_baseline_photo_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(imageViewSlider)
        }
    }
}