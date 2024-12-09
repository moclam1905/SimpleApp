package com.nguyenmoclam.simpleapp.database.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.collection.LruCache
import androidx.core.graphics.ColorUtils

object InitialsDrawableCache {
    private val cacheSize = 100
    val bitmapCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }
}

fun createInitialsBitmap(
    initials: String,
    size: Int = 100,
    backgroundColor: Int,
    textColor: Int
): Bitmap {
    InitialsDrawableCache.bitmapCache.get(initials)?.let {
        return it
    }

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        isAntiAlias = true
        color = backgroundColor
        style = Paint.Style.FILL
    }
    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    val textPaint = Paint().apply {
        isAntiAlias = true
        color = textColor
        textSize = size / 2f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val xPos = size / 2f
    val yPos = (size / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)

    canvas.drawText(initials, xPos, yPos, textPaint)

    InitialsDrawableCache.bitmapCache.put(initials, bitmap)

    return bitmap
}

fun createInitialsDrawable(
    initials: String?, backgroundColor: Int?,
    textColor: Int?, size: Int = 100
): Drawable {

    val bitmap = createInitialsBitmap(
        initials ?: "NA",
        size,
        backgroundColor ?: Color.BLACK,
        textColor ?: Color.WHITE
    )
    return BitmapDrawable(Resources.getSystem(), bitmap)
}

val stopWords = setOf(
    "in", "the", "and", "of", "a", "an", "to", "with", "for", "on", "at", "by"
)

fun generateInitials(name: String): String {
    val words = name.split(" ")
        .filter { it.isNotEmpty() && it.lowercase() !in stopWords }
        .map { it.filter { char -> char.isLetter() } }

    return when {
        words.size >= 2 -> {
            "${words.first().first().uppercaseChar()}${words.last().first().uppercaseChar()}"
        }

        words.size == 1 -> {
            val firstChar = words[0].firstOrNull()?.uppercaseChar() ?: 'N'
            "$firstChar$firstChar"
        }

        else -> "NA"
    }
}

fun isColorDark(color: Int): Boolean {
    val darkness = 1 - (0.299 * Color.red(color) +
            0.587 * Color.green(color) +
            0.114 * Color.blue(color)) / 255
    return darkness >= 0.5
}

fun dpToPx(dp: Float, context: Context): Int {
    return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
}

fun getColorFromInitials(initials: String): Pair<Int, Int> {
    if (initials.isEmpty()) {
        return Pair(Color.parseColor("#E0E0E0"), Color.DKGRAY)
    }

    val firstChar = initials[0].uppercaseChar()
    val textColor = ColorMapping.textColorMap[firstChar] ?: Color.DKGRAY
    val backgroundColor = getComplementaryColor(textColor)

    if (!isContrastSufficient(textColor, backgroundColor)) {
        val adjustedBackgroundColor =
            adjustBrightness(backgroundColor, if (isColorDark(textColor)) 1.5f else 0.5f)
        return Pair(adjustedBackgroundColor, textColor)
    }

    return Pair(backgroundColor, textColor)
}


fun getComplementaryColor(color: Int): Int {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color, hsl)
    hsl[0] = (hsl[0] + 180) % 360
    return ColorUtils.HSLToColor(hsl)
}

fun isContrastSufficient(textColor: Int, backgroundColor: Int): Boolean {
    val contrast = ColorUtils.calculateContrast(textColor, backgroundColor)
    return contrast >= 4.5
}

fun adjustBrightness(color: Int, factor: Float): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)
    return Color.HSVToColor(hsv)
}




