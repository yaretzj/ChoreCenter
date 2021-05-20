package com.cse403chorecenter.chorecenterapp;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ChooseAccountTypeTest {

    @Rule
    public ActivityScenarioRule<ChooseAccountType> activityRule
            = new ActivityScenarioRule<>(ChooseAccountType.class);

    @Test
    public void chooseParentAccount() {
        // press the button.
        onView(withId(R.id.parentAccountBtn))
                .perform(ViewActions.click());
    }@Test
    public void chooseKidAccount() {
        // press the button.
        onView(withId(R.id.kidAccountBtn))
                .perform(ViewActions.click());
    }
}