package me.selinali.tribbble.ui.deck;

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
import me.selinali.tribbble.R;
import me.selinali.tribbble._;
import me.selinali.tribbble.api.Dribble;
import me.selinali.tribbble.data.ArchiveManager;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.Bindable;
import me.selinali.tribbble.ui.shot.ShotActivity;
import me.selinali.tribbble.utils.ViewUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeckFragment extends Fragment implements Bindable<List<Shot>> {

  public DeckFragment() {}

  public static Fragment newInstance() {
    return new DeckFragment();
  }

  private static final int PRELOAD_THRESHOLD = 5;

  @BindView(R.id.card_stack) CardStack mCardStack;
  @BindView(R.id.progress_view) View mProgressView;

  private Subscription mSubscription;
  private Unbinder mUnbinder;
  private DeckAdapter mAdapter;
  private int mCurrentPage = 1;
  private int mCurrentPosition = 0;

  private DeckListener mDeckListener = new DeckListener() {
    @Override
    void onCardSwiped(int direction, int swipedIndex) {
      mCurrentPosition++;
      if (mAdapter.getCount() - swipedIndex <= PRELOAD_THRESHOLD) {
        mCurrentPage++;
        loadNext();
      }

      if (direction == RIGHT) {
        ArchiveManager.instance().archive(mAdapter.getItem(swipedIndex));
      } else if (direction == LEFT) {
        ArchiveManager.instance().discard(mAdapter.getItem(swipedIndex));
      }
    }

    @Override
    public void topCardTapped() {
      Shot shot = mAdapter.getItem(mCurrentPosition);
      startActivity(ShotActivity.launchIntentFor(shot, getContext()));
    }
  };

  private void loadNext() {
    _.unsubscribe(mSubscription);
    mSubscription = Dribble.instance().getShots(mCurrentPage)
        .flatMapIterable(shots -> shots)
        .filter(DeckFragment::shouldShow)
        .toList()
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
    setupPadding();
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
    if (shots.isEmpty()) {
      mCurrentPage++;
      loadNext();
    }
    if (mAdapter == null) {
      mAdapter = new DeckAdapter(getContext(), shots);
      mCardStack.setListener(mDeckListener);
      mCardStack.setAdapter(mAdapter);
    } else {
      mAdapter.addAll(shots);
    }
    ViewUtils.fadeView(mProgressView, false, 150);
  }

  private void setupPadding() {
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    mCardStack.setPadding(ViewUtils.dpToPx(14), ViewUtils.dpToPx(52),
        ViewUtils.dpToPx(14), navigationBarHeight + ViewUtils.dpToPx(80));
    mProgressView.setPadding(0, 0, 0, navigationBarHeight + ViewUtils.dpToPx(80));
  }

  private static boolean shouldShow(Shot shot) {
    return !ArchiveManager.instance().isArchived(shot) &&
        !ArchiveManager.instance().isDiscarded(shot);
  }
}
