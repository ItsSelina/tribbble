package me.selinali.tribble;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import rx.Subscription;

public class TribbleApp extends Application {

  public static final String PREF_NAME = "tribble.PREFERENCE_KEY";

  private static Context sContext;

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);
    }

    sContext = getApplicationContext();
  }

  public static Context context() {
    return sContext;
  }

  public static void unsubscribe(Subscription subscription) {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
