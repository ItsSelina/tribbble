package me.selinali.tribble;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.selinali.tribble.model.Shot;

public class ArchiveManager {

  private static ArchiveManager sInstance;

  public static ArchiveManager instance() {
    return sInstance == null ? sInstance = new ArchiveManager() : sInstance;
  }

  private static final String KEY_ARCHIVED_ID_SET = "archived_ids." + BuildConfig.VERSION_CODE;
  private static final String KEY_DISCARDED_ID_SET = "discarded_ids." + BuildConfig.VERSION_CODE;

  private final SharedPreferences mPreferences;

  private Set<String> mArchived, mDiscarded;

  private ArchiveManager() {
    mPreferences = PreferenceManager.getDefaultSharedPreferences(TribbleApp.context());
    mArchived = new HashSet<>(mPreferences.getStringSet(KEY_ARCHIVED_ID_SET, Collections.emptySet()));
    mDiscarded = new HashSet<>(mPreferences.getStringSet(KEY_DISCARDED_ID_SET, Collections.emptySet()));
  }

  @SuppressLint("CommitPrefEdits")
  public void archive(Shot shot) {
    mArchived.add(String.valueOf(shot.getId()));
    mPreferences.edit().putStringSet(KEY_ARCHIVED_ID_SET, mArchived).commit();
  }

  public boolean isArchived(Shot shot) {
    return mArchived.contains(String.valueOf(shot.getId()));
  }

  public List<String> getArchivedShotIds() {
    return new ArrayList<>(mArchived);
  }

  @SuppressLint("CommitPrefEdits")
  public void discard(Shot shot) {
    mDiscarded.add(String.valueOf(shot.getId()));
    mPreferences.edit().putStringSet(KEY_DISCARDED_ID_SET, mDiscarded).commit();
  }

  public boolean isDiscarded(Shot shot) {
    return mDiscarded.contains(String.valueOf(shot.getId()));
  }
}
