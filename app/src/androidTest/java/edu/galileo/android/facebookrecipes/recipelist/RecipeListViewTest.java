package edu.galileo.android.facebookrecipes.recipelist;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ykro.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListViewTest {
    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule =
                                                new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


}
