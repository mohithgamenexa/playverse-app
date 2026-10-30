package com.playverse.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.playverse.app.data.Catalog
import com.playverse.app.data.Player
import com.playverse.app.ui.AccountScreen
import com.playverse.app.ui.GamePopup
import com.playverse.app.ui.GameScreen
import com.playverse.app.ui.SplashScreen
import com.playverse.app.ui.home.HomePopup
import com.playverse.app.ui.home.HomeScreen
import com.playverse.app.ui.home.Tab
import com.playverse.app.ui.theme.PlayverseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders every screen and popup to app/screenshots/ on a 393 x 852 dp phone (Pixel 7 size).
 * Run with: ./gradlew testDebugUnitTest
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w393dp-h852dp-xxhdpi")
class ScreenshotTest {
    @get:Rule val compose = createComposeRule()

    private val player get() = Player(ApplicationProvider.getApplicationContext())

    private fun shot(name: String, content: @Composable () -> Unit) {
        compose.setContent { PlayverseTheme { content() } }
        compose.onRoot().captureRoboImage("screenshots/$name.png")
    }

    private fun home(name: String, tab: Tab, popup: HomePopup? = null) = shot(name) {
        HomeScreen(player, tab, {}, popup, {}, {}, {})
    }

    @Test fun splash() = shot("01_splash") { SplashScreen(onDone = {}, durationMillis = 1_000_000) }
    @Test fun games() = home("02_games", Tab.Games)

    @Config(qualifiers = "w360dp-h740dp-xxhdpi")
    @Test fun gamesNarrowPhone() = home("02b_games_narrow_phone", Tab.Games)
    @Test fun welcome() = home("03_welcome_bonus", Tab.Games, HomePopup.Welcome)
    @Test fun daily() = home("04_daily_rewards", Tab.Games, HomePopup.Daily)
    @Test fun settings() = home("05_settings", Tab.Games, HomePopup.Settings)
    @Test fun missions() = home("06_missions", Tab.Missions)
    @Test fun challenges() = home("07_challenges", Tab.Challenges)
    @Test fun rewards() = home("08_rewards", Tab.Rewards)
    @Test fun store() = home("09_store", Tab.Store)
    @Test fun profile() = home("10_profile", Tab.Games, HomePopup.Profile)
    @Test fun accountAvatars() = shot("11_account_avatars") { AccountScreen(player, {}, {}) }
    @Test fun accountFrames() = shot("12_account_frames") { AccountScreen(player, {}, {}, initialTab = 1) }

    private fun game(name: String, popup: GamePopup?) = shot(name) {
        GameScreen(player, Catalog.game("slots"), onExit = {}, initialPopup = popup)
    }

    @Test fun levelUp() = game("13_level_up", GamePopup.LevelUp)
    @Test fun paused() = game("14_paused", GamePopup.Paused)
    @Test fun extraLife() = game("15_extra_life", GamePopup.ExtraLife)
    @Test fun extraTime() = game("16_extra_time", GamePopup.ExtraTime)
    @Test fun gameOver() = game("17_game_over", GamePopup.GameOver)
    @Test fun gameScreen() = game("18_game_screen", null)
}
