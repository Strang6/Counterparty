package com.strang6.counterparty;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.strang6.counterparty.main.MainActivity;
import com.strang6.counterparty.resent.RecentActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Strang6 on 21.01.2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void emptyTextViewTest() {
        onView(withId(R.id.counterpartyTextView))
                .check(matches(allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        isFocusable(), isClickable(), withText(""), withHint(R.string.name_inn))));
    }

    @Test
    public void recentButtonTest() {
        onView(withId(R.id.recentButton))
                .check(matches(allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        isClickable(), withText(R.string.recent_counterparty))));
    }

    @Test
    public void inputTest() {
        String query = "test";
        onView(withId(R.id.counterpartyTextView)).perform(typeText(query));
        closeSoftKeyboard();
        onView(withId(R.id.counterpartyTextView)).check(matches(withText(query)));
    }

    @Test
    public void recentButtonClickTest() {
        onView(withId(R.id.recentButton)).perform(click());
        Intents.intended(hasComponent(RecentActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
