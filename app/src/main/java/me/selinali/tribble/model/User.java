package me.selinali.tribble.model;

import org.parceler.Parcel;

import lombok.Getter;

@Parcel
public class User {
  @Getter int id;
  @Getter String name;
}
