package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;

import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
public class RecipeMainPresenterTest extends BaseTest {
    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeMainView view;
    @Mock
    private SaveRecipeInteractor saveRecipe;
    @Mock
    private GetNextRecipeInteractor getNextRecipe;

    private RecipeMainPresenter presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new RecipeMainPresenterImpl(eventBus, view, saveRecipe, getNextRecipe);
    }

    @Test
    public void onCreate_SubscribedToEventBus() {
        presenter.onCreate();
        verify(eventBus).register(presenter);
    }

    @Test
    public void onDestroy_UnsubscribedToEventBusAndViewDestroyed() throws NoSuchFieldException {
        presenter.onDestroy();

        assertNull(presenter.getView());
        verify(eventBus).unregister(presenter);
    }

    @Test
    public void saveRecipe_executeSaveInteractor() {
        Recipe recipe = new Recipe();

        presenter.saveRecipe(recipe);

        verify(view).saveAnimation();
        verify(view).hideUIElements();
        verify(view).showProgress();
        verify(saveRecipe).execute(recipe);
    }

    @Test
    public void dismissRecipe_executeGetNextRecipeInteractor() {
        presenter.dismissRecipe();

        verify(view).dismissAnimation();
        //getNextRecipe_executeGetNextRecipeInteractor();
    }

    @Test
    public void getNextRecipe_executeGetNextRecipeInteractor() {
        presenter.getNextRecipe();

        verify(view).hideUIElements();
        verify(view).showProgress();

        verify(getNextRecipe).execute();
    }

    @Test
    public void onEvent_hasError() {
        RecipeMainEvent event = new RecipeMainEvent();
        event.setError("");

        presenter.onEventMainThread(event);

        verify(view).hideProgress();
        verify(view).onGetRecipeError(event.getError());
    }

    @Test
    public void onNextEvent_setRecipeOnView() {
        RecipeMainEvent event = new RecipeMainEvent();
        event.setType(RecipeMainEvent.NEXT_EVENT);
        event.setRecipe(new Recipe());

        presenter.onEventMainThread(event);

        verify(view).setRecipe(event.getRecipe());
    }

    @Test
    public void onSaveEvent_notifyView_getNextRecipe() {
        RecipeMainEvent event = new RecipeMainEvent();
        event.setType(RecipeMainEvent.SAVE_EVENT);
        event.setRecipe(new Recipe());

        presenter.onEventMainThread(event);

        verify(view).onRecipeSaved();
        //getNextRecipe_executeGetNextRecipeInteractor();
    }
}
