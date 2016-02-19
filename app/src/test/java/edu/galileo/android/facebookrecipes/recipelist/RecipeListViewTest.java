package edu.galileo.android.facebookrecipes.recipelist;

import android.content.ComponentName;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.login.ui.LoginActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainActivity;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeListViewTest extends BaseRecipeListTest {
    @Mock
    RecipesAdapter adapter;

    @Mock
    RecipeListPresenter presenter;

    private RecipeListView view;
    private RecipeListActivity activity;

    private ShadowActivity shadowActivity;
    private ActivityController<RecipeListActivity> controller;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeListActivity listActivity = new RecipeListActivity(){
            @Override
            public RecipesAdapter getAdapter() {
                return adapter;
            }

            @Override
            public RecipeListPresenter getPresenter() {
                return presenter;
            }
        };

        controller = ActivityController.of(Robolectric.getShadowsAdapter(), listActivity).create();
        activity = controller.get();

        view = (RecipeListView)activity;
        shadowActivity = shadowOf(activity);

    }

    @Test
    public void onActivityCreated_getsRecipesFromDataSource() {
        verify(presenter).onCreate();
        verify(presenter).getRecipes();
    }

    @Test
    public void onActivityDestroyed_destroyPresenter() {
        controller.destroy();
        verify(presenter).onDestroy();
    }

    @Test
    public void getRecipesFromPresenter_setInAdapter() {
        List<Recipe> recipeList = Arrays.asList(new Recipe[]{
                new Recipe(),
                new Recipe(),
                new Recipe()});

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        view.setRecipes(recipeList);
        verify(adapter).setRecipes(argumentCaptor.capture());
        assertEquals(recipeList.size(), argumentCaptor.getValue().size());
    }

    @Test
    public void recipeUpdatedFromPresenter_adapterUpdated() {
        view.recipeUpdated();
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void recipeDeletedFromPresenter_adapterUpdated() {
        Recipe recipe = new Recipe();
        view.recipeDeleted(recipe);
        verify(adapter).removeRecipe(recipe);
    }

    @Test
    public void logoutMenuClick() {
        RoboMenuItem menuItem = new RoboMenuItem(R.id.action_logout);
        activity.onOptionsItemSelected(menuItem);
        //shadowActivity.clickMenuItem(R.id.action_logout);
        Intent intent = shadowActivity.peekNextStartedActivityForResult().intent;
        assertThat(intent.getComponent()).isEqualTo(new ComponentName(controller.get(), LoginActivity.class));
    }

    @Test
    public void mainMenuClick() {
        RoboMenuItem menuItem = new RoboMenuItem(R.id.action_main);
        activity.onOptionsItemSelected(menuItem);

        Intent intent = shadowActivity.peekNextStartedActivityForResult().intent;
        assertThat(intent.getComponent()).isEqualTo(new ComponentName(controller.get(), RecipeMainActivity.class));
    }
}
