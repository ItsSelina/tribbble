package me.selinali.tribbble.ui.archive;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.selinali.tribbble.R;
import me.selinali.tribbble.data.ArchiveManager;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.archive.ArchiveAdapter.ArchiveItemListener;
import me.selinali.tribbble.ui.common.Bindable;
import me.selinali.tribbble.ui.MainActivity;
import me.selinali.tribbble.ui.shot.ShotActivity;
import me.selinali.tribbble.utils.ViewUtils;

public class ArchiveFragment extends Fragment implements Bindable<List<Shot>> {

  public ArchiveFragment() {}

  public static Fragment newInstance() {
    return new ArchiveFragment();
  }

  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  private Unbinder mUnbinder;

  private ArchiveItemListener mItemListener = new ArchiveItemListener() {
    @Override public void onClick(Shot shot, ImageView imageView) {
      String transitionName = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
          ? imageView.getTransitionName() : null;
      Intent intent = ShotActivity.launchIntentFor(shot, transitionName, getContext());
      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          getActivity(), imageView, transitionName);
      startActivity(intent, options.toBundle());
    }

    @Override public void onSwipe(Shot shot) {
      ArchiveManager.instance().unArchive(shot);
      ArchiveManager.instance().discard(shot);
    }
  };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_archive, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    setupPadding();
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

      private int previousDy;

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (Math.signum(previousDy) != Math.signum(dy) && dy != 0) {
          ((MainActivity) getActivity()).showBottomBar(dy < 0);
        }
        previousDy = dy;
      }
    });
    bind(ArchiveManager.instance().getArchivedShots());
    return view;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  @Override public void bind(List<Shot> shots) {
    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setAdapter(new ArchiveAdapter(shots, mRecyclerView, mItemListener));
  }

  private void setupPadding() {
    int statusBarHeight = ViewUtils.getStatusBarHeight();
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    mRecyclerView.setPadding(0, statusBarHeight, 0, navigationBarHeight);
  }
}
