package me.selinali.tribble;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class TribbleApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);
    }
  }
}
