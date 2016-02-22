package edu.galileo.android.facebookrecipes.recipelist;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.ImageLoader;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.facebookrecipes.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowRecyclerViewAdapter.class})
public class RecipesAdapterTest extends BaseTest {
    @Mock
    private List<Recipe> recipes;
    @Mock
    private ImageLoader imageLoader;
    @Mock
    private OnItemClickListener onItemClickListener;

    private Recipe recipe;
    private RecipesAdapter adapter;
    private ShadowRecyclerViewAdapter shadowAdapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        recipe = new Recipe();
        recipe.setSourceURL("http://google.com");

        adapter = new RecipesAdapter(recipes, imageLoader, onItemClickListener);
        shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(adapter);

        Activity activity = Robolectric.setupActivity(Activity.class);
        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.setAdapter(adapter);
    }

    @Test
    public void setRecipes_itemCountMatch() {
        List<Recipe> recipeList = Arrays.asList(new Recipe[]{recipe, recipe, recipe});
        when(recipes.size()).thenReturn(recipeList.size());

        adapter.setRecipes(recipeList);

        assertEquals(adapter.getItemCount(), recipeList.size());
    }

    @Test
    public void removeRecipe_isRemovingFromAdapter() {
        adapter.removeRecipe(recipe);
        verify(recipes).remove(recipe);
    }

    @Test
    public void onItemClick_listenerCalled() {
        int positionToClick = 0;

        when(recipes.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClick(positionToClick);

        verify(onItemClickListener).onItemClick(recipe);
    }

    @Test
    public void ViewHolderRendersTitle() {
        int positionToShow = 0;
        recipe.setTitle("title");
        when(recipes.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);

        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToShow);
        TextView txtRecipeName = (TextView) viewHolder.itemView.findViewById(R.id.txtRecipeName);
        assertEquals(recipe.getTitle(),txtRecipeName.getText().toString());
    }

    @Test
    public void onFavoriteClick_viewHolderImageChanged() {
        int positionToClick = 0;
        boolean favorite = true;
        recipe.setFavorite(favorite);
        when(recipes.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgFav);

        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToClick);
        ImageButton imgFav = (ImageButton) viewHolder.itemView.findViewById(R.id.imgFav);
        boolean favoriteFromVH = (Boolean) imgFav.getTag();

        assertEquals(favorite,favoriteFromVH);
    }

    @Test
    public void onNonFavoriteClick_viewHolderImageChanged() {
        int positionToClick = 0;
        boolean favorite = false;
        recipe.setFavorite(favorite);
        when(recipes.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgFav);

        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToClick);
        ImageButton imgFav = (ImageButton) viewHolder.itemView.findViewById(R.id.imgFav);
        boolean favoriteFromVH = (Boolean) imgFav.getTag();

        assertEquals(favorite,favoriteFromVH);
    }

    @Test
    public void onDeleteClick_listenerCalled() {
        int positionToClick = 0;
        when(recipes.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgDelete);

        verify(onItemClickListener).onDeleteClick(recipe);
    }

    public void onfbShareBind_shareContentSet() {
        int positionToShow = 0;
        when(recipes.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToShow);

        SendButton fbSend = (SendButton) viewHolder.itemView.findViewById(R.id.fbSend);
        ShareContent shareContent = fbSend.getShareContent();
        assertNotNull(shareContent);
        String shareUrl = shareContent.getContentUrl().toString();
        assertEquals(recipe.getSourceURL(), shareUrl);
    }

    public void onfbSendBind_shareContentSet() {
        int positionToShow = 0;
        when(recipes.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToShow);

        ShareButton fbShare = (ShareButton) viewHolder.itemView.findViewById(R.id.fbShare);
        ShareContent shareContent = fbShare.getShareContent();
        assertNotNull(shareContent);
        String shareUrl = shareContent.getContentUrl().toString();
        assertEquals(recipe.getSourceURL(), shareUrl);
    }
}
