package me.selinali.tribble;

import retrofit2.Retrofit;

public class Dribble {

    public static volatile Dribble sInstance;

    public static Dribble instance() {
        return sInstance == null ? sInstance = new Dribble() : sInstance;
    }

    private final Endpoints mEndpoints;

    private Dribble() {
        mEndpoints = new Retrofit.Builder()
                .baseUrl("https://api.dribbble.com/v1/")
                .build()
                .create(Endpoints.class);
    }

    private interface Endpoints {
    }
}
