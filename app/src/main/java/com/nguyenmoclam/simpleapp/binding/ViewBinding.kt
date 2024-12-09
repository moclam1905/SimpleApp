package com.nguyenmoclam.simpleapp.binding

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable


import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.nguyenmoclam.simpleapp.R
import com.nguyenmoclam.simpleapp.database.utils.createInitialsDrawable
import com.nguyenmoclam.simpleapp.database.utils.dpToPx
import com.nguyenmoclam.simpleapp.utils.formatDate
import com.nguyenmoclam.simpleapp_model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ViewBinding {

    @JvmStatic
    @BindingAdapter("colorText")
    fun bindTextColor(view: AppCompatTextView, backgroundColor: Int?) {
        backgroundColor?.let {
            view.setTextColor(backgroundColor)
        }
    }

    @JvmStatic
    @BindingAdapter("simpleToast")
    fun bindToast(view: View, text: String?) {
        text?.let {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @BindingAdapter("date")
    fun bindDate(view: AppCompatTextView, dateString: String?) {
        dateString?.let {
            view.text = formatDate(view.context.getString(R.string.date_format), it)
        }
    }


    object CoroutineScopeSingleton {
        private val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)

        fun cancel() {
            job.cancel()
        }
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteCard")
    fun bindLoadImagePalette(
        view: AppCompatImageView,
        item: Item?,
        paletteCard: MaterialCardView
    ) {
        item?.let {
            CoroutineScopeSingleton.scope.launch {
                val drawableGenerated = withContext(Dispatchers.Default) {
                    val sizeInDp = 120f
                    val sizeInPx = dpToPx(sizeInDp, view.context)
                    createInitialsDrawable(
                        it.initials,
                        it.backgroundColor,
                        it.textColor,
                        size = sizeInPx
                    )
                }
                Glide.with(view.context)
                    .load(drawableGenerated)
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean,
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean,
                            ): Boolean {
                                paletteCard.setCardBackgroundColor(item.textColor!!)
//                                val drawable = resource as BitmapDrawable
//                                val bitmap = drawable.bitmap
//                                Palette.Builder(bitmap).generate {
//                                    it?.let { palette ->
//                                        val rgb = palette.dominantSwatch?.rgb
//                                        if (rgb != null) {
//                                            paletteCard.setCardBackgroundColor(rgb)
//                                        }
//                                    }
//                                }
                                return false
                            }
                        },
                    )
                    .into(view)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("paletteImage", "paletteView")
    fun bindLoadImagePaletteView(view: AppCompatImageView, item: Item?, paletteView: View) {
        val context = view.context
        item?.let {
            CoroutineScopeSingleton.scope.launch {
                val drawableGenerated = withContext(Dispatchers.Default) {
                    val sizeInDp = 120f
                    val sizeInPx = dpToPx(sizeInDp, view.context)
                    createInitialsDrawable(
                        it.initials,
                        it.backgroundColor,
                        it.textColor,
                        size = sizeInPx
                    )
                }
                Glide.with(view.context)
                    .load(drawableGenerated)
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean,
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean,
                            ): Boolean {
                                val drawable = resource as BitmapDrawable
                                val bitmap = drawable.bitmap
                                Palette.Builder(bitmap).generate { palette ->
                                    val light = palette?.lightVibrantSwatch?.rgb
                                    val domain = palette?.dominantSwatch?.rgb
                                    if (domain != null) {
                                        if (light != null) {
                                            val gradient = GradientDrawable(
                                                GradientDrawable.Orientation.TOP_BOTTOM,
                                                intArrayOf(domain, light)
                                            ).apply {
                                                cornerRadius = 0f
                                            }
                                            paletteView.background = gradient
                                        } else {
                                            paletteView.setBackgroundColor(item.textColor!!)
                                        }
                                        if (context is AppCompatActivity) {
                                            context.window.apply {
                                                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                                statusBarColor = domain
                                            }
                                        }
                                    }
                                }
                                return false
                            }
                        },
                    )
                    .into(view)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun bindGone(view: View, shouldGone: Boolean) {
        view.visibility = if (shouldGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //
    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindOnBackPressed(view: View, onBackPress: Boolean) {
        val context = view.context
        if (onBackPress && context is OnBackPressedDispatcherOwner) {
            view.setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
