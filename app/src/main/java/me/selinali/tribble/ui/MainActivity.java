package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribble.R;
import me.selinali.tribble.ui.deck.DeckFragment;

public class MainActivity extends AppCompatActivity {

  private static final String TAG_DECK_FRAGMENT = "TAG_DECK_FRAGMENT";
  private static final String TAG_ARCHIVE_FRAGMENT = "TAG_ARCHIVE_FRAGMENT";

  @BindView(R.id.button_deck) View mDeckButton;
  @BindView(R.id.button_archive) View mArchiveButton;
  @BindView(R.id.container) View mContainer;

  private final Map<String, Fragment> mFragments = new HashMap<>(2);
  private final Animation mAnimation = new AlphaAnimation(0, 1);

  {
    mAnimation.setDuration(200);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mFragments.put(TAG_DECK_FRAGMENT, DeckFragment.newInstance());
    mFragments.put(TAG_ARCHIVE_FRAGMENT, ArchiveFragment.newInstance());

    swapFragment(TAG_DECK_FRAGMENT, true);

    RxView.clicks(mDeckButton).subscribe(click -> swapFragment(TAG_DECK_FRAGMENT, true));
    RxView.clicks(mArchiveButton).subscribe(click -> swapFragment(TAG_ARCHIVE_FRAGMENT, true));
  }

  private void swapFragment(String tag, boolean animate) {
    if (animate) {
      mContainer.setAnimation(mAnimation);
      mAnimation.start();
    }

    FragmentManager manager = getSupportFragmentManager();
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
  }
}
