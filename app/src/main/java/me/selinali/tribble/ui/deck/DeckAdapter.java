package me.selinali.tribble.ui.deck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.shot.ShotCardView;

public class DeckAdapter extends ArrayAdapter<Shot> {

  public interface OnShotClickListener {
    void onShotClicked(Shot shot);
  }

  private OnShotClickListener mOnShotClickListener;

  public DeckAdapter(Context context, List<Shot> shots, OnShotClickListener onShotClickListener) {
    super(context, 0, shots);
    mOnShotClickListener = onShotClickListener;
  }

  private static class ViewHolder {
    ShotCardView view;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    Shot shot = getItem(position);

    ViewHolder viewHolder;
    if (view == null) {
      viewHolder = new ViewHolder();
      view = new ShotCardView(getContext());
      viewHolder.view = (ShotCardView) view;
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }

    viewHolder.view.bind(shot);
    view.setOnClickListener(v -> mOnShotClickListener.onShotClicked(shot));
    return view;
  }
}