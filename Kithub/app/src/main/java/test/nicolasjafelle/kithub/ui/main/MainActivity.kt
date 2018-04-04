package test.nicolasjafelle.kithub.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import test.nicolasjafelle.kithub.R
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.ui.AbstractAppCompatActivity
import test.nicolasjafelle.kithub.ui.detail.DetailActivity
import test.nicolasjafelle.kithub.ui.view.MaterialSearchView

class MainActivity : AbstractAppCompatActivity(), MainFragment.Callback {

    private lateinit var searchView: MaterialSearchView

    companion object {
        fun launchActivity(activity: AppCompatActivity) {

            val intent = Intent(activity, MainActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    override fun getBaseLayoutResId() = R.layout.activity_single_search_fragment

    override fun setInitialFragment() {
        setInitialFragment(MainFragment.newInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        return true
    }

    override fun onBackPressed() {
        if (searchView.isSearchOpen) {
            searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    private fun initSearchView() {
        searchView = findViewById(R.id.activity_single_fragment_search_view)

        toolbar?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                toolbar?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                toolbar?.let { searchView.toolbarPosition(it.width, it.height) }
            }
        })

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?) = resolveSearch(newText!!)
        })
    }

    private fun resolveSearch(query: String): Boolean {
        if (getCurrentFragment() is MainFragment) {
            (getCurrentFragment() as MainFragment).performSearch(query)
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MaterialSearchView.REQUEST_VOICE
                && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.size > 0) {
                val searchWord = results[0]
                searchView.searchSrcTextView.setText(searchWord)
                resolveSearch(searchWord)
            }
        }
    }

    override fun onItemSelect(repo: Repo, view: View) {
        DetailActivity.launchActivity(this, repo, view)
    }
}