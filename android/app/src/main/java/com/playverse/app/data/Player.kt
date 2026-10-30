package com.playverse.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * The player's profile, coins and settings, saved on the phone.
 * Every value is Compose state, so screens update as soon as it changes.
 * When the backend arrives, this is the one place that syncs with it.
 */
class Player(context: Context, private val today: () -> Long = { LocalDate.now().toEpochDay() }) {
    private val prefs: SharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE)

    var name by stringPref("name", "Player123")
    var avatarId by stringPref("avatar", "boy")
    var frameId by stringPref("frame", "default")
    var coins by intPref("coins", 8_450)
    var level by intPref("level", 5)
    var xp by intPref("xp", 320)
    var gamesPlayed by intPref("games_played", 28)
    var bestScore by intPref("best_score", 25_430)

    var music by boolPref("music", true)
    var sound by boolPref("sound", true)
    var haptics by boolPref("haptics", true)
    var language by stringPref("language", "English")

    var welcomeClaimed by boolPref("welcome_claimed", false)
    private var dailyCount by intPref("daily_count", 0)
    private var lastDailyDay by longPref("last_daily_day", Long.MIN_VALUE)
    private var lastChestDay by longPref("last_chest_day", Long.MIN_VALUE)

    val xpGoal get() = level * 100
    val avatar get() = Catalog.avatar(avatarId)
    val frame get() = Catalog.frame(frameId)

    fun claimWelcome() {
        if (welcomeClaimed) return
        welcomeClaimed = true
        coins += Catalog.WELCOME_BONUS
    }

    // Daily rewards: one claim a day, 7 days in a row. Missing a day starts again from day 1.
    val claimedDailyToday get() = lastDailyDay == today()

    /** How many days of the current 7-day run are already claimed. */
    val dailyClaimed: Int
        get() = when {
            claimedDailyToday -> dailyCount
            lastDailyDay == today() - 1 && dailyCount < 7 -> dailyCount
            else -> 0
        }

    /** The day (0 to 6) that is claimable today, or that was claimed today. */
    val dailyToday get() = if (claimedDailyToday) dailyCount - 1 else dailyClaimed

    fun claimDaily(): Int {
        if (claimedDailyToday) return 0
        val day = dailyClaimed
        val amount = Catalog.daily[day].coins
        dailyCount = day + 1
        lastDailyDay = today()
        coins += amount
        return amount
    }

    val canOpenChest get() = lastChestDay != today()

    fun openChest(): Int {
        if (!canOpenChest) return 0
        lastChestDay = today()
        coins += Catalog.FREE_CHEST
        return Catalog.FREE_CHEST
    }

    fun levelUp() {
        level += 1
        xp = 0
        coins += Catalog.LEVEL_UP_REWARD
    }

    fun finishGame(score: Int, coinsEarned: Int) {
        gamesPlayed += 1
        bestScore = maxOf(bestScore, score)
        coins += coinsEarned
    }

    private fun intPref(key: String, default: Int) = object : ReadWriteProperty<Any?, Int> {
        val state = mutableIntStateOf(prefs.getInt(key, default))
        override fun getValue(thisRef: Any?, property: KProperty<*>) = state.intValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            state.intValue = value
            prefs.edit().putInt(key, value).apply()
        }
    }

    private fun longPref(key: String, default: Long) = object : ReadWriteProperty<Any?, Long> {
        val state = mutableLongStateOf(prefs.getLong(key, default))
        override fun getValue(thisRef: Any?, property: KProperty<*>) = state.longValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            state.longValue = value
            prefs.edit().putLong(key, value).apply()
        }
    }

    private fun boolPref(key: String, default: Boolean) = object : ReadWriteProperty<Any?, Boolean> {
        val state = mutableStateOf(prefs.getBoolean(key, default))
        override fun getValue(thisRef: Any?, property: KProperty<*>) = state.value
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            state.value = value
            prefs.edit().putBoolean(key, value).apply()
        }
    }

    private fun stringPref(key: String, default: String) = object : ReadWriteProperty<Any?, String> {
        val state = mutableStateOf(prefs.getString(key, default) ?: default)
        override fun getValue(thisRef: Any?, property: KProperty<*>) = state.value
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            state.value = value
            prefs.edit().putString(key, value).apply()
        }
    }
}
