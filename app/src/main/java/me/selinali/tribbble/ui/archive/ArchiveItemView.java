/*
 * Copyright 2016 Selina Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
