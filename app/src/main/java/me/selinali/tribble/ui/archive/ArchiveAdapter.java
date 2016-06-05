package me.selinali.tribble.ui.archive;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.selinali.tribble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  private List<Shot> mShots;

  public ArchiveAdapter(List<Shot> shots) {
    mShots = shots;
  }

  @Override
  public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ImageView imageView = new ImageView(parent.getContext());
    return new ShotViewHolder(imageView);
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    Picasso.with(holder.itemView.getContext())
        .load(mShots.get(position).getImages().getNormal())
        .into((ImageView) holder.itemView);
  }

  @Override
  public int getItemCount() {
    return mShots.size();
  }

  class ShotViewHolder extends RecyclerView.ViewHolder {
    public ShotViewHolder(ImageView imageView) {
      super(imageView);
    }
  }
}
