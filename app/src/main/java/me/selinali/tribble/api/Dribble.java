package me.selinali.tribble.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import me.selinali.tribble.model.Shot;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public class Dribble {

  public static volatile Dribble sInstance;

  public static Dribble instance() {
    return sInstance == null ? sInstance = new Dribble() : sInstance;
  }

  private final Endpoints mEndpoints;
  private final Gson mGson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();

  private Dribble() {
    mEndpoints = new Retrofit.Builder()
        .baseUrl("https://api.dribbble.com/v1/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .build()
        .create(Endpoints.class);
  }

  public Observable<List<Shot>> getShots() {
    return mEndpoints.getShots();
  }

  private interface Endpoints {
    @GET("shots")
    Observable<List<Shot>> getShots();
  }
}
