package ru.korolevss.wallettestdogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar

private const val BREED = "breed"
private const val SUB_BREEDS = "subBreeds"

class SubbredsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subbreds)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_subbreeds_layout)
        val view = supportActionBar?.customView

        val title: TextView = view!!.findViewById(R.id.textViewTitleSubbreeds)
        val backButton: ImageView = view.findViewById(R.id.imageViewBeforeSubbreeds)

        val breed = intent.getStringExtra(BREED)
        val subBreeds = intent.getStringArrayExtra(SUB_BREEDS)

        title.text = breed
        backButton.setOnClickListener {
            onBackPressed()
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = BreedFragment.newInstance(false, breed, subBreeds)
        fragmentTransaction.add(R.id.subbreeds_container, fragment)
        fragmentTransaction.commit()
    }
}