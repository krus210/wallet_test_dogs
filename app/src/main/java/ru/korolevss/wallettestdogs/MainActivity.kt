package ru.korolevss.wallettestdogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val FROM_FAVORITE_TO_IMAGES = 100

class MainActivity : AppCompatActivity() {

    private var changeFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_main_layout)
        val view = supportActionBar?.customView

        val title: TextView = view!!.findViewById(R.id.textViewTitleMain)

        imageViewList.isEnabled = false
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        val oldFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if (oldFragment == null) {
            val fragmentAllBreeds = BreedFragment.newInstance(false)
            fragmentTransaction.add(R.id.main_container, fragmentAllBreeds)
            fragmentTransaction.commit()
        }

        imageViewFavorites.setOnClickListener {
            imageViewFavorites.setImageResource(R.drawable.ic_favorite_filled)
            imageViewList.setImageResource(R.drawable.ic_article_outlined)

            fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragmentFavorites = BreedFragment.newInstance(true)
            fragmentTransaction.replace(R.id.main_container, fragmentFavorites)
            fragmentTransaction.commit()

            imageViewList.isEnabled = true
            imageViewFavorites.isEnabled = false
            title.text = getString(R.string.favorites)
        }

        imageViewList.setOnClickListener {
            imageViewFavorites.setImageResource(R.drawable.ic_favorite_outlined)
            imageViewList.setImageResource(R.drawable.ic_article_filled)

            fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = BreedFragment.newInstance(false)
            fragmentTransaction.replace(R.id.main_container, fragment)
            fragmentTransaction.commit()

            imageViewList.isEnabled = false
            imageViewFavorites.isEnabled = true
            title.text = getString(R.string.breeds)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FROM_FAVORITE_TO_IMAGES && resultCode == Activity.RESULT_OK) {
            changeFragment = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (changeFragment) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragmentFavorites = BreedFragment.newInstance(true)
            fragmentTransaction.replace(R.id.main_container, fragmentFavorites)
            fragmentTransaction.commit()
            changeFragment = false
        }
    }
}