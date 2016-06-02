package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.widget.FrameLayout;

import me.selinali.tribble.R;

public class ShotCardView extends FrameLayout {

  public ShotCardView(Context context) {
    super(context);
    inflate(context, R.layout.shot_card_view, this);
  }


}
