package me.selinali.tribbble.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.selinali.tribbble.BuildConfig;
import me.selinali.tribbble.TribbbleApp;
import me.selinali.tribbble.model.Shot;

public class ArchiveManager {

  private static ArchiveManager sInstance;

  public static ArchiveManager instance() {
    return sInstance == null ? sInstance = new ArchiveManager() : sInstance;
  }

  private static final String ARCHIVED_PREF = "ARCHIVED" + BuildConfig.VERSION_CODE;
  private static final String DISCARDED_PREF = "DISCARDED" + BuildConfig.VERSION_CODE;

  private final SharedPreferences mArchivedPreferences;
  private final SharedPreferences mDiscardedPreferences;
  private final Gson mGson;

  private ArchiveManager() {
    mArchivedPreferences = TribbbleApp.context().getSharedPreferences(ARCHIVED_PREF, Context.MODE_PRIVATE);
    mDiscardedPreferences = TribbbleApp.context().getSharedPreferences(DISCARDED_PREF, Context.MODE_PRIVATE);
    mGson = new Gson();
  }

  @SuppressLint("CommitPrefEdits")
  public void archive(Shot shot) {
    mArchivedPreferences.edit()
        .putString(String.valueOf(shot.getId()), mGson.toJson(shot))
        .commit();
  }

  public boolean isArchived(Shot shot) {
    return mArchivedPreferences.contains(String.valueOf(shot.getId()));
  }

  public List<Shot> getArchivedShots() {
    List<Shot> shots = new ArrayList<>();
    Map<String, ?> shotMap = mArchivedPreferences.getAll();
    for (Map.Entry<String, ?> shot : shotMap.entrySet()) {
      shots.add(mGson.fromJson(String.valueOf(shot.getValue()), Shot.class));
    }
    return shots;
  }

  @SuppressLint("CommitPrefEdits")
  public void discard(Shot shot) {
    mDiscardedPreferences.edit()
        .putString(String.valueOf(shot.getId()), mGson.toJson(shot))
        .commit();
  }

  public boolean isDiscarded(Shot shot) {
    return mDiscardedPreferences.contains(String.valueOf(shot.getId()));
  }
}
