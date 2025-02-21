package com.nguyenmoclam.simpleapp.extensions

import android.os.Parcelable
import androidx.activity.ComponentActivity

fun <T : Parcelable> ComponentActivity.argument(key: String): Lazy<T> {
  return lazy { requireNotNull(intent.getParcelableExtra(key)) }
}
