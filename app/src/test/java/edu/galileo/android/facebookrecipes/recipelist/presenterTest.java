package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.EventBus;
import edu.galileo.android.facebookrecipes.recipelist.events.RecipeListEvent;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
public class PresenterTest {
    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeListView view;
    @Mock
    private RecipeListInteractor listInteractor;
    @Mock
    private StoredRecipesInteractor storedInteractor;

    private Recipe RECIPE = new Recipe();
    private RecipeListEvent EVENT = new RecipeListEvent();

    private RecipeListPresenter presenter;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new RecipeListPresenterImpl(eventBus, view, listInteractor, storedInteractor);
    }

    @Test
    public void onCreate_SubscribedToEventBus() {
        presenter.onCreate();
        verify(eventBus).register(presenter);
    }

    @Test
    public void onDestroy_UnsubscribedToEventBusAndViewDestroyed() {
        presenter.onDestroy();
        verify(eventBus).unregister(presenter);
        assertThat(presenter.getView(), is(nullValue()));
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
}
