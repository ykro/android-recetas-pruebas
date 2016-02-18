package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.galileo.android.facebookrecipes.entities.Recipe;

import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
public class InteractorsTest {
    @Mock
    private RecipeListRepository repository;
    private Recipe recipe = new Recipe();

    private RecipeListInteractor listInteractor;
    private StoredRecipesInteractor storedInteractor;


    @Before
    public void setupInteractors() {
        MockitoAnnotations.initMocks(this);
        listInteractor = new RecipeListInteractorImpl(repository);
        storedInteractor = new StoredRecipesInteractorImpl(repository);
    }

    @Test
    public void recipeListExecute_callsRepository() {
        listInteractor.execute();
        verify(repository).getSavedRecipes();
    }

    @Test
    public void storedRecipesExecuteUpdate_callsRepository() {
        storedInteractor.executeUpdate(recipe);
        verify(repository).updateRecipe(recipe);
    }

    @Test
    public void storedRecipesExecuteDelete_callsRepository() {
        storedInteractor.executeDelete(recipe);
        verify(repository).removeRecipe(recipe);
    }
}
