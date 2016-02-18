package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;

import static junit.framework.Assert.assertEquals;
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
    private ActivityController<RecipeListActivity> controller;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeListActivity activity = new RecipeListActivity(){
            @Override
            public RecipesAdapter getAdapter() {
                return adapter;
            }

            @Override
            public RecipeListPresenter getPresenter() {
                return presenter;
            }
        };

        controller = ActivityController.of(Robolectric.getShadowsAdapter(), activity).create();
        //controller.create();
        view = (RecipeListView)controller.get();
    }

    @Test
    public void onCreateCalled() {
        verify(presenter).onCreate();
        verify(presenter).getRecipes();
    }

    @Test
    public void onDestroyCalled() {
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



    //logout, navigateToMainScreen, onItemClick, onFavClick, onDeleteClick
}
