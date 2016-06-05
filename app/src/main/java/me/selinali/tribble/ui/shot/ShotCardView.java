package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.ui.Bindable;

public class ShotCardView extends CardView implements Bindable<Shot> {

  @BindView(R.id.imageview_shot) ImageView mShotImageView;
  @BindView(R.id.textview_shot_name) TextView mShotNameTextView;
  @BindView(R.id.textview_user) TextView mUserTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes) TextView mLikesTextView;
  @BindView(R.id.textview_views) TextView mViewsTextView;
  @BindView(R.id.textview_comments) TextView mCommentsTextView;

  public ShotCardView(Context context) {
    super(context);
    inflate(context, R.layout.shot_card_view, this);
    ButterKnife.bind(this);
  }

  @Override
  public void bind(Shot shot) {
    Picasso.with(getContext()).load(shot.getImages().getNormal()).into(mShotImageView);
    mShotNameTextView.setText(shot.getTitle());
    mUserTextView.setText(shot.getUser().getName());
    mDateTextView.setText(DateFormat.getDateInstance().format(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mCommentsTextView.setText(String.valueOf(shot.getCommentsCount()));
  }
}
