/*
 * Copyright 2016 Selina Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.selinali.tribbble;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;

public class TribbbleApp extends Application {

  private static Context sContext;

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);
    }
    Fabric.with(this, new Crashlytics());

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

  public static int integer(@IntegerRes int resId) {
    return sContext.getResources().getInteger(resId);
  }

  public static String string(@StringRes int resId) {
    return sContext.getString(resId);
  }

  public static String plural(@PluralsRes int resId, int quantity) {
    return sContext.getResources().getQuantityString(resId, quantity, quantity);
  }
}
