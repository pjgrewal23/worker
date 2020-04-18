package com.example.worker;


import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.worker", appContext.getPackageName());
    }

    @Test
    public void testSwipe() {
        onView(withId(R.id.frame)).perform(swipeLeft());
        onView(withId(R.id.frame)).perform(swipeRight());
        onView(withId(R.id.frame)).perform(swipeLeft());
        onView(withId(R.id.frame)).perform(swipeRight());
        onView(withId(R.id.frame)).perform(swipeLeft());
        onView(withId(R.id.frame)).perform(swipeRight());
    }

    @Test
    public void testGoToMatches() {
        onView(withId(R.id.matches)).perform(click());
        onView(withId(R.id.recycler)).perform(click());
        onView(withId(R.id.msg)).perform(typeText("How discrete is your service?"));
        onView(withId(R.id.sendBtn)).perform(click());
        onView(withId(R.id.msg)).perform(typeText("and how much do you charge to get sardine oil out of carpet?"));
        onView(withId(R.id.sendBtn)).perform(click());
    }

    @Test
    public void ztestGoToSettings() {
        onView(withId(R.id.settinsBtn)).perform(click());
        onView(withId(R.id.description)).perform(typeText("Looking for new husband who loves sardine oil and tigers"));
        onView(withId(R.id.phone)).perform(typeText("4205556969"));
        onView(withId(R.id.name)).perform(typeText(" Baskin"));
        onView(withId(R.id.name)).check(matches(withText("Carol Baskin")));
    }

    @Test
    public void testSignOut() {
        onView(withId(R.id.signOut)).perform(click());
    }

    /*
    * Below is testing switching tasks for a worker
    * Either the above tests can run or below ones but not both
    *

    @Test
    public void testAddSetTask() {
        onView(withId(R.id.settinsBtn)).perform(click());
        onView(withId(R.id.editText3)).perform(typeText("Git Baskin"));
        onView(withId(R.id.editText2)).perform(typeText("Git Tigers"));
        onView(withId(R.id.editText)).perform(typeText("Git Money"));
    }*/

}
