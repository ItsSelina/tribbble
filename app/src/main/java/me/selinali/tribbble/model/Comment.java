package me.selinali.tribbble.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Parcel
@AllArgsConstructor
public class Comment {
  @Getter User user;
  @Getter String body;
  @Getter Date createdAt;
  @Getter int likesCount;

  @ParcelConstructor Comment() {}
}