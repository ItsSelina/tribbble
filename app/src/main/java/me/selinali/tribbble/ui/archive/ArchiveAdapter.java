package me.selinali.tribbble.ui.archive;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface ArchiveItemListener {
    void onClick(Shot shot, ImageView imageView);

    void onSwipe(Shot shot);
  }

  static final int TYPE_SHOT = 0;
  static final int TYPE_EMPTY = 1;

  private final List<Shot> mShots;
  private final ArchiveItemListener mListener;
  private final int[] mPlaceholderIds;

  public ArchiveAdapter(List<Shot> shots, RecyclerView recyclerView, ArchiveItemListener listener) {
    mShots = shots;
    mListener = listener;
    ItemCallback simpleCallback = new ItemCallback(0, ItemTouchHelper.LEFT);
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    mPlaceholderIds = new int[3];
    mPlaceholderIds[0] = R.drawable.grid_item_placeholder;
    mPlaceholderIds[1] = R.drawable.grid_item_placeholder_2;
    mPlaceholderIds[2] = R.drawable.grid_item_placeholder_3;
  }

  public void insert(Shot shot, int position) {
    boolean wasEmpty = mShots.isEmpty();
    mShots.add(position, shot);
    if (wasEmpty) {
      notifyItemChanged(0);
    } else {
      notifyItemInserted(position);
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_SHOT) {
      return new ShotViewHolder(new ArchiveItemView(parent.getContext()));
    } else if (viewType == TYPE_EMPTY) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.empty_archive_item, parent, false);
      return new EmptyViewHolder(view);
    } else {
      return null;
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ShotViewHolder) {
      Shot shot = mShots.get(position);
      ArchiveItemView view = (ArchiveItemView) holder.itemView;
      view.bind(shot, mPlaceholderIds[position % mPlaceholderIds.length]);
      view.setOnClickListener(v -> mListener.onClick(shot, view.getImageView()));
    }
  }

  @Override public int getItemCount() {
    return mShots.isEmpty() ? 1 : mShots.size();
  }

  @Override public int getItemViewType(int position) {
    return mShots.isEmpty() ? TYPE_EMPTY : TYPE_SHOT;
  }

  private class ItemCallback extends ItemTouchHelper.SimpleCallback {
    public ItemCallback(int dragDirs, int swipeDirs) {
      super(dragDirs, swipeDirs);
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return (viewHolder instanceof EmptyViewHolder) ? 0 : super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      int position = viewHolder.getAdapterPosition();
      ArchiveAdapter.this.notifyItemRemoved(position);
      mListener.onSwipe(mShots.get(position));
      mShots.remove(position);
    }
  }

  static class ShotViewHolder extends RecyclerView.ViewHolder {
    public ShotViewHolder(ArchiveItemView itemView) {
      super(itemView);
    }
  }

  static class EmptyViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewHolder(View itemView) {
      super(itemView);
    }
  }
}