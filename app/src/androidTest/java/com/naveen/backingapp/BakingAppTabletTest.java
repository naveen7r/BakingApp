package com.naveen.backingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.PlayerView;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BakingAppTabletTest {

    @Rule
    public IntentsTestRule<RecipeListActivity> menuActivityTestRule = new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void A_checkMain(){
        onView(withId(R.id.llroot))
                .check(matches(isDisplayed()));
    }

    @Test
    public void B_performclick(){
        onView(withId(R.id.rvRecipeList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.rlReceipeListRoot))
                .check(matches(isDisplayed()));
    }

    @Test
    public  void C_selectAStep(){
        B_performclick();

        onView(allOf(withId(R.id.item_list), isDisplayed())).perform(swipeUp());

        onView(allOf(withId(R.id.item_detail_container), isDisplayed()));


        onView(withId(R.id.item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        intended(hasComponent(RecipeStepsListActivity.class.getName()));
    }

    @Test
    public void D_testExoPlayer(){
        C_selectAStep();

        onView(withId(R.id.exoPlayer))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.exoPlayer),
                withClassName(is(PlayerView.class.getName()))));
    }

    @Test
    public void E_testDescription(){
        D_testExoPlayer();

        onView(withId(R.id.tvRecipieInstruction))
                .check(matches(not(withText(""))));
    }


}
