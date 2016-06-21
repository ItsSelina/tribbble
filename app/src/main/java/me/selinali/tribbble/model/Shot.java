package me.selinali.tribbble.model;

import org.parceler.Parcel;

import java.util.Date;

import lombok.Getter;

@Parcel
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