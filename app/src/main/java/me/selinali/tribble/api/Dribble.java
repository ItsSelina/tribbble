package me.selinali.tribble.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dribble {

  public static volatile Dribble sInstance;

  public static Dribble instance() {
    return sInstance == null ? sInstance = new Dribble() : sInstance;
  }

  private final Endpoints mEndpoints;

  private Dribble() {
    mEndpoints = new Retrofit.Builder()
        .baseUrl("https://api.dribbble.com/v1/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Endpoints.class);
  }

  private interface Endpoints {

  }
}
