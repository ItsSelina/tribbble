package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribble.R;
import me.selinali.tribble.api.Dribble;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.deck.DeckFragment;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  private static final int INDEX_DECK = 0;
  private static final int INDEX_ARCHIVE = 1;

  @BindView(R.id.button_deck) View mDeckButton;
  @BindView(R.id.button_archive) View mArchiveButton;

  private Fragment[] mFragments = new Fragment[2];
  private Subscription mSubscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mFragments[INDEX_DECK] = DeckFragment.newInstance();
    mFragments[INDEX_ARCHIVE] = ArchiveFragment.newInstance();

    swapFragment(mFragments[INDEX_DECK]);

    RxView.clicks(mDeckButton).debounce(2, TimeUnit.SECONDS)
        .subscribe(click -> swapFragment(mFragments[INDEX_DECK]));

    RxView.clicks(mArchiveButton).debounce(2, TimeUnit.SECONDS)
        .subscribe(click -> swapFragment(mFragments[INDEX_ARCHIVE]));

    mSubscription = Dribble.instance().getShots()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::bindShots, throwable -> {throwable.printStackTrace();});
  }

  private void bindShots(List<Shot> shots) {
    ((DeckFragment) mFragments[INDEX_DECK]).bind(shots);
  }

  private void swapFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.container, fragment)
        .commit();
  }
}
