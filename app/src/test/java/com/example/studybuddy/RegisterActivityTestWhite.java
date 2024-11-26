package com.example.studybuddy;

import static org.junit.Assert.*;

import com.example.studybuddy.activities.RegisterActivity;

import org.junit.Before;
import org.junit.Test;

public class RegisterActivityTestWhite {

    private RegisterActivity registerActivity;

    @Before
    public void setUp() {
        registerActivity = new RegisterActivity();
    }

    @Test
    public void testValidateInputs_Success() {
        boolean isValid = registerActivity.validateInputs("John Doe", "johndoe@example.com", "Password123!", "Password123!");
        assertTrue(isValid);
    }

    @Test
    public void testValidateInputs_InvalidName() {
        boolean isValid = registerActivity.validateInputs("", "johndoe@example.com", "Password123!", "Password123!");
        assertFalse(isValid);
    }

    @Test
    public void testValidateInputs_InvalidEmail() {
        boolean isValid = registerActivity.validateInputs("John Doe", "invalid-email", "Password123!", "Password123!");
        assertFalse(isValid);
    }

    @Test
    public void testValidateInputs_PasswordMismatch() {
        boolean isValid = registerActivity.validateInputs("John Doe", "johndoe@example.com", "Password123!", "Password456!");
        assertFalse(isValid);
    }


}

