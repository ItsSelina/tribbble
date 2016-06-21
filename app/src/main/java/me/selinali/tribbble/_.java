package me.selinali.tribbble;

import android.view.View;
import android.view.ViewTreeObserver;

import rx.Subscription;

public class _ {
  public static void unsubscribe(Subscription subscription) {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  public static void onGlobalLayout(View view, Runnable runnable) {
    view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        runnable.run();
        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
      }
    });
  }
}
