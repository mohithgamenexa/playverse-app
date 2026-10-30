package com.playverse.app.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.playverse.app.R

/** Games without finished art yet are drawn in code (see GameArt.kt) until their art arrives. */
enum class DrawnArt { JewelSlide, Arrows, PicQuiz, HexaBlock }

data class Game(
    val id: String,
    val name: String,
    @DrawableRes val art: Int?,
    val drawn: DrawnArt?,
    val labelColor: Color,
)

enum class TaskAction { Play, WatchAd, Invite }

data class Task(
    val title: String,
    val detail: String,
    val progress: Int,
    val goal: Int,
    val reward: Int,
    val gameId: String? = null,
    @DrawableRes val icon: Int? = null,
    val action: TaskAction = TaskAction.Play,
)

data class CoinPack(val name: String, @DrawableRes val art: Int, val coins: Int, val price: String, val badge: String? = null)

data class DailyReward(val coins: Int, @DrawableRes val art: Int)

data class Avatar(val id: String, @DrawableRes val art: Int)

data class Frame(val id: String, val name: String, @DrawableRes val art: Int?)

object Catalog {
    val games = listOf(
        Game("marble_blast", "Marble Blast", R.drawable.game_marble_blast, null, Color(0xFF1D4F8F)),
        Game("jewel_slide", "Jewel Slide", null, DrawnArt.JewelSlide, Color(0xFF6A1B6E)),
        Game("arrows", "Arrows", null, DrawnArt.Arrows, Color(0xFF0F5E63)),
        Game("block_puzzle", "Block Puzzle", R.drawable.game_block_puzzle, null, Color(0xFF183A7A)),
        Game("bingo", "Bingo", R.drawable.game_bingo, null, Color(0xFF1E6B3A)),
        Game("blackjack", "Blackjack", R.drawable.game_blackjack, null, Color(0xFF3E1E6E)),
        Game("slots", "Slots", R.drawable.game_slots, null, Color(0xFF5B1E7A)),
        Game("pic_quiz", "Pic Quiz", null, DrawnArt.PicQuiz, Color(0xFF8C2F1E)),
        Game("hexa_block", "Hexa Block Puzzle", null, DrawnArt.HexaBlock, Color(0xFF23407E)),
        Game("yatzy", "Yatzy", R.drawable.game_yatzy, null, Color(0xFF8A3A16)),
    )

    fun game(id: String) = games.first { it.id == id }

    // Sample progress until the games report real numbers back from Unity.
    val missions = listOf(
        Task("Play Marble Blast", "Pop 300 marbles", 120, 300, 200, "marble_blast"),
        Task("Play Jewel Slide", "Match 50 jewels", 18, 50, 150, "jewel_slide"),
        Task("Play Arrows", "Clear 10 boards", 4, 10, 150, "arrows"),
        Task("Play Block Puzzle", "Clear 20 lines", 7, 20, 150, "block_puzzle"),
        Task("Play Bingo", "Complete 5 bingo games", 2, 5, 150, "bingo"),
        Task("Play Blackjack", "Win 5 hands", 3, 5, 200, "blackjack"),
        Task("Play Slots", "Play 10 games in Slots", 4, 10, 200, "slots"),
        Task("Play Pic Quiz", "Guess 15 pictures", 6, 15, 150, "pic_quiz"),
        Task("Play Hexa Block Puzzle", "Place 40 blocks", 22, 40, 150, "hexa_block"),
        Task("Play Yatzy", "Score 5,000 points", 2_500, 5_000, 200, "yatzy"),
        Task("Watch Ads", "Watch 5 rewarded ads", 2, 5, 100, icon = R.drawable.reward_video, action = TaskAction.WatchAd),
        Task("Invite Friends", "Invite 1 friend to play", 0, 1, 200, icon = R.drawable.reward_friends, action = TaskAction.Invite),
    )

