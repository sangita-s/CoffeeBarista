package com.lyeng.coffeebarista;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainAc = new ActivityTestRule(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//        assertEquals("com.lyeng.coffeebarista", appContext.getPackageName());

        RecyclerView recyclerView = mainAc.getActivity().findViewById(R.id.rv_coffeeList);

        int itemCount = recyclerView.getAdapter().getItemCount();
//        onView(withId(R.id.rv_coffeeList)).check(new RecyclerViewItemCountAssertion(6));
        onView(withId(R.id.action_filter)).perform(click());
//        onView(withId(R.id.rv_coffeeList)).check(new RecyclerViewItemCountAssertion(4));
        int favItems = recyclerView.getAdapter().getItemCount();
        assertNotEquals(favItems, itemCount);
    }
}
