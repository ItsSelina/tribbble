package me.selinali.tribbble.model;

import org.parceler.Parcel;

import lombok.Getter;

@Parcel
public class User {
  @Getter int id;
  @Getter String name;
  @Getter String location;
  @Getter String avatarUrl;
}
