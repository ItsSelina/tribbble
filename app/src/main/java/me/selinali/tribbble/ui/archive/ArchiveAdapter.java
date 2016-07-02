package me.selinali.tribbble.ui.archive;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.selinali.tribbble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  public interface ArchiveItemListener {
    void onClick(Shot shot, View view);
  }

  private final List<Shot> mShots;
  private final ArchiveItemListener mListener;

  public ArchiveAdapter(List<Shot> shots, ArchiveItemListener listener) {
    mShots = shots;
    mListener = listener;
  }

  @Override
  public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ShotViewHolder(new ArchiveItemView(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    ArchiveItemView view = (ArchiveItemView) holder.itemView;
    Shot shot = mShots.get(position);
    view.bind(shot);
    view.setOnClickListener(v -> mListener.onClick(shot, view));
    ViewCompat.setTransitionName(view, String.format("transition_%d", shot.hashCode()));
  }

  @Override
  public int getItemCount() {
    return mShots.size();
  }

  class ShotViewHolder extends RecyclerView.ViewHolder {
    public ShotViewHolder(ArchiveItemView itemView) {
      super(itemView);
    }
  }

//  @Override
//  public void onClick(View v) {
////      Intent intent = ShotActivity.launchIntentFor(mShots.get(getAdapterPosition()), mContext);
////      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(, (View) mShotImageView, "shot");
//    mContext.startActivity(ShotActivity.launchIntentFor(mShots.get(getAdapterPosition()), mContext));
////      mListener.onClick(mShots.get(getAdapterPosition()));
//  }

//  @Override
//  public boolean onLongClick(View v) {
//    return false;
//  }
//}
}
