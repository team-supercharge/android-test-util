package io.supercharge.test.util;

import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageButton;

import com.jraska.falcon.FalconSpoon;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.setFailureHandler;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.supercharge.test.util.EspressoTestHelper.clickOnViewGroupChildAt;
import static io.supercharge.test.util.EspressoTestHelper.getCurrentActivity;
import static io.supercharge.test.util.EspressoTestHelper.getViewById;
import static io.supercharge.test.util.EspressoTestHelper.typeInEditText;
import static io.supercharge.test.util.EspressoTestHelper.typeTextOnViewGroupChildAt;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by richardradics on 08/03/16.
 */
public class BaseTest {
    protected Solo solo;

    @Before
    public void clearSharedPreferences() {
        setFailureHandler(new CustomFailureHandler(InstrumentationRegistry.getTargetContext()));
        solo = new Solo(getInstrumentation(), getCurrentActivity());
    }

    protected void waitForView(int resourceId) {
        solo.waitForView(resourceId);
    }

    protected void waitForText(String text) {
        solo.waitForText(text);
    }

    protected void screenshot(String imageName) {
        FalconSpoon.screenshot(getCurrentActivity(), imageName);
    }

    protected void typeIntoEditText(int viewId, String text) {
        waitForView(viewId);
        scrollToView(viewId);
        typeInEditText(viewId, text);
        closeSoftKeyboard();
    }

    protected void clickOnToolbar(int toolbarId) {
        onView(allOf(instanceOf(ImageButton.class), isDescendantOfA(withId(toolbarId)))).perform(click());
    }

    protected void clickOnText(String text) {
        waitForText(text);
        onView(withText(text)).perform(click());
    }

    protected void clickOnText(int resourceId) {
        waitForText(getCurrentActivity().getString(resourceId));
        onView(withText(resourceId)).perform(click());
    }

    protected void clickOnId(int resourceId) {
        waitForView(resourceId);
        scrollToView(resourceId);
        onView(withId(resourceId)).perform(click());
    }

    protected void clickOnDialogButtonText(int stringResId) {
        solo.clickOnButton(solo.getString(stringResId));
    }

    protected void clickOnDialogButtonText(String text) {
        solo.clickOnButton(text);
    }

    protected void clickOnSpinnerItem(int spinnerId, int spinnerItemTextId) {
        clickOnSpinnerItem(spinnerId, solo.getString(spinnerItemTextId));
    }

    protected void clickOnSpinnerItem(int spinnerId, String spinnterItemText) {
        onView(withId(spinnerId)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(spinnterItemText))).perform(click());
    }

    protected void scrollOnePageUp() {
        solo.scrollUp();
    }

    protected void scrollOnePageDown() {
        solo.scrollDown();
    }

    protected void swipeLeftView(int resourceId) {
        getViewById(resourceId).perform(swipeLeft());
    }

    protected void swipeRightViewByText(int resourceId) {
        waitForText(getCurrentActivity().getString(resourceId));
        onView(allOf(withText(resourceId), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(swipeRight());
    }

    protected void swipeRightView(int resourceId) {
        getViewById(resourceId).perform(swipeRight());
    }

    protected void swipeDownView(int resourceId) {
        getViewById(resourceId).perform(swipeDown());
    }

    protected void swipeUpView(int resourceId) {
        getViewById(resourceId).perform(swipeUp());
    }

    protected void isViewDisplayed(int resourceId) {
        onView(withId(resourceId)).check(matches(isDisplayed()));
    }

    protected void clickOnListAtPosition(int listViewId, int position) {
        waitForView(listViewId);
        clickOnViewGroupChildAt(listViewId, position);
    }

    protected void clickOnActionBarOverflow() {
        DisplayMetrics metrics = solo.getCurrentActivity().getResources().getDisplayMetrics();
        Rect rectangle = new Rect();
        Window window = solo.getCurrentActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        solo.clickOnScreen(metrics.widthPixels - 20, statusBarHeight + 24); // assuming notification area on top
    }

    protected void typeTextListItemAtPosition(int listViewId, int position, int inputId, String text) {
        waitForView(listViewId);
        typeTextOnViewGroupChildAt(listViewId, position, inputId, text);
    }

    @After
    public void tearDown() throws Exception {
        try {
            solo.finishOpenedActivities();
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void scrollToView(int resourceId) {
        try {
            onView(withId(resourceId)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        } catch (PerformException e) {
        }
    }

}
