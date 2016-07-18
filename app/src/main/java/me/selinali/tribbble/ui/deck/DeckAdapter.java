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