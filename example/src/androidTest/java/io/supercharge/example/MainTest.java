package io.supercharge.example;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.supercharge.test.util.BaseTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainTest extends BaseTest {
    @Rule
    public ActivityTestRule<LoginActivity> mainActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void fullFlowTest() {


        waitForView(R.id.email);

        typeIntoEditText(R.id.email, "test@supercharge.io");

        typeIntoEditText(R.id.password, "Test123@");

        clickOnId(R.id.email_sign_in_button);

        waitForView(R.id.toolbar);

        screenshot("Toolbar1");

        clickOnToolbar(R.id.toolbar);

        screenshot("Toolbar2");

        Espresso.pressBack();

        clickOnId(R.id.fab);

        screenshot("Snackbar");

        clickOnToolbar(R.id.toolbar);

        clickOnText("Share");

        screenshot("ScrollingActivity1");

        scrollOnePageDown();

        screenshot("ScrollingActivity2");

        Espresso.pressBack();

        waitForView(R.id.toolbar);

        clickOnToolbar(R.id.toolbar);

        clickOnText("Send");

        screenshot("ItemListActivity1");

        scrollOnePageDown();

        screenshot("ItemListActivity2");
    }


}