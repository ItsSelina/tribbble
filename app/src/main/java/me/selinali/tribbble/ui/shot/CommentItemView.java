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

package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribbble.R;
import me.selinali.tribbble.TribbbleApp;
import me.selinali.tribbble.model.Comment;
import me.selinali.tribbble.ui.common.Bindable;
import me.selinali.tribbble.utils.DateUtils;
import me.selinali.tribbble.utils.StringUtils;
import me.selinali.tribbble.utils.ViewUtils;

public class CommentItemView extends RelativeLayout implements Bindable<Comment> {

  @BindView(R.id.imageview_avatar) ImageView mAvatarImageView;
  @BindView(R.id.textview_name) TextView mNameTextView;
  @BindView(R.id.textview_comment) TextView mCommentTextView;
  @BindView(R.id.textview_date) TextView mDateTextView;
  @BindView(R.id.textview_likes_count) TextView mLikesCountTextView;

  public CommentItemView(Context context) {
    super(context);
    inflate(context, R.layout.comment_item, this);
    ButterKnife.bind(this);

    setBackground(TribbbleApp.drawable(R.color.lightGray));
    setPadding(ViewUtils.dpToPx(16), ViewUtils.dpToPx(12),
        ViewUtils.dpToPx(16), ViewUtils.dpToPx(12));
  }

  @Override public void bind(Comment comment) {
    Glide.with(getContext()).load(comment.getUser().getAvatarUrl()).into(mAvatarImageView);
    mNameTextView.setText(comment.getUser().getName());
    mCommentTextView.setMovementMethod(LinkMovementMethod.getInstance());
    mCommentTextView.setText(StringUtils.trimTrailingNewLines(Html.fromHtml(comment.getBody())));
    mDateTextView.setText(DateUtils.formatDate(comment.getCreatedAt()));
    mLikesCountTextView.setText(String.valueOf(comment.getLikesCount()));
  }
}
