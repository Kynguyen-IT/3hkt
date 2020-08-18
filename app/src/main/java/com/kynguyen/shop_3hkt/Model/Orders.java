package com.kynguyen.shop_3hkt.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Orders {
  public String dateTime, name, phone, quantity, status, total,image, address, orderId;

  public Orders(String dateTime, String name, String phone, String quantity, String status, String total, String image, String address, String orderId) {
    this.dateTime = dateTime;
    this.name = name;
    this.phone = phone;
    this.quantity = quantity;
    this.status = status;
    this.total = total;
    this.image = image;
    this.address = address;
    this.orderId = orderId;
  }

  public String getAddress() {
    return address;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Orders() {
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
