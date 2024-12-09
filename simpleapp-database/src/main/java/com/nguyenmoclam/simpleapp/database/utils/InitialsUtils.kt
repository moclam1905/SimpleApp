package com.nguyenmoclam.simpleapp.database.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.collection.LruCache
import androidx.core.graphics.ColorUtils

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

    // Tính toán vị trí chữ để cân giữa
    val xPos = size / 2f
    val yPos = (size / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)

    canvas.drawText(initials, xPos, yPos, textPaint)

    // Lưu Bitmap vào cache
    InitialsDrawableCache.bitmapCache.put(initials, bitmap)

    return bitmap
}

// Hàm tạo Drawable từ Bitmap
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

//fun getColorFromInitials(initials: String): Pair<Int, Int> {
//    if (initials.isEmpty()) {
//        // Trả về màu mặc định nếu initials rỗng
//        return Pair(Color.LTGRAY, Color.DKGRAY)
//    }
//
//    // Lấy ký tự đầu tiên và chuyển thành chữ hoa
//    val firstChar = initials[0].uppercaseChar()
//
//    // Lấy màu chữ từ bản đồ, nếu không tìm thấy thì sử dụng màu mặc định
//    val textColor = ColorMapping.textColorMap[firstChar] ?: Color.BLACK
//
//    // Tính độ sáng của màu chữ để chọn màu nền phù hợp
//    val isTextColorDark = isColorDark(textColor)
//
//    // Chọn màu nền sao cho nổi bật với màu chữ
//    val backgroundColor = if (isTextColorDark) {
//        Color.WHITE
//    } else {
//        Color.BLACK
//    }
//
//    return Pair(backgroundColor, textColor)
//}
//
// Hàm kiểm tra xem màu có phải là màu tối hay không
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
        // Trả về màu mặc định nếu initials rỗng
        return Pair(Color.parseColor("#E0E0E0"), Color.DKGRAY) // Pastel Grey background
    }

    // Lấy ký tự đầu tiên và chuyển thành chữ hoa
    val firstChar = initials[0].uppercaseChar()

    // Lấy màu chữ từ bản đồ, nếu không tìm thấy thì sử dụng màu mặc định
    val textColor = ColorMapping.textColorMap[firstChar] ?: Color.DKGRAY

    // Lấy màu bổ trợ cho màu chữ
    val backgroundColor = getComplementaryColor(textColor)

    // Kiểm tra độ tương phản, nếu không đủ, điều chỉnh
    if (!isContrastSufficient(textColor, backgroundColor)) {
        // Điều chỉnh màu nền để đủ độ tương phản
        val adjustedBackgroundColor = adjustBrightness(backgroundColor, if (isColorDark(textColor)) 1.5f else 0.5f)
        return Pair(adjustedBackgroundColor, textColor)
    }

    // Thêm log để kiểm tra giá trị
    //Log.d("ColorUtils", "Initials: $initials, FirstChar: $firstChar, TextColor: $textColor, BackgroundColor: $backgroundColor")

    return Pair(backgroundColor, textColor)
}

// Hàm lấy màu bổ trợ
fun getComplementaryColor(color: Int): Int {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color, hsl)
    hsl[0] = (hsl[0] + 180) % 360 // Đổi hue sang màu bổ trợ
    return ColorUtils.HSLToColor(hsl)
}

// Hàm kiểm tra độ tương phản
fun isContrastSufficient(textColor: Int, backgroundColor: Int): Boolean {
    val contrast = ColorUtils.calculateContrast(textColor, backgroundColor)
    return contrast >= 4.5 // Theo WCAG AA
}

// Hàm điều chỉnh độ sáng của màu
fun adjustBrightness(color: Int, factor: Float): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f) // Điều chỉnh Value (Brightness)
    return Color.HSVToColor(hsv)
}




