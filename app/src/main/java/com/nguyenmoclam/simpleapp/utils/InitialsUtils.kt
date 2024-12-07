package com.nguyenmoclam.simpleapp.utils

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.collection.LruCache

// Danh sách các từ thường không quan trọng
val stopWords = setOf(
    "in", "the", "and", "of", "a", "an", "to", "with", "for", "on", "at", "by"
)

// Hàm tạo initials từ tên
fun generateInitials(name: String): String {
    val words = name.split(" ")
        .filter { it.isNotEmpty() && it.lowercase() !in stopWords }
        .map { it.filter { char -> char.isLetter() } } // Loại bỏ ký tự không phải chữ cái

    return when {
        words.size >= 2 -> {
            "${words.first().first().uppercaseChar()}${words.last().first().uppercaseChar()}"
        }
        words.size == 1 -> {
            val firstChar = words[0].firstOrNull()?.uppercaseChar() ?: 'N'
            "$firstChar$firstChar"
        }
        else -> "NA" // Nếu không có từ quan trọng nào
    }
}

// Hàm tính màu nền và màu chữ dựa trên initials
fun getColorFromInitials(initials: String): Pair<Int, Int> {
    val hash = initials.hashCode()
    val r = (hash and 0xFF0000 shr 16)
    val g = (hash and 0x00FF00 shr 8)
    val b = (hash and 0x0000FF)

    val backgroundColor = Color.rgb(r, g, b)

    val brightness = (299 * r + 587 * g + 114 * b) / 1000

    val textColor = if (brightness > 128) Color.BLACK else Color.WHITE

    return Pair(backgroundColor, textColor)
}

// Caching Bitmap để tối ưu hiệu suất
object InitialsDrawableCache {
    // Xác định kích thước cache (ví dụ: tối đa 100 Bitmap)
    private val cacheSize = 100
    val bitmapCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            // Tính kích thước của mỗi Bitmap trong cache (tính bằng KB)
            return value.byteCount / 1024
        }
    }
}

// Hàm tạo Bitmap với hình tròn và initials
fun createInitialsBitmap(
    initials: String,
    size: Int = 100,
    backgroundColor: Int,
    textColor: Int
): Bitmap {
    // Kiểm tra cache trước
    InitialsDrawableCache.bitmapCache.get(initials)?.let {
        return it
    }

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Vẽ hình tròn
    val paint = Paint().apply {
        isAntiAlias = true
        color = backgroundColor
        style = Paint.Style.FILL
    }
    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    // Vẽ chữ
    val textPaint = Paint().apply {
        isAntiAlias = true
        color = textColor
        textSize = size / 2f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    // Điều chỉnh kích thước chữ dựa trên số lượng ký tự
    val adjustedTextSize = when (initials.length) {
        1 -> size / 2.5f
        2 -> size / 2f
        else -> size / 2f
    }
    textPaint.textSize = adjustedTextSize

    // Tính toán vị trí chữ để cân giữa
    val xPos = size / 2f
    val yPos = (size / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)

    canvas.drawText(initials, xPos, yPos, textPaint)

    // Lưu Bitmap vào cache
    InitialsDrawableCache.bitmapCache.put(initials, bitmap)

    return bitmap
}

// Hàm tạo Drawable từ Bitmap
fun createInitialsDrawable(initials: String, size: Int = 100): Drawable {
    val (backgroundColor, textColor) = getColorFromInitials(initials)
    val bitmap = createInitialsBitmap(initials, size, backgroundColor, textColor)
    return BitmapDrawable(Resources.getSystem(), bitmap)
}
