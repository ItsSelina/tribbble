package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribble.R;
import me.selinali.tribble.ui.archive.ArchiveFragment;
import me.selinali.tribble.ui.deck.DeckFragment;
import me.selinali.tribble.utils.ViewUtils;

import me.selinali.tribble.ui.BinaryBar.Item;

public class MainActivity extends AppCompatActivity {

  private static final String TAG_DECK_FRAGMENT = "TAG_DECK_FRAGMENT";
  private static final String TAG_ARCHIVE_FRAGMENT = "TAG_ARCHIVE_FRAGMENT";

  @BindView(R.id.container) View mContainer;
  @BindView(R.id.binary_bar) BinaryBar mBinaryBar;

  private final Map<String, Fragment> mFragments = new HashMap<>(2);
  private final Animation mAnimation = new AlphaAnimation(0, 1);
  private final Item mLeftItem = new Item(R.string.deck, R.drawable.ic_deck,
      v -> swapFragment(TAG_DECK_FRAGMENT));
  private final Item mRightItem = new Item(R.string.archive, R.drawable.ic_archive,
      v -> swapFragment(TAG_ARCHIVE_FRAGMENT));

  {
    mAnimation.setDuration(200);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setupMargins();

    mFragments.put(TAG_DECK_FRAGMENT, DeckFragment.newInstance());
    mFragments.put(TAG_ARCHIVE_FRAGMENT, ArchiveFragment.newInstance());

    swapFragment(TAG_DECK_FRAGMENT);
    mBinaryBar.addItems(mLeftItem, mRightItem);
  }

  private void swapFragment(String tag) {
    mContainer.setAnimation(mAnimation);
    mAnimation.start();

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

  private void setupMargins() {
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    ViewUtils.setBottomMargin(mBinaryBar, navigationBarHeight + ViewUtils.dpToPx(16));
  }

  public void showBottomBar(boolean show) {
    if (show && mBinaryBar.getVisibility() == View.INVISIBLE) {
      ViewCompat.animate(mBinaryBar).alpha(1f).setDuration(150)
          .withStartAction(() -> mBinaryBar.setVisibility(View.VISIBLE));
    } else if (!show && mBinaryBar.getVisibility() == View.VISIBLE) {
      ViewCompat.animate(mBinaryBar).alpha(0f).setDuration(150)
          .withEndAction(() -> mBinaryBar.setVisibility(View.INVISIBLE));
    }
  }
}
