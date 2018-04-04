package test.nicolasjafelle.kithub.ui.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import test.nicolasjafelle.kithub.R
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.extensions.launchSceneTransitionAnimation
import test.nicolasjafelle.kithub.extensions.loadImage
import test.nicolasjafelle.kithub.ui.AbstractAppCompatActivity

/**
 * Created by nicolas on 11/7/17.
 */
class DetailActivity : AbstractAppCompatActivity() {

    private lateinit var imageView: ImageView

    companion object {
        fun launchActivity(activity: AppCompatActivity, repo: Repo, transitionView: View?) {

            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailFragment.SELECTED_REPO, repo)
            activity.launchSceneTransitionAnimation(intent, transitionView)
        }
    }

    override fun setInitialFragment() {
        setInitialFragment(DetailFragment.newInstance(intent.extras))
    }

    override fun getBaseLayoutResId() = R.layout.activity_collapse_single_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color.WHITE)
        setupToolbar()

        ActivityCompat.postponeEnterTransition(this)

        imageView = findViewById(R.id.activity_collapse_single_fragment_image)

        if (intent.extras != null && intent.extras.containsKey(DetailFragment.SELECTED_REPO)) {
            val repo = intent.extras.getParcelable<Repo>(DetailFragment.SELECTED_REPO)

            setToolbarTitle(repo.name)

            repo.owner.avatarUrl?.apply {
                imageView.loadImage(this, {
                    ActivityCompat.startPostponedEnterTransition(this@DetailActivity)
                })
            }
        }
    }

    private fun setupToolbar() {
        setToolbarColor(android.R.color.transparent)
        setMaterialStatusBarColor(android.R.color.transparent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}