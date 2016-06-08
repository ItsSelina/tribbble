package me.selinali.tribble;

import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

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

  public static int color(@ColorRes int resId) {
    return ContextCompat.getColor(sContext, resId);
  }
}
