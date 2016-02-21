package edu.galileo.android.facebookrecipes.api;

/**
 * Created by ykro.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Random;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainRepository;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeServiceTest extends BaseTest {

    RecipeService service;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        RecipeClient client = new RecipeClient();
        service = client.getRecipeService();
    }

    @Test
    public void doSearch_getRecipeFromBackend() throws IOException{
        int recipePage = 1;
        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY, RecipeMainRepository.RECENT_SORT, RecipeMainRepository.COUNT, recipePage);

        Response<RecipeSearchResponse> response = call.execute();
        assertTrue(response.isSuccess());

        RecipeSearchResponse recipeSearchResponse = response.body();
        assertEquals(1, recipeSearchResponse.getCount());
        Recipe recipe = recipeSearchResponse.getFirstRecipe();
        assertNotNull(recipe);
    }

    @Test
    public void doSearch_getRandomRecipeFromBackend() throws IOException{
        int recipePage = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY, RecipeMainRepository.RECENT_SORT, RecipeMainRepository.COUNT, recipePage);

        Response<RecipeSearchResponse> response = call.execute();
        assertTrue(response.isSuccess());

        RecipeSearchResponse recipeSearchResponse = response.body();
        if (recipeSearchResponse.getCount() == 1) {
            Recipe recipe = recipeSearchResponse.getFirstRecipe();
            assertNotNull(recipe);
        } else {
            System.out.println("invalid recipe, trying again");
            doSearch_getRecipeFromBackend();
        }
    }
}
