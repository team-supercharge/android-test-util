package io.supercharge.test.util;

import android.content.Context;
import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by richardradics on 19/03/16.
 */
public class CustomFailureHandler implements FailureHandler {
    private final FailureHandler delegate;

    public CustomFailureHandler(Context targetContext) {
        delegate = new DefaultFailureHandler(targetContext);
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
        try {
            delegate.handle(error, viewMatcher);
        } catch (NoMatchingViewException e) {
            throw new NoHierarchyException(e);
        } catch (AmbiguousViewMatcherException e) {
            throw new NoHierarchyException(e);
        }
    }
}
