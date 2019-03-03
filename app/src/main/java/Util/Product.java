package Util;

import java.io.Serializable;

public class Product implements Serializable{
    private int id;
    private int price;
    private String name;
    private String description;
    private String iconPath;

    private byte img[] ;

    public Product(int id, int price, String name, String description, String iconPath, byte[] img) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.img = img;
    }

    public Product(int id, int price, String name, String description, byte[] img) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String toString() {
        return "Product : "+name+" costs : "+price+"$ description : "+description+" size "+ img.length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
