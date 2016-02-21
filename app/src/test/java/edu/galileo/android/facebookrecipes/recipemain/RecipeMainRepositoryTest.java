package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.api.RecipeService;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeMainRepositoryTest extends BaseTest {
    @Mock
    private EventBus eventBus;

    @Mock
    RecipeService service;

    private RecipeMainRepository repository;
    private ArgumentCaptor<RecipeMainEvent> recipeMainEventArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new RecipeMainRepositoryImpl(eventBus, service);
        recipeMainEventArgumentCaptor = ArgumentCaptor.forClass(RecipeMainEvent.class);
    }

    @Test
    public void saveRecipeCalled_eventPosted() {
        Recipe recipe = new Recipe();
        int randomInt = (new Random()).nextInt(100000);
        recipe.setRecipeId("__id__" + randomInt);

        repository.saveRecipe(recipe);

        assertTrue(recipe.exists());
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();
        assertEquals(RecipeMainEvent.SAVE_EVENT, event.getType());
        assertNull(event.getError());
        assertNull(event.getRecipe());
        recipe.delete();
    }

    @Test
    public void getNextRecipeCalled_apiServiceMockCalled() {
        repository.getNextRecipe();
        int recipePage = repository.getRecipePage();
        verify(service).search(BuildConfig.FOOD_API_KEY, RecipeMainRepository.RECENT_SORT, RecipeMainRepository.COUNT, recipePage);
    }
}
