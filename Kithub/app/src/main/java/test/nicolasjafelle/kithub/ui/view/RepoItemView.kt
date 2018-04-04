package test.nicolasjafelle.kithub.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import test.nicolasjafelle.kithub.R
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.extensions.loadImage

/**
 * Created by nicolas on 11/10/17.
 */
//class RepoItemView(context: Context): CardView(context) { //if we want to use the first constructor as primary/default constructor
class RepoItemView : CardView {

    var imageView: ImageView

    var fullnameView: TextView

    var ageView: TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.repo_item_view, this)

        imageView = findViewById(R.id.repo_item_view_image)
        fullnameView = findViewById(R.id.repo_item_view_fullname)
        ageView = findViewById(R.id.repo_item_view_age)

        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams = params

        cardElevation = resources.getDimensionPixelSize(R.dimen.card_elevation).toFloat()
        radius = resources.getDimensionPixelSize(R.dimen.card_radius).toFloat()
        foreground = ContextCompat.getDrawable(context, R.drawable.common_background_drawable)
    }

    fun loadData(repo: Repo) {

        repo.owner.avatarUrl?.let {
            imageView.loadImage(it)
        }


        if (!repo.name.isEmpty()) {
            fullnameView.text = repo.name
            ageView.text = repo.fullName
        } else {
            fullnameView.setText(R.string.no_info_available)
        }


    }

}