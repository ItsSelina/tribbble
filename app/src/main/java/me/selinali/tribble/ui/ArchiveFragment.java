package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.selinali.tribble.data.ArchiveManager;
import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.archive.ArchiveAdapter;
import me.selinali.tribble.utils.ViewUtils;

public class ArchiveFragment extends Fragment implements Bindable<List<Shot>> {

  public ArchiveFragment() {}

  public static Fragment newInstance() {
    return new ArchiveFragment();
  }

  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  private Unbinder mUnbinder;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_archive, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    setUpPadding();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    bind(ArchiveManager.instance().getArchivedShots());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  @Override
  public void bind(List<Shot> shots) {
    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setAdapter(new ArchiveAdapter(shots));
  }

  private void setUpPadding() {
    int statusBarHeight = ViewUtils.getStatusBarHeight();
    int navigationBarHeight = ViewUtils.getNavigationBarHeight();
    mRecyclerView.setPadding(0, statusBarHeight, 0, navigationBarHeight);
  }
}
