package com.github.relativobr.supreme.util;

public class CompatibilySupremeLegacyItem {

  private String newSupremeID;
  private String oldSupremeID;

  public CompatibilySupremeLegacyItem(String newSupremeID, String oldSupremeID) {
    this.newSupremeID = newSupremeID;
    this.oldSupremeID = oldSupremeID;
  }

  public String getNewSupremeID() {
    return newSupremeID;
  }

  public void setNewSupremeID(String newSupremeID) {
    this.newSupremeID = newSupremeID;
  }

  public String getOldSupremeID() {
    return oldSupremeID;
  }

  public void setOldSupremeID(String oldSupremeID) {
    this.oldSupremeID = oldSupremeID;
  }
}