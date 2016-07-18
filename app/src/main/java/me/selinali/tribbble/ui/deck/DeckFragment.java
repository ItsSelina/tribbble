package me.selinali.tribbble.ui.deck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.wenchao.cardstack.CardStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.selinali.tribbble.R;
import me.selinali.tribbble._;
import me.selinali.tribbble.api.Dribble;
import me.selinali.tribbble.data.ArchiveManager;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.MainActivity;
import me.selinali.tribbble.ui.common.Bindable;
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

  private static final String TAG = DeckFragment.class.getSimpleName();
  private static final int PRELOAD_THRESHOLD = 5;

  @BindView(R.id.card_stack) CardStack mCardStack;
  @BindView(R.id.progress_view) View mProgressView;
  @BindView(R.id.conection_error_container) View mErrorContainer;

  private Subscription mSubscription;
  private Unbinder mUnbinder;
  private DeckAdapter mAdapter;
  private int mCurrentPage = 1;
  private int mCurrentPosition = 0;

  private DeckListener mDeckListener = new DeckListener() {
    @Override void onCardSwiped(int direction, int swipedIndex) {
      mCurrentPosition++;
      if (mAdapter.getCount() - swipedIndex <= PRELOAD_THRESHOLD) {
        mCurrentPage++;
        loadNext(0);
      }

      if (direction == RIGHT) {
        Answers.getInstance().logCustom(new CustomEvent("Shot Archived"));
        Shot shot = mAdapter.getItem(swipedIndex);
        ArchiveManager.instance().archive(shot);
        ((MainActivity) getActivity()).notifyShotArchived(shot);
      } else if (direction == LEFT) {
        Answers.getInstance().logCustom(new CustomEvent("Shot Discarded"));
        ArchiveManager.instance().discard(mAdapter.getItem(swipedIndex));
      }
    }

    @Override public void topCardTapped() {
      Shot shot = mAdapter.getItem(mCurrentPosition);
      startActivity(ShotActivity.launchIntentFor(shot, getContext()));
    }
  };

  private void loadNext(int delay) {
    _.unsubscribe(mSubscription);
    mSubscription = Dribble.instance()
        .getShots(mCurrentPage,
            DeckFragment::shouldShow,
            shots -> shots.size() >= 5,
            page -> mCurrentPage = page
        )
        .delaySubscription(delay, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::bind, this::handleError);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadNext(0);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    View view = inflater.inflate(R.layout.fragment_deck, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    setupPadding();
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
    _.unsubscribe(mSubscription);
  }

  @Override public void bind(List<Shot> shots) {
    ViewUtils.fadeView(mCardStack, true, 250);
    if (mAdapter == null) {
      mAdapter = new DeckAdapter(getContext(), shots);
      mCardStack.setListener(mDeckListener);
      mCardStack.setAdapter(mAdapter);
    } else {
      mAdapter.addAll(shots);
    }
  }

  @OnClick(R.id.textview_retry) public void onRetryClicked() {
    mProgressView.setVisibility(View.VISIBLE);
    ViewUtils.fadeView(mErrorContainer, false, 250);
    loadNext(500);
  }

  private void setupPadding() {
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    mCardStack.setPadding(ViewUtils.dpToPx(14), ViewUtils.dpToPx(52),
        ViewUtils.dpToPx(14), navigationBarHeight + ViewUtils.dpToPx(80));
    mProgressView.setPadding(0, 0, 0, navigationBarHeight + ViewUtils.dpToPx(80));
    mErrorContainer.setPadding(0, 0, 0, navigationBarHeight);
  }

  private static boolean shouldShow(Shot shot) {
    return !ArchiveManager.instance().isArchived(shot) &&
        !ArchiveManager.instance().isDiscarded(shot);
  }

  private void handleError(Throwable throwable) {
    Log.d(TAG, "Failed to load shot", throwable);
    Crashlytics.logException(throwable);
    mProgressView.setVisibility(View.INVISIBLE);
    ViewUtils.fadeView(mCardStack, false, 250);
    ViewUtils.fadeView(mErrorContainer, true, 250);
  }
}
