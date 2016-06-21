package me.selinali.tribbble.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import rx.Observable;

public class ImageUtils {
  public static Observable<Bitmap> fetchBitmapFrom(String url, Context context) {
    return Observable.fromCallable(() -> Picasso.with(context).load(url).get());
  }
}
