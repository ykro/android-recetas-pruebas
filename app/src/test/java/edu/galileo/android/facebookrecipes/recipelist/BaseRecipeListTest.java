package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Created by ykro.
 */
public abstract class BaseRecipeListTest {
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
