package edu.galileo.android.facebookrecipes.login;

import com.facebook.login.widget.LoginButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.FacebookRecipesApp;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.login.ui.LoginActivity;

import static org.robolectric.Shadows.shadowOf;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21,
        manifest = "src/main/AndroidManifest.xml")
public class LoginActivityTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FacebookRecipesApp app = (FacebookRecipesApp) RuntimeEnvironment.application;
        ShadowApplication shadowApp = shadowOf(app);
        shadowApp.grantPermissions("android.permission.INTERNET");
        //Intent intent = new Intent(this, DisplayMessageActivity.class);

        app.onCreate();
    }

    @Test
    public void onLoginButtonClicked_fbActivityLogin() {
        //FacebookActivity facebookActivity = Robolectric.setupActivity(FacebookActivity.class);
        //FacebookActivity facebookActivity = Robolectric.buildActivity(FacebookActivity.class).create().start().get();
        LoginActivity loginActivity = Robolectric.buildActivity(LoginActivity.class).create().visible().get();
        LoginButton loginButton = (LoginButton) loginActivity.findViewById(R.id.btnLogin);
        loginButton.performClick();
        /*
        ShadowActivity shadowActivity = shadowOf(loginActivity);
        Intent intent = shadowActivity.peekNextStartedActivityForResult().intent;
        System.out.println(intent.getAction() + " " + intent.getData() + " " + intent.getComponent());
        */
    }

}
