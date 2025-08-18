package com.example.paintnumber.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {
	private const val PREFS_NAME = "theme_prefs"
	private const val KEY_NIGHT_MODE = "is_night_mode"

	fun isNightMode(context: Context): Boolean {
		return getPrefs(context).getBoolean(KEY_NIGHT_MODE, false)
	}

	fun toggleTheme(context: Context) {
		val newValue = !isNightMode(context)
		setNightMode(context, newValue)
	}

	fun applySavedTheme(context: Context) {
		setNightMode(context, isNightMode(context), save = false)
	}

	private fun setNightMode(context: Context, enabled: Boolean, save: Boolean = true) {
		val mode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
		AppCompatDelegate.setDefaultNightMode(mode)
		if (save) getPrefs(context).edit().putBoolean(KEY_NIGHT_MODE, enabled).apply()
	}

	private fun getPrefs(context: Context): SharedPreferences {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}
} 