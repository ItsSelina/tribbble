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
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.no_comments_view, parent, false);
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

  static class CommentViewHolder extends RecyclerView.ViewHolder {
    public CommentViewHolder(CommentItemView itemView) {
      super(itemView);
    }
  }

  static class NoCommentsViewHolder extends RecyclerView.ViewHolder {
    public NoCommentsViewHolder(View itemView) {
      super(itemView);
    }
  }
}
