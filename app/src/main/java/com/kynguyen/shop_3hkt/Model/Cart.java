package com.kynguyen.shop_3hkt.Model;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class Cart {
  public String pid, name, price,discount, cateId, address, image;
  public int quantity;


  public Cart(String pid, String name, String price, int quantity, String discount, String cateId, String address, String image) {
    this.pid = pid;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.discount = discount;
    this.cateId = cateId;
    this.address = address;
    this.image = image;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Cart() {
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

  public String getDiscount() {
    return discount;
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getCateId() {
    return cateId;
  }

  public void setCateId(String cateId) {
    this.cateId = cateId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
//
//  public Double getTotalPrice() {
//    return this.price * this.quantity;
//  }

}
