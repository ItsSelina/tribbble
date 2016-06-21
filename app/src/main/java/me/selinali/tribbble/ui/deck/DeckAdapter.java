package me.selinali.tribbble.ui.deck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.shot.ShotCardView;

public class DeckAdapter extends ArrayAdapter<Shot> {

  public DeckAdapter(Context context, List<Shot> shots) {
    super(context, 0, shots);
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
    return view;
  }
}