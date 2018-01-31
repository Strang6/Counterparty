package com.strang6.counterparty;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.resent.RecentActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Strang6 on 21.01.2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecentActivityTest {
    @Rule
    public ActivityTestRule<RecentActivity> testRule = new ActivityTestRule<>(RecentActivity.class, true, true);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void recyclerViewDisplayedTest() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewScrollTest() {
        onView(withId(R.id.recyclerView))
                .perform(scrollToPosition(5))
                .perform(scrollToPosition(15))
                .perform(scrollToPosition(50))
                .perform(scrollToPosition(1));
    }

    @Test
    public void onItemClickTest() {
        onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition(2, click()));
        Intents.intended(hasComponent(DetailsActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