    val challenges = listOf(
        Task("Marble Blast", "Reach a 20x combo", 8, 20, 500, "marble_blast"),
        Task("Jewel Slide", "Score 50,000 in one game", 21_000, 50_000, 500, "jewel_slide"),
        Task("Arrows", "Clear 3 boards in a minute", 1, 3, 400, "arrows"),
        Task("Block Puzzle", "Clear 4 lines at once", 0, 1, 400, "block_puzzle"),
        Task("Bingo", "Get 3 bingos in one game", 1, 3, 500, "bingo"),
        Task("Blackjack", "Win 5 hands in a row", 2, 5, 600, "blackjack"),
        Task("Slots", "Hit 3 jackpots", 1, 3, 800, "slots"),
        Task("Pic Quiz", "Guess 10 in a row", 4, 10, 400, "pic_quiz"),
        Task("Hexa Block Puzzle", "Score 10,000 points", 6_500, 10_000, 500, "hexa_block"),
        Task("Yatzy", "Roll 2 Yatzys in one game", 0, 2, 600, "yatzy"),
    )

    val packs = listOf(
        CoinPack("Starter Pack", R.drawable.pack_starter, 5_000, "$0.99", "Popular"),
        CoinPack("Value Pack", R.drawable.pack_value, 25_000, "$2.99"),
        CoinPack("Mega Pack", R.drawable.pack_mega, 60_000, "$4.99"),
        CoinPack("Super Pack", R.drawable.pack_super, 150_000, "$9.99", "Best Value"),
        CoinPack("Ultra Pack", R.drawable.pack_ultra, 300_000, "$19.99"),
        CoinPack("Legendary Pack", R.drawable.pack_legendary, 700_000, "$49.99"),
    )

    val daily = listOf(
        DailyReward(5_000, R.drawable.day_coins_small),
        DailyReward(10_000, R.drawable.day_coins_small),
        DailyReward(15_000, R.drawable.day_bag),
        DailyReward(25_000, R.drawable.day_coins),
        DailyReward(50_000, R.drawable.day_coins_big),
        DailyReward(75_000, R.drawable.day_chest),
        DailyReward(100_000, R.drawable.day_chest_open),
    )

    const val WELCOME_BONUS = 25_000
    const val FREE_CHEST = 1_000
    const val LEVEL_UP_REWARD = 500

    val avatars = listOf(
        Avatar("boy", R.drawable.avatar_boy),
        Avatar("girl_cap", R.drawable.avatar_girl_cap),
        Avatar("boy_spiky", R.drawable.avatar_boy_spiky),
        Avatar("girl_shades", R.drawable.avatar_girl_shades),
        Avatar("cool_guy", R.drawable.avatar_cool_guy),
        Avatar("cat", R.drawable.avatar_cat),
        Avatar("panda", R.drawable.avatar_panda),
        Avatar("girl_headphones", R.drawable.avatar_girl_headphones),
        Avatar("beard", R.drawable.avatar_beard),
        Avatar("girl_pink", R.drawable.avatar_girl_pink),
        Avatar("robot", R.drawable.avatar_robot),
        Avatar("lion", R.drawable.avatar_lion),
    )

    fun avatar(id: String) = avatars.firstOrNull { it.id == id } ?: avatars.first()

    val frames = listOf(
        Frame("default", "Default", null),
        Frame("royal", "Royal", R.drawable.frame_royal),
        Frame("silver", "Silver", R.drawable.frame_silver),
        Frame("bronze", "Bronze", R.drawable.frame_bronze),
        Frame("neon", "Neon", R.drawable.frame_neon),
        Frame("nature", "Nature", R.drawable.frame_nature),
        Frame("summer", "Summer", R.drawable.frame_summer),
        Frame("halloween", "Halloween", R.drawable.frame_halloween),
        Frame("christmas", "Christmas", R.drawable.frame_christmas),
        Frame("winter", "Winter", R.drawable.frame_winter),
        Frame("legendary", "Legendary", R.drawable.frame_legendary),
        Frame("galaxy", "Galaxy", R.drawable.frame_galaxy),
    )

    fun frame(id: String) = frames.firstOrNull { it.id == id } ?: frames.first()

    val languages = listOf("English", "Español", "Français", "Deutsch", "Português", "हिन्दी")
}
