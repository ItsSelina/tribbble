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
import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.Binder;

public class DeckFragment extends Fragment implements Binder<List<Shot>> {

  public static Fragment newInstance() {
    return new DeckFragment();
  }

  @BindView(R.id.card_stack) CardStack mCardStack;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_deck, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void bind(List<Shot> shots) {
    mCardStack.setAdapter(new DeckAdapter(getContext(), shots));
  }
}
