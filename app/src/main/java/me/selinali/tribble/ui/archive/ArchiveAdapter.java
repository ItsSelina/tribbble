package me.selinali.tribble.ui.archive;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.selinali.tribble.data.IonLoader;
import me.selinali.tribble.data.PicassoLoader;
import me.selinali.tribble.model.Shot;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  private final PicassoLoader mPicassoLoader;
  private final IonLoader mIonLoader;
  private List<Shot> mShots;

  public ArchiveAdapter(List<Shot> shots) {
    mShots = shots;
    mPicassoLoader = new PicassoLoader();
    mIonLoader = new IonLoader();
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
    Shot shot = mShots.get(position);
    (shot.isAnimated() ? mIonLoader : mPicassoLoader)
        .load(shot.getImages().getHighResImage(), view);
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
