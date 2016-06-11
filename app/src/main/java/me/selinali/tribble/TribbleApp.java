package me.selinali.tribble;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
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

  public static Drawable drawable(@DrawableRes int resId) {
    return ContextCompat.getDrawable(sContext, resId);
  }

  public static String string(@StringRes int resId) {
    return sContext.getString(resId);
  }
}
