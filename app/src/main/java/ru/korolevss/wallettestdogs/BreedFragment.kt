package ru.korolevss.wallettestdogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_breed.*
import kotlinx.android.synthetic.main.item_fail.*
import ru.korolevss.wallettestdogs.adapter.BreedAdapter
import ru.korolevss.wallettestdogs.adapter.BreedDiffUtilCallback
import ru.korolevss.wallettestdogs.contract.MainContract
import ru.korolevss.wallettestdogs.model.BreedForList
import ru.korolevss.wallettestdogs.presenter.MainPresenter

private const val BREED = "breed"
private const val SUBBREED= "subbreed"
private const val SUB_BREEDS = "subBreeds"
private const val IS_FAVORITES = "is_favorites"
private const val FROM_FAVORITE_TO_IMAGES = 100

class BreedFragment : Fragment(), MainContract.View, BreedAdapter.OnBtnClickListener {

    private var breedName: String? = null
    private var subBreeds: Array<String>? = null
    private var isFavorites: Boolean = false
    private val mainPresenter by lazy { MainPresenter(context!!, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            breedName = it.getString(BREED)
            subBreeds = it.getStringArray(SUB_BREEDS)
            isFavorites = it.getBoolean(IS_FAVORITES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_breed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContainer.isEnabled = false
        when {
            isFavorites -> {
                mainPresenter.onCreateFavoriteBreeds()
            }
            breedName.isNullOrEmpty() -> {
                swipeContainer.isEnabled = true
                mainPresenter.onCreateAllBreeds()
                swipeContainer.setOnRefreshListener {
                    mainPresenter.onRefreshClicked()
                }
            }
            else -> {
                mainPresenter.onCreateSubBreeds(subBreeds ?: emptyArray())
            }
        }
    }

    override fun showError(message: Int) {
        val dialog = AlertDialog.Builder(context!!)
            .setView(R.layout.item_fail)
            .show()
        with (dialog) {
            textViewFailed.text = getString(message)
            buttonOk.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun stopRefreshing() {
        swipeContainer.isRefreshing = false
    }

    override fun createList(breeds: List<BreedForList>) {
        with(container) {
            layoutManager = LinearLayoutManager(context)
            adapter = BreedAdapter(breeds).apply {
                btnClickListener = this@BreedFragment
            }
        }
    }

    override fun updateList(breeds: List<BreedForList>) {
        with(container.adapter as BreedAdapter) {
            val breedDiffUtilCallback = BreedDiffUtilCallback(list, breeds)
            val postDiffResult = DiffUtil.calculateDiff(breedDiffUtilCallback)
            updatePosts(breeds)
            postDiffResult.dispatchUpdatesTo(this)
        }
    }

    override fun showLoading(isVisible: Boolean) {
        determinateBarBreeds.isVisible = isVisible
    }

    override fun onBtnClicked(breed: BreedForList) {
        when {
            isFavorites -> {
                val intent = Intent(context, ImagesActivity::class.java)
                intent.putExtra(BREED, breed.breedName)
                intent.putExtra(IS_FAVORITES, isFavorites)
                activity?.startActivityForResult(intent, FROM_FAVORITE_TO_IMAGES)
            }
            breedName.isNullOrEmpty() -> {
                if (breed.subBreeds.isEmpty()) {
                    val intent = Intent(context, ImagesActivity::class.java)
                    intent.putExtra(BREED, breed.breedName)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, SubbredsActivity::class.java)
                    intent.putExtra(BREED, breed.breedName)
                    intent.putExtra(SUB_BREEDS, breed.subBreeds.toTypedArray())
                    startActivity(intent)
                }
            }
            else -> {
                val intent = Intent(context, ImagesActivity::class.java)
                intent.putExtra(BREED, breedName)
                intent.putExtra(SUBBREED, breed.breedName)
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            isFavorites: Boolean,
            breed: String? = null,
            subBreeds: Array<String>? = null
        ) =
            BreedFragment().apply {
                arguments = Bundle().apply {
                    putString(BREED, breed)
                    putStringArray(SUB_BREEDS, subBreeds)
                    putBoolean(IS_FAVORITES, isFavorites)
                }
            }
    }
}