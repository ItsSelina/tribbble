package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;
import me.selinali.tribble.utils.ImageUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShotActivity extends AppCompatActivity {

  private static final String EXTRA_SHOT = "EXTRA_SHOT";
  private static final int COLORS = 6;

  public static Intent launchIntentFor(Shot shot, Context context) {
    return new Intent(context, ShotActivity.class)
        .putExtra(EXTRA_SHOT, Parcels.wrap(shot));
  }

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.imageview_shot) ImageView mShotImageView;
  @BindView(R.id.textview_shot_name) TextView mShotNameTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes) TextView mLikesTextView;
  @BindView(R.id.textview_views) TextView mViewsTextView;
  @BindView(R.id.textview_buckets) TextView mBucketsTextView;
  @BindView(R.id.textview_comments) TextView mCommentsTextView;
  @BindView(R.id.color_container_left) LinearLayout mColorsPaneLeft;
  @BindView(R.id.color_container_right) LinearLayout mColorPaneRight;
  @BindView(R.id.imageview_avatar) CircleImageView mAvatarImageView;
  @BindView(R.id.textview_name) TextView mArtistName;
  @BindView(R.id.textview_location) TextView mArtistLocation;
  @BindView(R.id.textview_description) TextView mDescription;
  @BindView(R.id.webview_description) WebView mDescriptionWeb;

  private Shot mShot;
  private Subscription mSubscription;
  private ColorView[] mColorViews = new ColorView[COLORS];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shot);
    mShot = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_SHOT));
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    bind(mShot);

    for (int i = 0; i < COLORS; i++) {
      ((i < COLORS / 2) ? mColorsPaneLeft : mColorPaneRight)
          .addView(mColorViews[i] = new ColorView(this));
    }

    mSubscription = ImageUtils.fetchBitmapFrom(mShot.getImages().getHighResImage(), this)
        .doOnNext(mShotImageView::setImageBitmap)
        .map(bitmap -> Palette.from(bitmap).maximumColorCount(6).generate())
        .filter(palette -> palette != null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(Palette::getSwatches)
        .flatMapIterable(swatches -> swatches)
        .zipWith(Arrays.asList(mColorViews), Pair::create)
        .subscribe(swatchAndView -> {
          swatchAndView.second.bindColor(swatchAndView.first.getRgb());
        });
  }


  private void initColors() {

  }

  private void bind(Shot shot) {
    Picasso.with(this)
        .load(shot.getUser().getAvatarUrl())
        .into(mAvatarImageView);
    mShotNameTextView.setText(shot.getTitle());
    mDateTextView.setText(DateFormat.getDateInstance().format(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mBucketsTextView.setText(String.valueOf(shot.getBucketsCount()));
    mCommentsTextView.setText(String.valueOf(shot.getCommentsCount()));
    mArtistName.setText(shot.getUser().getName());
    mArtistLocation.setText(shot.getUser().getLocation());
    mDescription.setText(shot.getDescription());
    mDescriptionWeb.loadData(shot.getDescription(), "text/html; charset=utf-8", "UTF-8");
  }
}
