package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.selinali.tribble.R;

public class ColorView extends LinearLayout {

  @BindView(R.id.image_view) CircleImageView mImageView;
  @BindView(R.id.text_view) TextView mTextView;

  @ColorInt private final int mColor;

  public ColorView(Context context, @ColorInt int color) {
    super(context);
    mColor = color;
    if (!isInEditMode()) init();
  }

  private void init() {
    inflate(getContext(), R.layout.color_view, this);
    setOrientation(HORIZONTAL);
    setGravity(Gravity.CENTER);
    setPadding(4, 4, 4, 4);
    ButterKnife.bind(this);

    mImageView.setFillColor(mColor);
    mTextView.setText(String.format("#%06X", (0xFFFFFF & mColor)));
  }
}
