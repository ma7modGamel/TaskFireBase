package com.mgh.taskfirebase.Model;

public class ImageModel {
    private  String nameImage;
    private  String idImage;
    private  String uriImage;

    public ImageModel() {
    }

    public ImageModel(String nameImage, String idImage, String uriImage) {
        this.nameImage = nameImage;
        this.idImage = idImage;
        this.uriImage = uriImage;
    }

    public ImageModel(String nameImage, String uriImage) {
        this.nameImage = nameImage;
        this.uriImage = uriImage;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }
}
