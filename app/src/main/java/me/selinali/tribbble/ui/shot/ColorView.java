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

package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.selinali.tribbble.R;

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
    setGravity(Gravity.CENTER_VERTICAL);
    setPadding(10, 10, 10, 10);
    ButterKnife.bind(this);

    mImageView.setImageDrawable(new ColorDrawable(mColor));
    mTextView.setText(String.format("#%06X", (0xFFFFFF & mColor)));
  }
}
