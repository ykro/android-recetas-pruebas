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
import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
public class RecipesListPresenterTest {
    private static List<Recipe> ONE_RECIPE = Arrays.asList(new Recipe[]{
                                                                new Recipe("ID3")});

    private static List<Recipe> RECIPES = Arrays.asList(new Recipe[]{
                                                                new Recipe("ID1"),
                                                                new Recipe("ID2"),
                                                                new Recipe("ID3")});

    private static RecipeListEvent READ_EVENT = new RecipeListEvent(RecipeListEvent.READ_EVENT, RECIPES);
    private static RecipeListEvent UPDATE_EVENT = new RecipeListEvent(RecipeListEvent.UPDATE_EVENT, null);
    private static RecipeListEvent DELETE_EVENT = new RecipeListEvent(RecipeListEvent.DELETE_EVENT, ONE_RECIPE);

    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeListView view;
    @Mock
    private RecipeListInteractor listInteractor;
    @Mock
    private StoredRecipesInteractor storedInteractor;

    private RecipeListPresenter presenter;
    private ArgumentCaptor<RecipeListView> recipeListViewArgumentCaptor = ArgumentCaptor.forClass(RecipeListView.class);
    @Before
    public void setupNotesPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new RecipeListPresenterImpl(eventBus, view, listInteractor, storedInteractor);
    }

    @Test
    public void onDestroy_UnsubscribedToEventBusAndViewDestroyed() throws Exception {
        presenter.onDestroy();
        assertEquals(null, presenter.getView());
    }

    @Test
    public void getRecipesFromInteractor_LoadIntoView() {
        presenter.getRecipes();
        verify(listInteractor).execute();

        presenter.onEventMainThread(READ_EVENT);
        verify(view).setRecipes(RECIPES);
    }

    @Test
    public void clickOnFavorite_UpdateRecipe() {
        Recipe recipe = new Recipe();
        boolean favorite = true;
        recipe.setFavorite(favorite);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedInteractor).executeUpdate(recipeArgumentCaptor.capture());
        assertEquals(!favorite, recipeArgumentCaptor.getValue().getFavorite());

        presenter.onEventMainThread(UPDATE_EVENT);
        verify(view).recipeUpdated();
    }

    @Test
    public void clickOnRemove_UpdateView() {
        Recipe recipe = ONE_RECIPE.get(0);
        storedInteractor.executeDelete(recipe);
        verify(storedInteractor).executeDelete(recipe);

        presenter.onEventMainThread(DELETE_EVENT);
        verify(view).recipeDeleted(recipe);
    }
}
