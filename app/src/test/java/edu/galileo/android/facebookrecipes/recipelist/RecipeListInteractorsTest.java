package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
public class RecipeListInteractorsTest extends BaseRecipeListTest {
    @Mock
    private RecipeListRepository repository;

    private RecipeListInteractor listInteractor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        listInteractor = new RecipeListInteractorImpl(repository);
    }

    @Test
    public void recipeListExecute_callsRepository() {
        listInteractor.execute();
        verify(repository).getSavedRecipes();
    }
}
