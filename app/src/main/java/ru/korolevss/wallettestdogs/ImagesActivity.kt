package ru.korolevss.wallettestdogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.fragment_breed.*
import kotlinx.android.synthetic.main.item_fail.*
import kotlinx.android.synthetic.main.item_share.*
import ru.korolevss.wallettestdogs.adapter.ViewPagerAdapter
import ru.korolevss.wallettestdogs.contract.ImagesContract
import ru.korolevss.wallettestdogs.model.BreedImage
import ru.korolevss.wallettestdogs.presenter.ImagesPresenter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val BREED = "breed"
private const val SUBBREED = "subbreed"
private const val IS_FAVORITES = "is_favorites"

class ImagesActivity : AppCompatActivity(), ImagesContract.View,
    ViewPagerAdapter.OnLikeClickListener {

    private val imagesPresenter by lazy { ImagesPresenter(this) }
    private val breed by lazy { intent.getStringExtra(BREED) ?: "" }
    private val subbreed by lazy { intent.getStringExtra(SUBBREED) ?: "" }
    private val isFavorite by lazy { intent.getBooleanExtra(IS_FAVORITES, false) }
    private var currentImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_images_layout)
        val view = supportActionBar?.customView

        val title: TextView = view!!.findViewById(R.id.textViewTitleImages)
        val backButton: ImageView = view.findViewById(R.id.imageViewBeforeImages)
        val shareButton: ImageView = view.findViewById(R.id.imageViewShare)

        when {
            isFavorite -> {
                imagesPresenter.onCreateFavoriteImages(breed)
                title.text = breed
                val backText: TextView = view.findViewById(R.id.textViewFromSubbreedsToBreeds)
                backText.text = getString(R.string.favorites)
            }
            subbreed.isNotEmpty() -> {
                imagesPresenter.onCreateSubbreedImages(breed, subbreed)
                title.text = subbreed
            }
            else -> {
                imagesPresenter.onCreateAllImages(breed)
                title.text = breed
            }
        }

        backButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            onBackPressed()
        }
        shareButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setView(R.layout.item_share)
                .create()
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.show()
            with(dialog) {
                buttonCancel.setOnClickListener {
                    dialog.dismiss()
                }
                buttonShare.setOnClickListener {
                    shareImage(currentImage)
                    dialog.dismiss()
                }
            }
        }

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

    }

    override fun createImages(images: List<BreedImage>) {
        viewPager.adapter = ViewPagerAdapter(images.toMutableList()).apply {
            likeClickListener = this@ImagesActivity
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentImage = (viewPager.adapter as ViewPagerAdapter).list[position].image
            }
        })
    }

    override fun showError(message: Int) {
        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.item_fail)
            .show()
        with(dialog) {
            textViewFailed.text = getString(message)
            buttonOk.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun showLoading(isVisible: Boolean) {
        determinateBarImages.isVisible = isVisible
    }

    override fun fillOrOutlineLikeBtn(breedImage: BreedImage, position: Int) {
        with(viewPager.adapter as ViewPagerAdapter) {
            changeItem(breedImage, position)
            notifyItemChanged(position)
        }
    }

    override fun onLikeClicked(breedImage: BreedImage, position: Int) {
        if (breedImage.isLiked == 0) {
            imagesPresenter.onLikeClicked(breedImage, position)
        } else {
            imagesPresenter.onUnlikeClicked(breedImage, position)
        }
    }

    private fun shareImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(resource))
                    startActivity(Intent.createChooser(intent, "Share Image"))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(this.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")

            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

}