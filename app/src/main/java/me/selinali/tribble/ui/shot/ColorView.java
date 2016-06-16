package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.selinali.tribble.R;
import me.selinali.tribble.TribbleApp;
import me.selinali.tribble.utils.ViewUtils;

public class ColorView extends LinearLayout {

  private View mColorView;
  private TextView mColorTextView;

  public ColorView(Context context) {
    super(context);
    if (isInEditMode()) return;
    init();
  }

  private void init() {
    setOrientation(HORIZONTAL);

    mColorView = new View(getContext());
    mColorTextView = new TextView(getContext());

    addView(mColorView);
    addView(mColorTextView);

    mColorView.setBackground(TribbleApp.drawable(R.drawable.dot));
    ViewUtils.setRightMargin(mColorView, ViewUtils.dpToPx(16));
    mColorTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
  }

  public void bindColor(@ColorInt int color) {
    GradientDrawable shapeDrawable = (GradientDrawable) mColorView.getBackground();
    shapeDrawable.setColor(color);

    String hexColor = String.format("#%06X", (0xFFFFFF & color));
    mColorTextView.setText(hexColor);
  }
}
