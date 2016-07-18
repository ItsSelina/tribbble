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

package me.selinali.tribbble.ui.common;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lombok.AllArgsConstructor;
import me.selinali.tribbble.R;
import me.selinali.tribbble.utils.ViewUtils;

import static me.selinali.tribbble.TribbbleApp.color;
import static me.selinali.tribbble.TribbbleApp.drawable;
import static me.selinali.tribbble.TribbbleApp.string;

public class BinaryBar extends CardView {

  @AllArgsConstructor
  public static class Item {
    @StringRes final int textResourceId;
    @DrawableRes final int iconResourceId;
    final OnClickListener onClickListener;
  }

  private RelativeLayout mContainer;
  private View mLeftView;
  private View mRightView;

  public BinaryBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mContainer = new RelativeLayout(getContext());
    addView(mContainer);
  }

  public void addItems(Item left, Item right) {
    mLeftView = inflateViewFor(left);
    mRightView = inflateViewFor(right);
    View divider = new View(getContext());
    divider.setBackgroundColor(color(R.color.divider));
    divider.setId(new Object().hashCode());

    LayoutParams layoutParams = new LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

    RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(
        ViewUtils.dpToPx(1),
        ViewGroup.LayoutParams.MATCH_PARENT);
    dividerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dividerParams.setMargins(0, ViewUtils.dpToPx(8), 0, ViewUtils.dpToPx(8));

    RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    leftParams.setMargins(ViewUtils.dpToPx(2), 0, ViewUtils.dpToPx(2), 0);
    leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    leftParams.addRule(RelativeLayout.ALIGN_RIGHT, divider.getId());

    RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    rightParams.setMargins(ViewUtils.dpToPx(2), 0, ViewUtils.dpToPx(2), 0);
    rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    rightParams.addRule(RelativeLayout.ALIGN_LEFT, divider.getId());

    mContainer.addView(mLeftView);
    mContainer.addView(mRightView);
    mContainer.addView(divider);

    mContainer.setLayoutParams(layoutParams);
    divider.setLayoutParams(dividerParams);
    mLeftView.setLayoutParams(leftParams);
    mRightView.setLayoutParams(rightParams);

    setActive(mLeftView, true);

    mLeftView.setOnClickListener(v -> {
      setActive(mLeftView, true);
      setActive(mRightView, false);
      left.onClickListener.onClick(v);
    });
    mRightView.setOnClickListener(v -> {
      setActive(mLeftView, false);
      setActive(mRightView, true);
      right.onClickListener.onClick(v);
    });
  }

  public void setActive(View view, boolean active) {
    int iconColor = active ? R.color.colorAccent : R.color.textNormal;
    int textColor = active? R.color.textDark : R.color.textNormal;
    ImageView icon = (ImageView) view.findViewById(R.id.icon);
    TextView text = (TextView) view.findViewById(R.id.text);
    ViewUtils.applyColorFilter(icon, iconColor);
    text.setTextColor(color(textColor));
  }

  private View inflateViewFor(Item item) {
    View view = View.inflate(getContext(), R.layout.binary_bar_item, null);
    ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(drawable(item.iconResourceId));
    ((TextView) view.findViewById(R.id.text)).setText(string(item.textResourceId));
    return view;
  }
}
