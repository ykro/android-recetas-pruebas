package edu.galileo.android.facebookrecipes;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Created by ykro.
 */
public abstract class BaseTest {
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
