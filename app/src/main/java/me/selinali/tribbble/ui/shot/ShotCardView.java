package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.ui.common.Bindable;
import me.selinali.tribbble.utils.DateUtils;

public class ShotCardView extends CardView implements Bindable<Shot> {

  @BindView(R.id.imageview_shot) ImageView mShotImageView;
  @BindView(R.id.textview_shot_name) TextView mShotNameTextView;
  @BindView(R.id.textview_user) TextView mUserTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes_count) TextView mLikesTextView;
  @BindView(R.id.textview_views_count) TextView mViewsTextView;
  @BindView(R.id.textview_buckets_count) TextView mBucketsTextView;

  public ShotCardView(Context context) {
    super(context);
    inflate(context, R.layout.shot_card_view, this);
    ButterKnife.bind(this);
  }

  @Override
  public void bind(Shot shot) {
    Glide.with(getContext())
        .load(shot.getImages().getHighResImage())
        .placeholder(R.drawable.grid_item_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(mShotImageView);
    mShotNameTextView.setText(shot.getTitle());
    mUserTextView.setText(shot.getUser().getName());
    mDateTextView.setText(DateUtils.formatDate(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mBucketsTextView.setText(String.valueOf(shot.getBucketsCount()));
  }
}