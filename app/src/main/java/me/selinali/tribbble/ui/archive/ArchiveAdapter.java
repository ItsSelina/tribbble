package me.selinali.tribbble.ui.archive;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.selinali.tribbble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  public interface ArchiveItemListener {
    void onClick(Shot shot, ImageView imageView);
    void onSwipe(Shot shot);
  }

  private final List<Shot> mShots;
  private final ArchiveItemListener mListener;

  public ArchiveAdapter(List<Shot> shots, RecyclerView recyclerView, ArchiveItemListener listener) {
    mShots = shots;
    mListener = listener;
    ItemCallback simpleCallback = new ItemCallback(0, ItemTouchHelper.LEFT);
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
  }

  public void insert(Shot shot, int position) {
    mShots.add(position, shot);
    notifyItemInserted(position);
  }

  @Override
  public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ShotViewHolder(new ArchiveItemView(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    Shot shot = mShots.get(position);
    ArchiveItemView view = (ArchiveItemView) holder.itemView;
    ImageView imageView = view.getImageView();
    ViewCompat.setTransitionName(imageView, String.format("transition_%d", shot.hashCode()));
    view.bind(shot);
    view.setOnClickListener(v -> mListener.onClick(shot, view.getImageView()));
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

  private class ItemCallback extends ItemTouchHelper.SimpleCallback {
    public ItemCallback(int dragDirs, int swipeDirs) {
      super(dragDirs, swipeDirs);
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      int position = viewHolder.getAdapterPosition();
      ArchiveAdapter.this.notifyItemRemoved(position);
      mListener.onSwipe(mShots.get(position));
      mShots.remove(position);
    }
  }
}