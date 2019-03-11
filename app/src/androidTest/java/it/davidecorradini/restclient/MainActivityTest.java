package it.davidecorradini.restclient;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void voidSearchTest() {
        onView(withId(R.id.buttonSearch)).perform(click());
        onView(withId(R.id.textViewSearchResults)).check(matches(withText("[2] Titolo di prova - Davide Corradini\n")));
    }

    @Test
    public void querySearchTest() {
        onView(withId(R.id.editTextSearchQuery)).perform(replaceText("turwioep"));
        onView(withId(R.id.buttonSearch)).perform(click());
        onView(withId(R.id.textViewSearchResults)).check(matches(withText("No result found.")));
    }

    @Test
    public void getBookTest() {
        onView(withId(R.id.navigation_get)).perform(click());
        onView(withId(R.id.editTextID)).perform(replaceText("2"));
        onView(withId(R.id.buttonGet)).perform(click());
        onView(withId(R.id.textViewGetResult)).check(matches(withText("Book found!")));
        onView(withId(R.id.textViewGetTitle)).check(matches(withText("Titolo di prova")));
    }

    @Test
    public void getBookWithWrongIdTest() {
        onView(withId(R.id.navigation_get)).perform(click());
        onView(withId(R.id.editTextID)).perform(replaceText("1"));
        onView(withId(R.id.buttonGet)).perform(click());
        onView(withId(R.id.textViewGetResult)).check(matches(withText("Book not found.")));
    }
}
