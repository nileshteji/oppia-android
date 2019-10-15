package org.oppia.app.topic.overview

import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.oppia.app.R
import org.oppia.app.topic.TopicActivity
import org.oppia.util.threading.BackgroundDispatcher
import org.oppia.util.threading.BlockingDispatcher
import javax.inject.Singleton

/** Tests for [TopicOverviewFragment]. */
@RunWith(AndroidJUnit4::class)
class TopicOverviewFragmentTest {

  @get:Rule
  var activityTestRule: ActivityTestRule<TopicActivity> = ActivityTestRule(
    TopicActivity::class.java, /* initialTouchMode= */ true, /* launchActivity= */ false
  )

  @Test
  fun testTopicOverviewFragment_loadFragmentTest1_topicNameIsDisplayed() {
    ActivityScenario.launch(TopicActivity::class.java).use {
      onView(withId(R.id.topic_name_text_view)).check(matches(withText("Second Test Topic")))
    }
  }

  // TODO(#135): Update this test case to check on click of See More play tab is shown.
  @Test
  fun testTopicOverviewFragment_loadFragmentTest1_seeMoreIsClickable() {
    ActivityScenario.launch(TopicActivity::class.java).use {
      onView(withId(R.id.see_more_text_view)).check(matches(isClickable()))
    }
  }

  @Test
  fun testTopicOverviewFragment_loadFragmentTest1_configuration_change_topicNameIsDisplayed() {
    activityTestRule.launchActivity(null)
    activityTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    onView(withId(R.id.topic_name_text_view)).check(matches(withText("Second Test Topic")))
  }

  @Module
  class TestModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
      return application
    }

    // TODO(#89): Introduce a proper IdlingResource for background dispatchers to ensure they all complete before
    //  proceeding in an Espresso test. This solution should also be interoperative with Robolectric contexts by using a
    //  test coroutine dispatcher.

    @Singleton
    @Provides
    @BackgroundDispatcher
    fun provideBackgroundDispatcher(@BlockingDispatcher blockingDispatcher: CoroutineDispatcher): CoroutineDispatcher {
      return blockingDispatcher
    }
  }

  @Singleton
  @Component(modules = [TestModule::class])
  interface TestApplicationComponent {
    @Component.Builder
    interface Builder {
      @BindsInstance
      fun setApplication(application: Application): Builder

      fun build(): TestApplicationComponent
    }
  }
}
