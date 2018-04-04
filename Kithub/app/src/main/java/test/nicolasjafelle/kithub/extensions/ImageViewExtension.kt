package test.nicolasjafelle.kithub.extensions

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import test.nicolasjafelle.kithub.R

/**
 * Created by nicolas on 11/10/17.
 */

fun ImageView.loadImage(url: String) {

    Picasso.with(context)
            .load(url)
            .fit()
            .centerCrop()
            .error(R.drawable.ic_image_thumb)
            .placeholder(R.drawable.ic_image_thumb)
            .into(this)
}

fun ImageView.loadImage(url: String, downloaded: () -> Unit) {

    val creator = Picasso.with(context).load(url).fit().centerCrop()

    creator.into(this, object : Callback {
        override fun onSuccess() {
            downloaded()
        }

        override fun onError() {
            downloaded()
        }
    })
}
