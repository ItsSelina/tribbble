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

package me.selinali.tribbble.utils;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.selinali.tribbble.R;
import me.selinali.tribbble.TribbbleApp;

import static me.selinali.tribbble.TribbbleApp.color;

public final class ViewUtils {

  private ViewUtils() {}

  public static int getStatusBarHeight() {
    return getInternalDimension("status_bar_height");
  }

  public static int getNavigationBarHeight() {
    boolean hasMenuKey = ViewConfiguration.get(TribbbleApp.context()).hasPermanentMenuKey();
    boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
    return (!hasMenuKey && !hasBackKey) ? getInternalDimension("navigation_bar_height") : 0;
  }

  private static int getInternalDimension(String name) {
    Resources res = TribbbleApp.context().getResources();
    int resId = res.getIdentifier(name, "dimen", "android");
    return resId != 0 ? res.getDimensionPixelSize(resId) : 0;
  }

  public static int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        TribbbleApp.context().getResources().getDisplayMetrics());
  }

  public static void setBottomMargin(View view, int bottomMargin) {
    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
  }

  public static void applyColorFilter(ImageView imageView, @ColorRes int resId) {
    imageView.setColorFilter(new PorterDuffColorFilter(
        TribbbleApp.color(resId), PorterDuff.Mode.SRC_ATOP));
  }

  public static void fadeView(View view, boolean show, long duration) {
    if (show && view.getVisibility() == View.INVISIBLE) {
      ViewCompat.animate(view).alpha(1f).setDuration(duration)
          .withStartAction(() -> view.setVisibility(View.VISIBLE));
    } else if (!show && view.getVisibility() == View.VISIBLE) {
      ViewCompat.animate(view).alpha(0f).setDuration(duration)
          .withEndAction(() -> view.setVisibility(View.INVISIBLE));
    }
  }

  public static void tintDrawable(TextView textView, int position) {
      DrawableCompat.setTint(textView.getCompoundDrawables()[position], color(R.color.textNormal));
  }
}
