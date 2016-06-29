package me.selinali.tribbble.ui.shot;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import me.selinali.tribbble.model.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

  private final List<Comment> mComments;

  public CommentsAdapter(List<Comment> comments) {
    mComments = comments;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(new CommentItemView(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    ((CommentItemView) holder.itemView).bind(mComments.get(position));
  }

  @Override
  public int getItemCount() {
    return mComments.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(CommentItemView itemView) {
      super(itemView);
    }
  }
}
