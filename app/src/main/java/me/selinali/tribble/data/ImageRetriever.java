package me.selinali.tribble.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.selinali.tribble.TribbleApp;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.droidsonroids.gif.GifDrawable;
import rx.Observable;

public class ImageRetriever {

  private static ImageRetriever sInstance;

  public static ImageRetriever instance() {
    return sInstance == null ? sInstance = new ImageRetriever() : sInstance;
  }

  private static final long CACHE_SIZE = 1024 * 50;

  private final OkHttpClient mClient;

  private ImageRetriever() {
    Cache cache = new Cache(TribbleApp.context().getExternalCacheDir(), CACHE_SIZE);
    mClient = new OkHttpClient.Builder().cache(cache).build();
  }

  public Observable<GifDrawable> streamToDrawable(String url) {
    Request request = new Request.Builder().url(url).build();
    return Observable.fromCallable(() -> mClient.newCall(request).execute())
        .<GifDrawable>flatMap(response -> {
          if (response.isSuccessful()) {
            try {
              InputStream in = response.body().byteStream();
              BufferedReader reader = new BufferedReader(new InputStreamReader(in));
              String result, line = reader.readLine();
              result = line;
              while ((line = reader.readLine()) != null) {
                result += line;
              }
              response.body().close();

              return Observable.just(new GifDrawable(result.getBytes()));
            } catch (IOException e) {
              return Observable.error(e);
            }
          } else {
            return Observable.error(new IOException(
                "Unsuccessful response: " + response));
          }
        });
  }
}
