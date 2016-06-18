package me.selinali.tribble.ui.archive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.selinali.tribble.R;
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
    View itemView = LayoutInflater.from(mContext).inflate(R.layout.archived_item, parent, false);
    return new ShotViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    Shot shot = mShots.get(position);
    (shot.isAnimated() ? mIonLoader : mPicassoLoader)
        .load(shot.getImages().getHighResImage(), holder.mShotImageView);
  }

  @Override
  public int getItemCount() {
    return mShots.size();
  }

  class ShotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    public ImageView mShotImageView;
    public View mCheckmarkOverlay;

    public ShotViewHolder(View view) {
      super(view);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this);
      mShotImageView = (ImageView) view.findViewById(R.id.imageview_shot);
      mCheckmarkOverlay = view.findViewById(R.id.checkmark_overlay);
    }

    @Override
    public void onClick(View v) {
      mContext.startActivity(ShotActivity.launchIntentFor(mShots.get(getAdapterPosition()), mContext));
    }

    @Override
    public boolean onLongClick(View v) {
      return false;
    }
  }
}
