package me.selinali.tribble.ui.archive;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;

import java.util.List;

import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  private List<Shot> mShots;

  public ArchiveAdapter(List<Shot> shots) {
    mShots = shots;
  }

  @Override
  public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ImageView imageView = new ImageView(parent.getContext());
    imageView.setAdjustViewBounds(true);
    return new ShotViewHolder(imageView);
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    ImageView view = (ImageView) holder.itemView;
    Ion.with(view)
        .animateGif(AnimateGifMode.ANIMATE)
        .placeholder(R.drawable.grid_item_placeholder)
        .load(mShots.get(position).getImages().getImage());
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
