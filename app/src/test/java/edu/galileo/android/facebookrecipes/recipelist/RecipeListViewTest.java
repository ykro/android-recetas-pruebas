package edu.galileo.android.facebookrecipes.recipelist;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
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
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainActivity;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
    private OnItemClickListener onItemClickListener;

    private RecyclerView recyclerView;
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
        onItemClickListener = (OnItemClickListener)activity;

        shadowActivity = Shadows.shadowOf(activity);
        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);

        recyclerView.measure(0, 0);
        recyclerView.layout(0, 0, 100, 10000);
    }

    @Test
    public void onActivityCreated() {
        verify(presenter).onCreate();
        verify(presenter).getRecipes();
    }

    @Test
    public void onActivityDestroyed() {
        controller.destroy();
        verify(presenter).onDestroy();
    }

    @Test
    public void setRecipesFromPresenter() {
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
    public void recipeUpdatedFromPresenter() {
        view.recipeUpdated();
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void recipeDeletedFromPresenter() {
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

    @Test
    public void onItemClick() {
        //ShadowListView shadowListView =


//activity.onItemClick();
    }


    //onItemClick, onFavClick, onDeleteClick
/*

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceURL()));
        startActivity(intent);
    }

    @Override
    public void onFavClick(Recipe recipe) {
        presenter.toggleFavorite(recipe);
    }

    @Override
    public void onDeleteClick(Recipe recipe) {
        presenter.removeRecipe(recipe);
    }
 */

}
