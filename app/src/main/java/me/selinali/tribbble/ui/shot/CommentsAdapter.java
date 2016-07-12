package me.selinali.tribbble.ui.shot;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.selinali.tribbble.R;
import me.selinali.tribbble.model.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int TYPE_COMMENT = 0;
  private static final int TYPE_NO_COMMENTS = 1;

  private final List<Comment> mComments;

  public CommentsAdapter(List<Comment> comments) {
    mComments = comments;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_COMMENT) {
      return new CommentViewHolder(new CommentItemView(parent.getContext()));
    } else if (viewType == TYPE_NO_COMMENTS) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_comments_view, parent, false);
      return new NoCommentsViewHolder(view);
    } else {
      return null;
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CommentViewHolder) {
      ((CommentItemView) holder.itemView).bind(mComments.get(position));
    }
  }

  @Override public int getItemCount() {
    return mComments.isEmpty() ? 1 : mComments.size();
  }

  @Override public int getItemViewType(int position) {
    return mComments.isEmpty() ? TYPE_NO_COMMENTS : TYPE_COMMENT;
  }

  public static class CommentViewHolder extends RecyclerView.ViewHolder {
    public CommentViewHolder(CommentItemView itemView) {
      super(itemView);
    }
  }

  public static class NoCommentsViewHolder extends RecyclerView.ViewHolder {
    public NoCommentsViewHolder(View itemView) {
      super(itemView);
    }
  }
}
