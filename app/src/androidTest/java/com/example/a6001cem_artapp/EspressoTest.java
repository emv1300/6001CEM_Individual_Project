package com.example.a6001cem_artapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(
                    MainActivity.class);//this slice of code has been copied from the unit test lab

    @Test
    public void attemptLoginWithCorrectCredentials(){
        onView(withId(R.id.emailLogin)).perform(typeText("emv20000@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("Pass11!!"), closeSoftKeyboard());
        onView(withId(R.id.loginBT)).perform(click());
    }

    @Test
    public void attemptLoginWithWrongCredentials1(){
        onView(withId(R.id.emailLogin)).perform(typeText("email@service.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("demopass"), closeSoftKeyboard());
        onView(withId(R.id.loginBT)).perform(click());
    }

    @Test
    public void attemptLoginWithWrongCredentials2(){
        onView(withId(R.id.emailLogin)).perform(typeText("text"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("demopass"), closeSoftKeyboard());
        onView(withId(R.id.loginBT)).perform(click());
    }

    @Test
    public void navigateToRegisterAndBack(){
        onView(withId(R.id.regTVlogin)).perform(click());
        onView(withId(R.id.regBackBT)).perform(click());
    }

    @Test
    public void refresh(){
        onView(withId(R.id.swiperefresh)).perform(swipeUp());
    }
}
