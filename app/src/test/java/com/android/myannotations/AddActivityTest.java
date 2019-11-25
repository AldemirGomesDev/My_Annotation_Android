package com.android.myannotations;

import android.os.Bundle;

import com.android.myannotations.activitys.AddActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.TestCase.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class AddActivityTest {

    private AddActivity addActivity;

    @Before
    public void setup() {
        addActivity = Robolectric.buildActivity(AddActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testFieldIsEmpty() {
        addActivity.getAddButton().performClick();
        assertTrue(addActivity.fieldIsEmpty(""));
    }
}
