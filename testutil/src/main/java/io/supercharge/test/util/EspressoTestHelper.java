package io.supercharge.test.util;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by gergogergely on 2/24/16.
 */
public class EspressoTestHelper {


    public static Activity getCurrentActivity() {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                activity[0] = Iterables.getOnlyElement(activites);
            }
        });

        return activity[0];
    }

    /**
     * Perform action of waiting for a specific view id.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }


    public static void clickButton(int buttonResourceId, long millis) {
        onView(isRoot()).perform(waitId(buttonResourceId, millis));
        clickButton(buttonResourceId);
    }


    public static void clickButton(int buttonResourceId) {
        try {
            onView(withId(buttonResourceId)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        } catch (PerformException e) {
        }
        getViewById(buttonResourceId).perform(click());
    }


    public static void clickOnDialogButton(int buttonresourceId) {
        onView(withId(buttonresourceId)).perform(click());
    }

    public static void clickButton(String text) {
        getViewByText(text).perform(click());
    }

    public static void typeInEditText(int editTextResourceId, String text) {
        getViewById(editTextResourceId).perform(typeText(text));
    }

    public static void clearEditText(int editTextResourceId) {
        getViewById(editTextResourceId).perform(clearText());
    }

    public static void typeInEditTextAndHitEnter(int editTextResourceId, String text) {
        getViewById(editTextResourceId).perform(typeText(text), pressKey(KeyEvent.KEYCODE_ENTER));
    }

    public static ViewInteraction getViewById(int viewResourceId) {
        return onView(withId(viewResourceId));
    }

    public static ViewInteraction getViewByText(String text) {
        return onView(withText(text));
    }

    public static void clickOnViewGroupChildAt(int viewGroupResourceId, int childIndex) {
        onView(nthChildOf(withId(viewGroupResourceId), childIndex)).perform(click());
    }

    public static void typeTextOnViewGroupChildAt(int viewGroupResourceId, int position, int inputResId, String text) {
        Matcher<View> rowMatcher = nthChildOf(withId(viewGroupResourceId), position);
        onView(allOf(withId(inputResId), isDescendantOfA(rowMatcher))).perform(typeText(text));
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childIndex) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childIndex + ". child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {

                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childIndex).equals(view);

            }
        };
    }

    public static void pressBack() {
//        onView(withId(android.R.id.home)).perform(click());
        Espresso.pressBack();
    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    public static void setSeekBarProgress(int seekbarResourceId, int progress) {
        onView(withId(seekbarResourceId)).perform(setProgress(progress));
    }
}
