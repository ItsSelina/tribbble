package me.selinali.tribbble.ui.shot;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Comment;
import me.selinali.tribbble.ui.Bindable;
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
    int padding = ViewUtils.dpToPx(16);
    setPadding(padding, padding, padding, padding);
  }

  @Override
  public void bind(Comment comment) {
    Glide.with(getContext()).load(comment.getUser().getAvatarUrl()).into(mAvatarImageView);
    mNameTextView.setText(comment.getUser().getName());
    mCommentTextView.setMovementMethod(LinkMovementMethod.getInstance());
    mCommentTextView.setText(Html.fromHtml(comment.getBody().trim()));
    mDateTextView.setText(DateFormat.getInstance().format(comment.getCreatedAt()));
    mLikesCountTextView.setText(String.valueOf(comment.getLikesCount()));
  }
}
