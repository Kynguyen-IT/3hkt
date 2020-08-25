package com.kynguyen.shop_3hkt.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Products {
  public String address, description, idCategory, image,name, pid,price, lat, lng;
  public int quantity;
  public Products() {
  }

  public Products(String address, String description, String idCategory, String image, String name, String pid, String price, int quantity) {
    this.address = address;
    this.description = description;
    this.idCategory = idCategory;
    this.image = image;
    this.name = name;
    this.pid = pid;
    this.price = price;
    this.lat= lat;
    this.lng = lng;
    this.quantity = quantity;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public String getLng() {
    return lng;
  }

  public void setLng(String lng) {
    this.lng = lng;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIdCategory() {
    return idCategory;
  }

  public void setIdCategory(String idCategory) {
    this.idCategory = idCategory;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
