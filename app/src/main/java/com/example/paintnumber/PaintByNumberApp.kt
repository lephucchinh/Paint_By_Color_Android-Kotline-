package com.example.paintnumber

import android.app.Application
import com.example.paintnumber.utils.ThemeManager

/**
 * Class Application chính của ứng dụng.
 * Được khởi tạo khi ứng dụng bắt đầu chạy.
 * Chịu trách nhiệm khởi tạo các thành phần cốt lõi của ứng dụng.
 */
class PaintByNumberApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.applySavedTheme(this)
    }
} 