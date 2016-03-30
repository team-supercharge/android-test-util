package io.supercharge.test.util;

import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.EspressoException;
import android.support.test.espresso.NoMatchingViewException;

/**
 * Created by richardradics on 19/03/16.
 */
public class NoHierarchyException extends RuntimeException implements EspressoException {
    public NoHierarchyException(NoMatchingViewException e) {
        super(new NoMatchingViewException.Builder().from(e).includeViewHierarchy(false).build());
    }

    public NoHierarchyException(AmbiguousViewMatcherException e) {
        super(new AmbiguousViewMatcherException.Builder().from(e).includeViewHierarchy(false).build());
    }
}
