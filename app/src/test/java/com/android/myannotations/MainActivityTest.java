package com.android.myannotations;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.provider.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.android.myannotations.activitys.AddActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setup() throws InterruptedException {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(mainActivity);
    }

    @Test
    public void continueShouldLaunchMineActivity() {
        // define the expected results
        Intent expectedIntent = new Intent(mainActivity, AddActivity.class);
        // click the continue button
        mainActivity.findViewById(R.id.fab).callOnClick();
        // get the actual results
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        // check if the expected results match the actual results
        assertTrue(expectedIntent.filterEquals(actualIntent));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O_MR1)
    public void testLayout() throws Exception {
        Activity activity = Robolectric.setupActivity(MainActivity.class);

        Button button = (Button) activity.findViewById(R.id.buttom);

        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}