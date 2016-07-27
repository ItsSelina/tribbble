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

package me.selinali.tribbble.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.archive.ArchiveFragment;
import me.selinali.tribbble.ui.common.BinaryBar;
import me.selinali.tribbble.ui.common.BinaryBar.Item;
import me.selinali.tribbble.ui.deck.DeckFragment;
import me.selinali.tribbble.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

  private static final String TAG_DECK_FRAGMENT = "TAG_DECK_FRAGMENT";
  private static final String TAG_ARCHIVE_FRAGMENT = "TAG_ARCHIVE_FRAGMENT";

  @BindView(R.id.container) View mContainer;
  @BindView(R.id.binary_bar) BinaryBar mBinaryBar;

  private final Map<String, Fragment> mFragments = new HashMap<>(2);
  private final Animation mAnimation = new AlphaAnimation(0, 1);
  private final Item mLeftItem = new Item(R.string.deck, R.drawable.ic_deck, v -> {
    Answers.getInstance().logCustom(new CustomEvent("Deck clicked"));
    swapFragment(TAG_DECK_FRAGMENT);
  });
  private final Item mRightItem = new Item(R.string.archive, R.drawable.ic_archive, v -> {
    Answers.getInstance().logCustom(new CustomEvent("Archive clicked"));
    swapFragment(TAG_ARCHIVE_FRAGMENT);
  });

  {
    mAnimation.setDuration(200);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    setupMargins();

    mFragments.put(TAG_DECK_FRAGMENT, DeckFragment.newInstance());
    mFragments.put(TAG_ARCHIVE_FRAGMENT, ArchiveFragment.newInstance());

    swapFragment(TAG_DECK_FRAGMENT);
    mBinaryBar.addItems(mLeftItem, mRightItem);
  }

  private void swapFragment(String tag) {
    FragmentManager manager = getSupportFragmentManager();
    Fragment targetFragment = manager.findFragmentByTag(tag);
    if (targetFragment != null && targetFragment.isVisible()) {
      return;
    }

    mContainer.setAnimation(mAnimation);
    mAnimation.start();

    String otherTag = tag.equals(TAG_DECK_FRAGMENT) ? TAG_ARCHIVE_FRAGMENT : TAG_DECK_FRAGMENT;
    if (manager.findFragmentByTag(tag) != null) {
      manager.beginTransaction()
          .show(manager.findFragmentByTag(tag))
          .commit();
    } else {
      manager.beginTransaction()
          .add(R.id.container, mFragments.get(tag), tag)
          .commit();
    }
    if (manager.findFragmentByTag(otherTag) != null) {
      manager.beginTransaction()
          .hide(manager.findFragmentByTag(otherTag))
          .commit();
    }
    if (tag.equals(TAG_DECK_FRAGMENT)) showBottomBar(true);
  }

  @Nullable private String getVisibleFragmentTag() {
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = manager.findFragmentByTag(TAG_DECK_FRAGMENT);
    if (fragment != null && fragment.isVisible()) {
      return TAG_DECK_FRAGMENT;
    } else if ((fragment = manager.findFragmentByTag(TAG_ARCHIVE_FRAGMENT)) != null &&
        fragment.isVisible()) {
      return TAG_ARCHIVE_FRAGMENT;
    } else {
      return null;
    }
  }

  @Override public void onBackPressed() {
    String visibleFragmentTag = getVisibleFragmentTag();
    if (visibleFragmentTag == null || visibleFragmentTag.equals(TAG_DECK_FRAGMENT)) {
      super.onBackPressed();
    } else {
      mBinaryBar.click(BinaryBar.LEFT);
    }
  }

  private void setupMargins() {
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    ViewUtils.setBottomMargin(mBinaryBar, navigationBarHeight + ViewUtils.dpToPx(16));
  }

  public void showBottomBar(boolean show) {
    ViewUtils.fadeView(mBinaryBar, show, 150);
  }

  public void notifyShotArchived(Shot shot) {
    if (getSupportFragmentManager().findFragmentByTag(TAG_ARCHIVE_FRAGMENT) != null) {
      ((ArchiveFragment) mFragments.get(TAG_ARCHIVE_FRAGMENT)).insertFirst(shot);
    }
  }
}
