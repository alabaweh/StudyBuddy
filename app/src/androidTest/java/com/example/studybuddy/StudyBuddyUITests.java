package com.example.studybuddy;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.studybuddy.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StudyBuddyUITests {
    private static final String TEST_EMAIL = "test2@test.com";
    private static final String TEST_PASSWORD = "Today@123";

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth.getInstance().signOut();

        // Wait for Firebase operations
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform login
        onView(withId(R.id.emailInput))
                .perform(replaceText(TEST_EMAIL), closeSoftKeyboard());

        onView(withId(R.id.passwordInput))
                .perform(replaceText(TEST_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.loginButton))
                .perform(click());

        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testNavigationFlow() {
        // Test Home
        onView(allOf(
                withId(R.id.navigation_home),
                isDescendantOfA(withId(R.id.bottomNav))
        ))
                .perform(click());

        // Test Calendar
        onView(allOf(
                withId(R.id.navigation_calendar),
                isDescendantOfA(withId(R.id.bottomNav))
        ))
                .perform(click());

        // Wait after navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSessionFeatures() {
        // Navigate to calendar
        onView(allOf(
                withId(R.id.navigation_calendar),
                isDescendantOfA(withId(R.id.bottomNav))
        ))
                .perform(click());

        // Wait after navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGroupFeatures() {
        onView(allOf(
                withId(R.id.navigation_home),
                isDescendantOfA(withId(R.id.bottomNav))
        ))
                .perform(click());
    }
}