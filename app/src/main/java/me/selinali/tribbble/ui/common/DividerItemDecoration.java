package me.selinali.tribbble.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.selinali.tribbble.R;
import me.selinali.tribbble.TribbbleApp;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
  private Drawable mDivider;

  public DividerItemDecoration(Context context) {
    mDivider = TribbbleApp.drawable((R.drawable.line_divider));
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + mDivider.getIntrinsicHeight();

      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }
}