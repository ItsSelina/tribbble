package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

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
import me.selinali.tribbble.ui.common.DividerItemDecoration;
import me.selinali.tribbble.utils.DateUtils;
import me.selinali.tribbble.utils.StringUtils;
import me.selinali.tribbble.utils.ViewUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShotActivity extends AppCompatActivity {

  private static final String EXTRA_SHOT = "EXTRA_SHOT";
  private static final String EXTRA_TRANSITION_SOURCE = "EXTRA_TRANSITION_SOURCE";

  public static Intent launchIntentFor(Shot shot, Context context) {
    return new Intent(context, ShotActivity.class)
        .putExtra(EXTRA_SHOT, Parcels.wrap(shot));
  }

  public static Intent launchIntentFor(Shot shot, String transitionSource, Context context) {
    return launchIntentFor(shot, context)
        .putExtra(EXTRA_TRANSITION_SOURCE, transitionSource);
  }

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.shot_content_container) View mShotContentContainer;
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
  @BindView(R.id.progress_container) View mProgressContainer;

  private Shot mShot;
  private Subscription mShotSubscription;

  private interface ImageLoader {
    void load(Action1<Bitmap> futureBitmap);
  }

  private final ImageLoader mStaticImageLoader = callback ->
      Glide.with(this)
          .load(mShot.getImages().getHighResImage())
          .asBitmap()
          .placeholder(R.drawable.grid_item_placeholder)
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .into(new BitmapImageViewTarget(mShotImageView) {
            @Override public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
              super.onResourceReady(bitmap, anim);
              callback.call(bitmap);
            }
          });

  private final ImageLoader mGifImageLoader = callback ->
      Glide.with(this)
          .load(mShot.getImages().getHighResImage())
          .placeholder(R.drawable.grid_item_placeholder)
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .into(new GlideDrawableImageViewTarget(mShotImageView) {
            @Override protected void setResource(GlideDrawable resource) {
              super.setResource(resource);
              callback.call(((GifDrawable) resource).getFirstFrame());
            }
          });

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shot);
    ButterKnife.bind(this);
    setUpPadding();
    postponeEnterTransition();

    mShot = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_SHOT));

    String transitionName;
    if ((transitionName = getIntent().getExtras().getString(EXTRA_TRANSITION_SOURCE)) != null) {
      ViewCompat.setTransitionName(mShotImageView, transitionName);
    }

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    (mShot.isAnimated() ? mGifImageLoader : mStaticImageLoader).load(bitmap -> {
      Palette.from(bitmap).maximumColorCount(8)
          .generate(palette -> bindSwatches(palette.getSwatches()));
      startPostponedEnterTransition();
    });

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

  @Override protected void onDestroy() {
    super.onDestroy();
    _.unsubscribe(mShotSubscription);
  }

  private void bindShot(Shot shot) {
    mShot = shot;
    Glide.with(this).load(shot.getUser().getAvatarUrl()).into(mAvatarImageView);
    mShotNameTextView.setText(shot.getTitle());
    mDateTextView.setText(DateUtils.formatDate(shot.getCreatedAt()));
    mLikesTextView.setText(String.valueOf(shot.getLikesCount()));
    mViewsTextView.setText(String.valueOf(shot.getViewsCount()));
    mBucketsTextView.setText(String.valueOf(shot.getBucketsCount()));
    mCommentsTextView.setText(String.valueOf(shot.getCommentsCount()));
    mArtistName.setText(shot.getUser().getName());
    mArtistLocation.setText(shot.getUser().getLocation());
    mDescription.setMovementMethod(LinkMovementMethod.getInstance());
    mDescription.setText(StringUtils.trimTrailingNewLines(Html.fromHtml(shot.getDescription())));
    mCommentsRecyclerView.setNestedScrollingEnabled(false);
    mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mCommentsRecyclerView.addItemDecoration(new DividerItemDecoration(this));
    mCommentsRecyclerView.setAdapter(new CommentsAdapter(shot.getComments()));
    ViewUtils.fadeView(mProgressContainer, false, 150);
  }

  private void bindSwatches(List<Palette.Swatch> swatches) {
    for (int i = 0; i < swatches.size(); i++) {
      ColorView view = new ColorView(this, swatches.get(i).getRgb());
      if (i % 2 == 0) mColorsPaneLeft.addView(view);
      else mColorPaneRight.addView(view);
    }
  }

  private void setUpPadding() {
    mShotContentContainer.setPadding(mShotContentContainer.getPaddingLeft(),
        mShotContentContainer.getPaddingTop(),
        mShotContentContainer.getPaddingRight(),
        ViewUtils.getNavigationBarHeight());
  }
}
