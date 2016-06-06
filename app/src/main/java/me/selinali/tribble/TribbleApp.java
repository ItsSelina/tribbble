package me.selinali.tribble;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class TribbleApp extends Application {

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
}
