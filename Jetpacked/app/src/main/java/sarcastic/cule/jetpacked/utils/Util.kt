package sarcastic.cule.jetpacked.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import sarcastic.cule.jetpacked.R
import java.util.*

fun getProgressDrawable(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 1f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

@BindingAdapter("android:setText")
fun setText(view: TextView, text: String?) {

    if (!isValidString(text)) {
        view.visibility = View.VISIBLE
        view.text = text
    } else {
        view.visibility = View.GONE
    }

}

fun isValidString(text: String?): Boolean {

    return text == null ||
            text.isEmpty() ||
            text.trim() == "" ||
            text.trim().toLowerCase(Locale.ROOT) == "Null".toLowerCase(Locale.ROOT) ||
            text.trim().toLowerCase(Locale.ROOT) == "None".toLowerCase(Locale.ROOT)
}
