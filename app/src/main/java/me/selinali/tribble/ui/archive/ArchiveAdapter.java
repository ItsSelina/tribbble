package me.selinali.tribble.ui.archive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.selinali.tribble.data.IonLoader;
import me.selinali.tribble.data.PicassoLoader;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.shot.ShotActivity;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  private final PicassoLoader mPicassoLoader;
  private final IonLoader mIonLoader;
  private Context mContext;
  private List<Shot> mShots;

  public ArchiveAdapter(Context context, List<Shot> shots) {
    mContext = context;
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
    view.setOnClickListener(v ->
        mContext.startActivity(ShotActivity.launchIntentFor(shot, mContext)));
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
