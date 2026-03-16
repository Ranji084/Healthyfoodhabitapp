package com.simats.Healthyfoodhabitapp

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val defaultUrl = "http://10.161.227.202:5000/"

    fun saveLoginSession(email: String, name: String? = null, userId: Int? = null) {
        sharedPref.edit().apply {
            putString("user_email", email)
            if (!name.isNullOrBlank()) {
                putString("user_name", name)
            }
            if (userId != null) {
                putInt("user_id", userId)
            }
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = sharedPref.getBoolean("is_logged_in", false)
    fun getUserEmail(): String? = sharedPref.getString("user_email", null)
    fun getUserName(): String = sharedPref.getString("user_name", "User") ?: "User"
    
    fun getUserId(): Int = sharedPref.getInt("user_id", -1)

    // --- DYNAMIC SERVER IP ---
    fun saveBaseUrl(ip: String) {
        val trimmedIp = ip.trim()
        if (trimmedIp.isBlank()) return
        
        val formattedUrl = if (trimmedIp.startsWith("http")) {
            if (trimmedIp.endsWith("/")) trimmedIp else "$trimmedIp/"
        } else {
            "http://$trimmedIp:5000/"
        }
        
        // Basic validation: ensure it looks like a URL and has a host
        try {
            val host = formattedUrl.substringAfter("://").substringBefore("/")
            val hostname = if (host.contains(":")) host.substringBefore(":") else host
            if (hostname.isNotBlank()) {
                sharedPref.edit().putString("base_url", formattedUrl).apply()
            }
        } catch (e: Exception) {
            // Ignore invalid inputs
        }
    }

    fun getBaseUrl(): String {
        val savedUrl = sharedPref.getString("base_url", defaultUrl) ?: defaultUrl
        if (savedUrl.isBlank()) return defaultUrl
        
        // Double check validity before returning
        return try {
            val host = savedUrl.substringAfter("://").substringBefore("/")
            val hostname = if (host.contains(":")) host.substringBefore(":") else host
            if (hostname.isBlank()) defaultUrl else savedUrl
        } catch (e: Exception) {
            defaultUrl
        }
    }

    fun saveWaterIntake(glasses: Int) = sharedPref.edit().putInt("water_intake", glasses).apply()
    fun getWaterIntake(): Int = sharedPref.getInt("water_intake", 0)

    fun saveSteps(steps: Int) = sharedPref.edit().putInt("steps_count", steps).apply()
    fun getSteps(): Int = sharedPref.getInt("steps_count", 0)

    fun saveHealthScore(score: Int) {
        sharedPref.edit().putInt("health_score", score).apply()
    }

    fun getHealthScore(): Int {
        return sharedPref.getInt("health_score", 0)
    }

    fun saveAiAdvice(advice: String) {
        sharedPref.edit().putString("ai_advice", advice).apply()
    }

    fun getAiAdvice(): String {
        return sharedPref.getString("ai_advice", "No advice available") ?: "No advice available"
    }

    fun logout() {
        val currentUrl = getBaseUrl()
        sharedPref.edit().clear().apply()
        // Restore base_url after clear so the user doesn't have to re-enter IP
        sharedPref.edit().putString("base_url", currentUrl).apply()
    }
}
