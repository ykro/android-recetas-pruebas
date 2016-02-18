package edu.galileo.android.facebookrecipes.recipelist;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.EventBus;
import edu.galileo.android.facebookrecipes.recipelist.events.RecipeListEvent;

/**
 * Created by ykro.
 */
public class RecipeListRepositoryImpl implements RecipeListRepository {
    private EventBus eventBus;

    public RecipeListRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getSavedRecipes() {
        List<Recipe> recipeList = new Select()
                                    .from(Recipe.class)
                                    .queryList();
        post(RecipeListEvent.READ_EVENT, recipeList);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipe.update();
        post(RecipeListEvent.UPDATE_EVENT);
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        recipe.delete();
        post(RecipeListEvent.DELETE_EVENT, Arrays.asList(recipe));
    }

    private void post(int type, List<Recipe> recipes) {
        RecipeListEvent event = new RecipeListEvent();
        event.setRecipes(recipes);
        event.setType(type);
        eventBus.post(event);
    }

    private void post(int type) {
        post(type, null);
    }

}
