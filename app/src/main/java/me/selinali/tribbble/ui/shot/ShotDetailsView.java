package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;
import me.selinali.tribbble.utils.DateUtils;
import me.selinali.tribbble.utils.StringUtils;

import static me.selinali.tribbble.TribbbleApp.color;

public class ShotDetailsView extends RelativeLayout {

  @BindView(R.id.textview_shot_name) TextView mShotNameTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes_count) TextView mLikesTextView;
  @BindView(R.id.textview_views_count) TextView mViewsTextView;
  @BindView(R.id.textview_buckets_count) TextView mBucketsTextView;
  @BindView(R.id.textview_comments_count) TextView mCommentsTextView;
  @BindView(R.id.color_holder_left) LinearLayout mColorsPaneLeft;
  @BindView(R.id.color_holder_right) LinearLayout mColorPaneRight;
  @BindView(R.id.imageview_avatar) CircleImageView mAvatarImageView;
  @BindView(R.id.textview_name) TextView mArtistNameTextView;
  @BindView(R.id.textview_location) TextView mArtistLocationTextView;
  @BindView(R.id.textview_description) TextView mDescriptionTextView;

  public ShotDetailsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    inflate(context, R.layout.shot_details_view, this);
    ButterKnife.bind(this);
    setBackgroundColor(color(R.color.lighterGray));

    for (TextView v : new TextView[]{mLikesTextView, mViewsTextView, mBucketsTextView, mCommentsTextView}) {
      DrawableCompat.setTint(v.getCompoundDrawables()[0], color(R.color.textNormal));
    }
    DrawableCompat.setTint(mLikesTextView.getCompoundDrawables()[0], color(R.color.textNormal));
    DrawableCompat.setTint(mViewsTextView.getCompoundDrawables()[0], color(R.color.textNormal));
    DrawableCompat.setTint(mBucketsTextView.getCompoundDrawables()[0], color(R.color.textNormal));
    DrawableCompat.setTint(mCommentsTextView.getCompoundDrawables()[0], color(R.color.textNormal));
  }

  public void bind(Shot shot) {
    Glide.with(getContext()).load(shot.getUser().getAvatarUrl()).into(mAvatarImageView);
    mShotNameTextView.setText(shot.getTitle());
    mDateTextView.setText(DateUtils.formatDate(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mBucketsTextView.setText(String.valueOf(shot.getBucketsCount()));
    mCommentsTextView.setText(String.valueOf(shot.getCommentsCount()));
    mArtistNameTextView.setText(shot.getUser().getName());
    mArtistLocationTextView.setText(shot.getUser().getLocation());
    mDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
    String description = shot.getDescription();
    if (description == null) {
      mDescriptionTextView.setVisibility(GONE);
    } else {
      mDescriptionTextView.setText(StringUtils.trimTrailingNewLines(Html.fromHtml(description)));
    }
  }

  public void bind(List<Palette.Swatch> swatches) {
    for (int i = 0; i < swatches.size(); i++) {
      ColorView view = new ColorView(getContext(), swatches.get(i).getRgb());
      if (i % 2 == 0) mColorsPaneLeft.addView(view);
      else mColorPaneRight.addView(view);
    }
  }
}
