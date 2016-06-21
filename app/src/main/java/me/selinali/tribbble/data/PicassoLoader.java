package me.selinali.tribbble.data;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.selinali.tribbble.R;

public class PicassoLoader implements ImageLoader {
  @Override
  public void load(String url, ImageView imageView) {
    Picasso.with(imageView.getContext())
        .load(url)
        .placeholder(R.drawable.grid_item_placeholder)
        .into(imageView);
  }
}
