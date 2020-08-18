package com.kynguyen.shop_3hkt.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Categories {
  public String id, name , image;

  public Categories(String id, String name) {
    this.id = id;
    this.name = name;
    this.image = image;
  }

  public Categories() {
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Categories){
      Categories c = (Categories ) obj;
      if(c.getName().equals(name) && c.getId()==id ) return true;
    }

    return false;
  }
}
