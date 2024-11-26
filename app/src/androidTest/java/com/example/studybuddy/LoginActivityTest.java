package com.example.studybuddy;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.studybuddy.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static java.util.function.Predicate.not;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private FirebaseAuth mockAuth;

    @Before
    public void setUp() {
        // Mock FirebaseAuth
        mockAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testValidLogin() {
        ActivityScenario.launch(LoginActivity.class);

        // Enter valid email and password
        onView(withId(R.id.emailInput)).perform(typeText("luka.brnetic@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("Lukaerik1"), closeSoftKeyboard());

        // Click login button
        onView(withId(R.id.loginButton)).perform(click());

        // Verify redirection to DashboardActivity
        onView(withId(R.id.fragmentContainer)) // Assuming there's an ID for the dashboard layout
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidEmailLogin() {
        ActivityScenario.launch(LoginActivity.class);

        // Enter invalid email and valid password
        onView(withId(R.id.emailInput)).perform(typeText("invalidemail"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("Password123"), closeSoftKeyboard());

        // Click login button
        onView(withId(R.id.loginButton)).perform(click());

        // Verify that the email input field shows an error
        onView(withId(R.id.emailInput)).check(matches(hasErrorText("Please enter a valid email")));
    }

    @Test
    public void testEmptyPasswordLogin() {
        ActivityScenario.launch(LoginActivity.class);

        // Enter valid email and leave password blank
        onView(withId(R.id.emailInput)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText(""), closeSoftKeyboard());

        // Click login button
        onView(withId(R.id.loginButton)).perform(click());

        // Verify that the password input field shows an error
        onView(withId(R.id.passwordInput)).check(matches(hasErrorText("Password cannot be empty")));
    }

    @Test
    public void testFailedLogin() {
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        scenario.onActivity(activity -> {
            // Enter valid email but incorrect password
            onView(withId(R.id.emailInput)).perform(typeText("test@example.com"), closeSoftKeyboard());
            onView(withId(R.id.passwordInput)).perform(typeText("WrongPassword"), closeSoftKeyboard());

            onView(withId(R.id.loginButton)).perform(click());

            // Verify the toast message using the decor view of the activity's window
            onView(withText("Login failed: Invalid credentials")).check(matches(isDisplayed()));
        });
    }

    @Test
    public void testEmptyFields() {
        ActivityScenario.launch(LoginActivity.class);

        // Leave email and password blank
        onView(withId(R.id.loginButton)).perform(click());

        // Verify that both fields show error messages
        onView(withId(R.id.emailInput)).check(matches(hasErrorText("Please enter a valid email")));

    }
}

