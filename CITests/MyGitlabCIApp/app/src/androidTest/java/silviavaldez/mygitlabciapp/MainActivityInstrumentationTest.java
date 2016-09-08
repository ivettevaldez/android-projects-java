package silviavaldez.mygitlabciapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Simple Test class for the MainActivity.java class.
 * Example taken from: https://io2015codelabs.appspot.com/codelabs/android-studio-testing#8
 * Created by Silvia Valdez on 8/25/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {

    private static final String STRING_TO_BE_TYPED = "Ivette";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sayHello() {
        // Type the selected string in the editText
        onView(withId(R.id.main_edit_name)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // Perform click in the button
        onView(withText("Say hello!")).perform(click());

        // Compare results
        String expectedText = String.format("Hello, %s!", STRING_TO_BE_TYPED);
        onView(withId(R.id.main_text_hello)).check(matches(withText(expectedText)));
    }

}
