package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.api.RecipeSearchResponse;
import edu.galileo.android.facebookrecipes.api.RecipeService;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeMainRepositoryTest extends BaseTest {
    @Mock
    private EventBus eventBus;

    @Mock
    private RecipeService service;

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
    public void getNextRecipeCalled_apiServiceSuccessCallEventPosted() {
        final Recipe recipe = new Recipe();
        recipe.setRecipeId("id");
        recipe.setSourceURL("http://google.com");

        int recipePage = repository.getRecipePage();

        when(service.search(BuildConfig.FOOD_API_KEY,
                            RecipeMainRepository.RECENT_SORT,
                            RecipeMainRepository.COUNT, recipePage)).thenReturn(buildSuccessCall(recipe));

        repository.getNextRecipe();

        verify(service).search(BuildConfig.FOOD_API_KEY, RecipeMainRepository.RECENT_SORT, RecipeMainRepository.COUNT, recipePage);
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();
        assertEquals(RecipeMainEvent.NEXT_EVENT, event.getType());
        assertNull(event.getError());
        assertEquals(recipe, event.getRecipe());
    }

    @Test
    public void getNextRecipeCalled_apiServiceFailedCallEventPosted() {
        int recipePage = repository.getRecipePage();
        String error = "error";
        when(service.search(BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT, recipePage)).thenReturn(buildFailedCall(error));

        repository.getNextRecipe();

        verify(service).search(BuildConfig.FOOD_API_KEY, RecipeMainRepository.RECENT_SORT, RecipeMainRepository.COUNT, recipePage);
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();
        assertEquals(RecipeMainEvent.NEXT_EVENT, event.getType());
        assertNull(event.getRecipe());
        assertEquals(error, event.getError());
    }

    private Call<RecipeSearchResponse> buildSuccessCall(final Recipe recipe) {
        return new Call<RecipeSearchResponse>() {
            @Override
            public Response<RecipeSearchResponse> execute() throws IOException {
                RecipeSearchResponse response = new RecipeSearchResponse();
                response.setCount(1);
                response.setRecipes(Arrays.asList(new Recipe[]{recipe}));

                return Response.success(response);
            }

            @Override
            public void enqueue(Callback<RecipeSearchResponse> callback) {
                try {
                    callback.onResponse(this, execute());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<RecipeSearchResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }

    private Call<RecipeSearchResponse> buildFailedCall(final String errorMsg){
        return new Call<RecipeSearchResponse>() {
            @Override
            public Response<RecipeSearchResponse> execute() throws IOException {
                return Response.error(null, null);
            }

            @Override
            public void enqueue(Callback<RecipeSearchResponse> callback) {
                callback.onFailure(this, new Throwable(errorMsg));
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<RecipeSearchResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }
}
