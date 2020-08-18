package com.kynguyen.shop_3hkt.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Saves {
  public String pid, name ,address, image;

  public Saves(String pid, String name, String address, String image) {
    this.pid = pid;
    this.name = name;
    this.address = address;
    this.image = image;
  }

  public Saves() {
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}

