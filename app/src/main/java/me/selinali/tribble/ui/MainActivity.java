package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribble.R;
import me.selinali.tribble.ui.deck.DeckFragment;
import me.selinali.tribble.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

  private static final String TAG_DECK_FRAGMENT = "TAG_DECK_FRAGMENT";
  private static final String TAG_ARCHIVE_FRAGMENT = "TAG_ARCHIVE_FRAGMENT";

  @BindView(R.id.button_deck) View mDeckButton;
  @BindView(R.id.icon_deck) ImageView mDeckIcon;
  @BindView(R.id.textview_deck) TextView mDeckTextView;
  @BindView(R.id.button_archive) View mArchiveButton;
  @BindView(R.id.icon_archive) ImageView mArchiveIcon;
  @BindView(R.id.textview_archive) TextView mArchiveTextView;
  @BindView(R.id.container) View mContainer;
  @BindView(R.id.bottom_bar) View mBottomBar;

  private final Map<String, Fragment> mFragments = new HashMap<>(2);
  private final Animation mAnimation = new AlphaAnimation(0, 1);

  {
    mAnimation.setDuration(200);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setupMargins();

    mFragments.put(TAG_DECK_FRAGMENT, DeckFragment.newInstance());
    mFragments.put(TAG_ARCHIVE_FRAGMENT, ArchiveFragment.newInstance());

    swapFragment(TAG_DECK_FRAGMENT);

    RxView.clicks(mDeckButton).subscribe(click -> swapFragment(TAG_DECK_FRAGMENT));
    RxView.clicks(mArchiveButton).subscribe(click -> swapFragment(TAG_ARCHIVE_FRAGMENT));
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
    ViewUtils.setBottomMargin(mBottomBar, navigationBarHeight + ViewUtils.dpToPx(16));
  }

  public void showBottomBar(boolean show) {
    if (show && mBottomBar.getVisibility() == View.INVISIBLE) {
      ViewCompat.animate(mBottomBar).alpha(1f).setDuration(150)
          .withStartAction(() -> mBottomBar.setVisibility(View.VISIBLE));
    } else if (!show && mBottomBar.getVisibility() == View.VISIBLE) {
      ViewCompat.animate(mBottomBar).alpha(0f).setDuration(150)
          .withEndAction(() -> mBottomBar.setVisibility(View.INVISIBLE));
    }
  }
}
