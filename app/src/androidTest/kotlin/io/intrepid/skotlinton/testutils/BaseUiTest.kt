package io.intrepid.skotlinton.testutils

import android.os.Environment
import androidx.test.espresso.Espresso
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import io.intrepid.skotlinton.InstrumentationTestApplication
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import java.io.File

abstract class BaseUiTest {
    @Rule
    @JvmField
    val testNameRule = TestName()

    private lateinit var screenCaptureProcessor: ScreenCaptureProcessor

    @Before
    fun baseSetUp() {
        screenCaptureProcessor = SpoonScreenCaptureProcessor(this::class.java.name, testNameRule.methodName)
        // Take a screenshot whenever a test fails. The screenshot can be viewed in the spoon report or manually at
        // the phone's /sdcard/app_spoon-screenshots/ directory.
        Espresso.setFailureHandler { error, _ ->
            takeScreenshot("test_failure")
            throw error
        }
    }

    @After
    fun baseTearDown() {
        InstrumentationTestApplication.clearRestApiOverride()
        InstrumentationTestApplication.clearUserSettingsOverride()
    }

    fun takeScreenshot(screenShotName: String) {
        val screenCapture = Screenshot.capture()
        screenCapture.name = screenShotName
        screenCaptureProcessor.process(screenCapture)
    }
}

// Saves the screenshot to the directory that is expected by the spoon report.
class SpoonScreenCaptureProcessor(className: String, methodName: String) : BasicScreenCaptureProcessor() {
    init {
        val spoonDirectory = File(Environment.getExternalStorageDirectory(), "app_spoon-screenshots")
        val classDirectory = File(spoonDirectory, className)
        val screenshotDirectory = File(classDirectory, methodName)
        this.mDefaultScreenshotPath = screenshotDirectory
    }
}
