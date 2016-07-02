package me.selinali.tribbble.ui.archive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.shot.ShotActivity;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ShotViewHolder> {

  public interface ArchiveItemListener {
    void onClick(Shot shot);
  }

  private Context mContext;
  private List<Shot> mShots;

  public ArchiveAdapter(Context context, List<Shot> shots) {
    mContext = context;
    mShots = shots;
  }

  @Override
  public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(mContext).inflate(R.layout.archived_item, parent, false);
    return new ShotViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ShotViewHolder holder, int position) {
    Shot shot = mShots.get(position);
    Glide.with(mContext)
        .load(shot.getImages().getHighResImage())
        .placeholder(R.drawable.grid_item_placeholder)
        .diskCacheStrategy(shot.isAnimated() ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.RESULT)
        .into(holder.mShotImageView);
  }

  @Override
  public int getItemCount() {
    return mShots.size();
  }

  class ShotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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
//      Intent intent = ShotActivity.launchIntentFor(mShots.get(getAdapterPosition()), mContext);
//      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(, (View) mShotImageView, "shot");
      mContext.startActivity(ShotActivity.launchIntentFor(mShots.get(getAdapterPosition()), mContext));
//      mListener.onClick(mShots.get(getAdapterPosition()));
    }

    @Override
    public boolean onLongClick(View v) {
      return false;
    }
  }
}
