package gr.mobap;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.firebase.analytics.FirebaseAnalytics;

public final class AnalyticsTrackers {

  private static volatile FirebaseAnalytics fa;

  private AnalyticsTrackers() {}

  public static void init(@NonNull Context appContext) {
    if (fa == null) {
      synchronized (AnalyticsTrackers.class) {
        if (fa == null) {
          fa = FirebaseAnalytics.getInstance(appContext.getApplicationContext());
        }
      }
    }
  }

  public static void logEvent(@NonNull String eventName, Bundle params) {
    if (fa == null) return;
    if (params == null) params = new Bundle();
    fa.logEvent(eventName, params);
  }

  public static void logScreen(@NonNull String screenName, @NonNull String screenClass) {
    if (fa == null) return;
    Bundle b = new Bundle();
    b.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
    b.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass);
    fa.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, b);
  }

  public static void setUserId(String userId) {
    if (fa == null) return;
    fa.setUserId(userId);
  }

  public static void setUserProperty(@NonNull String name, String value) {
    if (fa == null) return;
    fa.setUserProperty(name, value);
  }
}
