package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.selinali.tribbble.R;
import me.selinali.tribbble._;
import me.selinali.tribbble.api.Dribble;
import me.selinali.tribbble.model.Comment;
import me.selinali.tribbble.model.Shot;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShotActivity extends AppCompatActivity {

  private static final String EXTRA_SHOT = "EXTRA_SHOT";

  public static Intent launchIntentFor(Shot shot, Context context) {
    return new Intent(context, ShotActivity.class)
        .putExtra(EXTRA_SHOT, Parcels.wrap(shot));
  }

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.imageview_shot) ImageView mShotImageView;
  @BindView(R.id.textview_shot_name) TextView mShotNameTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes_count) TextView mLikesTextView;
  @BindView(R.id.textview_views_count) TextView mViewsTextView;
  @BindView(R.id.textview_buckets_count) TextView mBucketsTextView;
  @BindView(R.id.textview_comments_count) TextView mCommentsTextView;
  @BindView(R.id.color_holder_left) LinearLayout mColorsPaneLeft;
  @BindView(R.id.color_holder_right) LinearLayout mColorPaneRight;
  @BindView(R.id.imageview_avatar) CircleImageView mAvatarImageView;
  @BindView(R.id.textview_name) TextView mArtistName;
  @BindView(R.id.textview_location) TextView mArtistLocation;
  @BindView(R.id.textview_description) TextView mDescription;
  @BindView(R.id.recyclerview_comments) RecyclerView mCommentsRecyclerView;

  private Shot mShot;
  private Subscription mShotSubscription;

  private interface ImageLoader {
    void load(Action1<Bitmap> futureBitmap);
  }

  private final ImageLoader mPicassoLoader = callback ->
      Picasso.with(this).load(mShot.getImages().getHighResImage()).into(mShotImageView,
          new Callback() {
            @Override public void onError() {}

            @Override public void onSuccess() {
              callback.call(((BitmapDrawable) mShotImageView.getDrawable()).getBitmap());
            }
          });

  private final ImageLoader mIonLoader = callback ->
      Ion.with(mShotImageView).load(mShot.getImages().getHighResImage()).setCallback(
          (e, view) -> callback.call(((BitmapDrawable) view.getDrawable()).getBitmap()));

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shot);
    mShot = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_SHOT));
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    (mShot.isAnimated() ? mIonLoader : mPicassoLoader).load(
        bitmap -> Palette.from(bitmap).maximumColorCount(8).generate(
            palette -> bindSwatches(palette.getSwatches())));

    mShotSubscription = Dribble.instance()
        .getShot(mShot.getId())
        .onErrorReturn(t -> mShot)
        .flatMap(shot -> {
          Observable<List<Comment>> comments = Dribble.instance().getComments(shot);
          return Observable.zip(Observable.just(shot), comments, Shot::withComments);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::bindShot, throwable -> {
          // TODO
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    _.unsubscribe(mShotSubscription);
  }

  private void bindShot(Shot shot) {
    mShot = shot;
    Picasso.with(this).load(shot.getUser().getAvatarUrl()).into(mAvatarImageView);
    mShotNameTextView.setText(shot.getTitle());
    mDateTextView.setText(DateFormat.getDateInstance().format(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mBucketsTextView.setText(String.valueOf(shot.getBucketsCount()));
    mCommentsTextView.setText(String.valueOf(shot.getCommentsCount()));
    mArtistName.setText(shot.getUser().getName());
    mArtistLocation.setText(shot.getUser().getLocation());
    mDescription.setText(Html.fromHtml(shot.getDescription().trim()));
    mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mCommentsRecyclerView.setAdapter(new CommentsAdapter(shot.getComments()));
  }

  public void bindSwatches(List<Palette.Swatch> swatches) {
    for (int i = 0; i < swatches.size(); i++) {
      ColorView view = new ColorView(this, swatches.get(i).getRgb());
      if (i % 2 == 0) mColorsPaneLeft.addView(view);
      else mColorPaneRight.addView(view);
    }
  }
}
