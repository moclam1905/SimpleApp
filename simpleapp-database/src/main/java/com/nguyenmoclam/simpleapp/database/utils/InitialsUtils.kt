package com.nguyenmoclam.simpleapp.database.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.collection.LruCache
import androidx.core.graphics.ColorUtils

/**
 * Object to cache generated initials drawables to improve performance and memory usage.
 * Uses LruCache to automatically remove least recently used bitmaps when cache size limit is reached.
 */
object InitialsDrawableCache {
  private const val CACHE_SIZE = 100
  val bitmapCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(CACHE_SIZE) {
    override fun sizeOf(key: String, value: Bitmap): Int {
      return value.byteCount / 1024
    }
  }
}

/**
 * Creates a circular bitmap with initials text centered inside.
 *
 * @param initials The text to display (typically 1-2 characters)
 * @param size The width and height of the bitmap in pixels
 * @param backgroundColor The background color of the circle
 * @param textColor The color of the initials text
 * @return A circular bitmap containing the initials
 */
fun createInitialsBitmap(
  initials: String,
  size: Int = 100,
  backgroundColor: Int,
  textColor: Int,
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

/**
 * Creates a drawable from initials text with specified colors.
 *
 * @param initials The text to display (typically 1-2 characters)
 * @param backgroundColor The background color of the circle (defaults to black if null)
 * @param textColor The color of the initials text (defaults to white if null)
 * @param size The width and height of the drawable in pixels
 * @return A drawable containing the initials in a circle
 */
fun createInitialsDrawable(
  initials: String?,
  backgroundColor: Int?,
  textColor: Int?,
  size: Int = 100,
): Drawable {
  val bitmap = createInitialsBitmap(
    initials ?: "NA",
    size,
    backgroundColor ?: Color.BLACK,
    textColor ?: Color.WHITE,
  )
  return BitmapDrawable(Resources.getSystem(), bitmap)
}

/** Common words to exclude when generating initials from names */
val stopWords = setOf(
  "in", "the", "and", "of", "a", "an", "to", "with", "for", "on", "at", "by",
)

/**
 * Generates a two-letter initial string from a given name by applying the following rules:
 * 1. For multi-word names: Uses first letter of first and last non-stop words
 *    Example: "John William Smith" -> "JS"
 *    Example: "Mary of the House" -> "MH" ("of", "the" are stop words)
 * 2. For single-word names:
 *    - If word has 2+ letters: Uses first two letters
 *      Example: "John" -> "JO"
 *    - If word has 1 letter: Duplicates that letter
 *      Example: "A" -> "AA"
 * 3. Special cases:
 *    - Empty string or only stop words: Returns "NA"
 *    - Non-letter characters are ignored
 *      Example: "John.Doe" -> "JD"
 *
 * @param name The input name to generate initials from
 * @return A two-letter initial string in uppercase, or "NA" if valid initials cannot be generated
 */
fun generateInitials(name: String): String {
  val words = name.split(" ")
    .filter { it.isNotEmpty() && it.lowercase() !in stopWords }
    .map { it.filter { char -> char.isLetter() } }

  return when {
    words.size >= 2 -> {
      "${words.first().first().uppercaseChar()}${words.last().first().uppercaseChar()}"
    }

    words.size == 1 -> {
      val word = words[0]
      if (word.length >= 2) {
        "${word[0].uppercaseChar()}${word[1].uppercaseChar()}"
      } else {
        val firstChar = word.firstOrNull()?.uppercaseChar() ?: 'N'
        "$firstChar$firstChar"
      }
    }

    else -> "NA"
  }
}

/**
 * Determines if a color is considered "dark" based on its RGB values.
 *
 * @param color The color to check
 * @return true if the color is dark, false if it's light
 */
fun isColorDark(color: Int): Boolean {
  val darkness = 1 - (
    0.299 * Color.red(color) +
      0.587 * Color.green(color) +
      0.114 * Color.blue(color)
    ) / 255
  return darkness >= 0.5
}

/**
 * Converts dp value to pixels based on device density.
 *
 * @param dp The value in density-independent pixels
 * @param context The context to get display metrics
 * @return The value in pixels
 */
fun dpToPx(dp: Float, context: Context): Int {
  return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
}

/**
 * Generates a color pair (background and text) based on initials.
 * Ensures good contrast between the colors.
 *
 * @param initials The initials to generate colors for
 * @return Pair of (backgroundColor, textColor)
 */
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

/**
 * Calculates the complementary color by shifting hue by 180 degrees.
 *
 * @param color The original color
 * @return The complementary color
 */
fun getComplementaryColor(color: Int): Int {
  val hsl = FloatArray(3)
  ColorUtils.colorToHSL(color, hsl)
  hsl[0] = (hsl[0] + 180) % 360
  return ColorUtils.HSLToColor(hsl)
}

/**
 * Checks if the contrast between two colors is sufficient for readability.
 * Uses WCAG guidelines for minimum contrast ratio.
 *
 * @param textColor The color of the text
 * @param backgroundColor The color of the background
 * @return true if contrast is sufficient, false otherwise
 */
fun isContrastSufficient(textColor: Int, backgroundColor: Int): Boolean {
  val contrast = ColorUtils.calculateContrast(textColor, backgroundColor)
  return contrast >= 4.5
}

/**
 * Adjusts the brightness of a color by the given factor.
 *
 * @param color The color to adjust
 * @param factor Factor to multiply brightness by (>1 brightens, <1 darkens)
 * @return The adjusted color
 */
fun adjustBrightness(color: Int, factor: Float): Int {
  val hsv = FloatArray(3)
  Color.colorToHSV(color, hsv)
  hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)
  return Color.HSVToColor(hsv)
}
