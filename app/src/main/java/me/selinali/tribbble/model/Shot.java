package me.selinali.tribbble.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Wither;

@Parcel
@EqualsAndHashCode
@AllArgsConstructor
public class Shot {
  @Getter int id;
  @Getter String title;
  @Getter String description;
  @Getter int width;
  @Getter int height;
  @Getter Images images;
  @Getter int viewsCount;
  @Getter int likesCount;
  @Getter int bucketsCount;
  @Getter int commentsCount;
  @Getter User user;
  @Getter Date createdAt;
  @Getter boolean animated;
  @Getter String commentsUrl;
  @Wither @Getter Date archivedAt;
  @Wither @Getter List<Comment> comments = new ArrayList<>();

  @ParcelConstructor Shot() {}

  @Parcel
  public static class Images {
    @Getter String hidpi;
    @Getter String teaser;
    @Getter String normal;

    public String getHighResImage() {
      return hidpi == null? normal : hidpi;
    }
  }
}