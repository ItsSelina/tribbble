package me.selinali.tribble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.selinali.tribble.R;

public class ArchiveFragment extends Fragment {

  public static Fragment newInstance() {
    return new ArchiveFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_archive, container, false);
    ButterKnife.bind(this, view);
    return view;
  }
}
