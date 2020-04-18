package com.example.worker;


import android.content.Context;

import org.junit.Before;
import org.junit.Rule;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
    public void testLogin() {


        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.email)).perform(typeText("jacobzenger@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("Testing1234"));
        onView(withId(R.id.loginButton)).perform(click());

        /* Go into settings and check that the name is bob */
        onView(withId(R.id.settinsBtn)).perform(click());
        assertEquals(onView(withId(R.id.name)), "bob");
    }

    @Test
    public void testRegister() {
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.name)).perform(typeText("bob"));
        onView(withId(R.id.email)).perform(typeText("jacobzenger@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("Testing1234"));
        onView(withId(R.id.registerButton)).perform(click());

        /* Go into settings and check that the name is bob */
        onView(withId(R.id.settinsBtn)).perform(click());
        assertEquals(onView(withId(R.id.name)), "bob");
    }

/*    @Test
    public void testRightSwipe() {

    }

    @Test
    public void testLeftSwipe() {

    }*/
}
