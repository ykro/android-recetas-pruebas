package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.ImageLoader;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;

import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipesAdapterTest extends BaseRecipeListTest {
    @Mock
    List<Recipe> recipes;
    @Mock
    ImageLoader imageLoader;
    @Mock
    OnItemClickListener onItemClickListener;

    private RecipesAdapter adapter;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        adapter = new RecipesAdapter(recipes, imageLoader, onItemClickListener);
    }

    @Test
    public void removeRecipe_isRemovingFromAdapter() {
        Recipe recipe = new Recipe();
        adapter.removeRecipe(recipe);
        verify(recipes).remove(recipe);
    }
}
