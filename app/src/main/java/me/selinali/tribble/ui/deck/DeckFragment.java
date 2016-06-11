package me.selinali.tribble.ui.deck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenchao.cardstack.CardStack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.selinali.tribble.R;
import me.selinali.tribble._;
import me.selinali.tribble.api.Dribble;
import me.selinali.tribble.data.ArchiveManager;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.Bindable;
import me.selinali.tribble.ui.shot.ShotActivity;
import me.selinali.tribble.utils.ViewUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeckFragment extends Fragment implements Bindable<List<Shot>> {

  public DeckFragment() {
  }

  public static Fragment newInstance() {
    return new DeckFragment();
  }

  private static final int PRELOAD_THRESHOLD = 5;

  @BindView(R.id.card_stack) CardStack mCardStack;

  private Subscription mSubscription;
  private Unbinder mUnbinder;
  private DeckAdapter mAdapter;
  private int mCurrentPage = 1;

  private DeckListener mDeckListener = new DeckListener() {
    @Override
    void onCardSwiped(int direction, int swipedIndex) {
      if (mAdapter.getCount() - swipedIndex == PRELOAD_THRESHOLD) {
        mCurrentPage++;
        loadNext();
      }

      if (direction == RIGHT) {
        ArchiveManager.instance().archive(mAdapter.getItem(swipedIndex));
      } else if (direction == LEFT) {
        ArchiveManager.instance().discard(mAdapter.getItem(swipedIndex));
      }
    }
  };

  private void loadNext() {
    _.unsubscribe(mSubscription);
    mSubscription = Dribble.instance().getShots(mCurrentPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::bind, Throwable::printStackTrace);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadNext();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_deck, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    setUpPadding();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
    _.unsubscribe(mSubscription);
  }

  @Override
  public void bind(List<Shot> shots) {
    if (mAdapter == null) {
      mAdapter = new DeckAdapter(getContext(), shots, shot ->
          startActivity(ShotActivity.launchIntentFor(shot, getContext())));
      mCardStack.setListener(mDeckListener);
      mCardStack.setAdapter(mAdapter);
    } else {
      mAdapter.addAll(shots);
    }
  }

  private void setUpPadding() {
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    mCardStack.setPadding(ViewUtils.dpToPx(14), ViewUtils.dpToPx(52), ViewUtils.dpToPx(14), navigationBarHeight + ViewUtils.dpToPx(80));
  }
}
