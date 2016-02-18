package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;

import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ViewTest extends BaseRecipeListTest {
    @Mock
    RecipesAdapter adapter;

    @Mock
    RecipeListPresenter presenter;

    RecipeListView view;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void onCreate_SubscribedToEventBus() {
        RecipeListActivity activity = Robolectric.buildActivity(RecipeListActivity.class).create().get();
        /*
        *
        setupInjection();
        setupRecyclerView();
        * */

        verify(presenter).onCreate();
        verify(presenter).getRecipes();
    }

    /*
    @Test
    public void onDestroy_UnsubscribedToEventBusAndViewDestroyed() throws NoSuchFieldException {
        assertNotNull(presenter.getView());
        presenter.onDestroy();
        verify(eventBus).unregister(presenter);
        assertNull(presenter.getView());
    }

    @Test
    public void getRecipes() {
        presenter.getRecipes();
        verify(listInteractor).execute();
    }

    @Test
    public void clickOnFavorite_UpdateRecipe() {
        RECIPE.setFavorite(true);
        updateRecipe(RECIPE);
    }

    @Test
    public void clickNoNOnFavorite_UpdateRecipe() {
        RECIPE.setFavorite(false);
        updateRecipe(RECIPE);
    }

    private void updateRecipe(Recipe recipe) {
        boolean favorite = recipe.getFavorite();
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedInteractor).executeUpdate(recipeArgumentCaptor.capture());
        assertEquals(!favorite, recipeArgumentCaptor.getValue().getFavorite());
    }

    @Test
    public void removeRecipe() {
        presenter.removeRecipe(RECIPE);
        verify(storedInteractor).executeDelete(RECIPE);
    }

    @Test
    public void onReadEvent() {
        List<Recipe> recipeList = Arrays.asList(new Recipe[]{
                new Recipe(),
                new Recipe(),
                new Recipe()});

        EVENT.setType(RecipeListEvent.READ_EVENT);
        EVENT.setRecipes(recipeList);

        presenter.onEventMainThread(EVENT);
        verify(view).setRecipes(recipeList);
    }

    @Test
    public void onUpdateEvent() {
        EVENT.setType(RecipeListEvent.UPDATE_EVENT);

        presenter.onEventMainThread(EVENT);
        verify(view).recipeUpdated();
    }

    @Test
    public void onDeleteEvent() {
        EVENT.setType(RecipeListEvent.DELETE_EVENT);
        EVENT.setRecipes(Arrays.asList(new Recipe[]{RECIPE}));

        presenter.onEventMainThread(EVENT);
        verify(view).recipeDeleted(RECIPE);
    }
    */
}
