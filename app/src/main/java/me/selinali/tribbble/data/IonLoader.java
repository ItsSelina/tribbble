package me.selinali.tribbble.data;

import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import me.selinali.tribbble.R;

public class IonLoader implements ImageLoader {
  @Override
  public void load(String url, ImageView imageView) {
    Ion.with(imageView)
        .placeholder(R.drawable.grid_item_placeholder)
        .load(url);
  }
}
