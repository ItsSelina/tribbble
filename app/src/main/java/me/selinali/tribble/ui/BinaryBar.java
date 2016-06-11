package me.selinali.tribble.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lombok.AllArgsConstructor;
import me.selinali.tribble.R;
import me.selinali.tribble.utils.ViewUtils;

import static me.selinali.tribble.TribbleApp.color;
import static me.selinali.tribble.TribbleApp.drawable;
import static me.selinali.tribble.TribbleApp.string;

public class BinaryBar extends CardView {

  @AllArgsConstructor
  static class Item {
    @StringRes final int textResourceId;
    @DrawableRes final int iconResourceId;
    final View.OnClickListener clickListener;
  }

  private RelativeLayout mContainer;

  public BinaryBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mContainer = new RelativeLayout(getContext());
    addView(mContainer);
  }

  public void addItems(Item left, Item right) {
    View leftView = inflateViewFor(left);
    View rightView = inflateViewFor(right);
    View divider = new View(getContext());
    divider.setBackgroundColor(color(R.color.divider));
    divider.setId(View.generateViewId());

    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
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

    mContainer.addView(leftView);
    mContainer.addView(rightView);
    mContainer.addView(divider);

    mContainer.setLayoutParams(layoutParams);
    divider.setLayoutParams(dividerParams);
    leftView.setLayoutParams(leftParams);
    rightView.setLayoutParams(rightParams);

    leftView.setOnClickListener(left.clickListener);
    rightView.setOnClickListener(right.clickListener);
  }

  private View inflateViewFor(Item item) {
    View view = View.inflate(getContext(), R.layout.bottom_bar_item, null);
    ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(drawable(item.iconResourceId));
    ((TextView) view.findViewById(R.id.text)).setText(string(item.textResourceId));
    return view;
  }
}
