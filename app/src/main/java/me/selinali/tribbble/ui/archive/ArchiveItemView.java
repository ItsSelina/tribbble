package me.selinali.tribbble.ui.archive;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Shot;

public class ArchiveItemView extends RelativeLayout {

  @BindView(R.id.imageview_shot) ImageView mShotImageView;
  @BindView(R.id.gif_label) View mGifLabel;

  public ArchiveItemView(Context context) {
    super(context);
    inflate(context, R.layout.archived_item, this);
    ButterKnife.bind(this);
  }

  public void bind(Shot shot, @DrawableRes int placeholderId) {
    mGifLabel.setVisibility(shot.isAnimated() ? VISIBLE : INVISIBLE);
    Glide.with(getContext())
        .load(shot.getImages().getHighResImage())
        .placeholder(placeholderId)
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(new GlideDrawableImageViewTarget(mShotImageView) {
          @Override public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
            super.onResourceReady(resource, animation);
            resource.stop();
          }

          @Override public void onStart() {}

          @Override public void onStop() {}
        });
  }

  public ImageView getImageView() {
    return mShotImageView;
  }
}
