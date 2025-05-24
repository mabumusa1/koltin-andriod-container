package karage.app

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import android.widget.EditText

import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testHelloWorldTextIsDisplayed() {
        // Test that the Hello World text is displayed
        onView(withId(R.id.greeting)).check(matches(withText("Hello World!")))
    }
    
    @Test
    fun testAdditionFunctionality() {
        // Enter values in the input fields
        onView(withId(R.id.inputNum1)).perform(typeText("5"), closeSoftKeyboard())
        onView(withId(R.id.inputNum2)).perform(typeText("3"), closeSoftKeyboard())
        
        // Click the addition button
        onView(withId(R.id.buttonAdd)).perform(click())
        
        // Verify the result
        onView(withId(R.id.resultText)).check(matches(withText("Result: 8")))
    }
    
    @Test
    fun testSubtractionFunctionality() {
        // Enter values in the input fields
        onView(withId(R.id.inputNum1)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.inputNum2)).perform(typeText("4"), closeSoftKeyboard())
        
        // Click the subtraction button
        onView(withId(R.id.buttonSubtract)).perform(click())
        
        // Verify the result
        onView(withId(R.id.resultText)).check(matches(withText("Result: 6")))
    }
    
    @Test
    fun testMultiplicationFunctionality() {
        // Enter values in the input fields
        onView(withId(R.id.inputNum1)).perform(typeText("7"), closeSoftKeyboard())
        onView(withId(R.id.inputNum2)).perform(typeText("3"), closeSoftKeyboard())
        
        // Click the multiplication button
        onView(withId(R.id.buttonMultiply)).perform(click())
        
        // Verify the result
        onView(withId(R.id.resultText)).check(matches(withText("Result: 21")))
    }
    
    @Test
    fun testDivisionFunctionality() {
        // Enter values in the input fields
        onView(withId(R.id.inputNum1)).perform(typeText("20"), closeSoftKeyboard())
        onView(withId(R.id.inputNum2)).perform(typeText("4"), closeSoftKeyboard())
        
        // Click the division button
        onView(withId(R.id.buttonDivide)).perform(click())
        
        // Verify the result
        onView(withId(R.id.resultText)).check(matches(withText("Result: 5")))
    }
}
