/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper.kijiji;

import java.util.Objects;

/**
 *
 * @author mingjiongliao
 * KijijiItem is a simple POJO item which follows the Class diagrams attached. all setters are default access.
 */
public class KijijiItem {

    private String id;
    private String url;
    private String imageUrl;
    private String imageName;
    private String price;
    private String title;
    private String date;
    private String location;
    private String description;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "KijijiItem{" + "id=" + id + ", url=" + url + ", imageUrl=" + imageUrl + ", imageName=" + imageName + ", price=" + price + ", title=" + title + ", date=" + date + ", location=" + location + ", description=" + description + '}';
    }

    KijijiItem() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KijijiItem other = (KijijiItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    void setId(String id) {
        this.id = id;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    void setImageName(String imageName) {
        this.imageName = imageName;
    }

    void setPrice(String price) {
        this.price = price;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setDate(String date) {
        this.date = date;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setDescription(String description) {
        this.description = description;
    }

}
