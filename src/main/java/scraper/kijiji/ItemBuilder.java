/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper.kijiji;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 *
 * @author mingjiongliao
 */
public class ItemBuilder {

    private static final String URL_BASE = "https://www.kijiji.ca";
    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private Element element;
    private KijijiItem item;
    private Elements elements;

    ItemBuilder() {
        item = new KijijiItem();
    }

    public ItemBuilder setElement(Element element) {
        this.element = element;
        return this;
    }

    public KijijiItem build() {
        item.setId(element.attr(ATTRIBUTE_ID).trim());
        item.setUrl(URL_BASE + element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim());
        elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            item.setImageUrl("");
            item.setImageName("");
        } else {
            item.setImageUrl(elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim());
            item.setImageName(elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim());
        }

        elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if (elements.isEmpty()) {
            item.setPrice("");
        } else {
            item.setPrice(elements.get(0).text().trim());
        }
        elements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if (elements.isEmpty()) {
            item.setTitle("");
        } else {
            item.setTitle(elements.get(0).child(0).text().trim());
        }
        elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if (elements.isEmpty()) {
            item.setDate("");
        } else {
            item.setDate(elements.get(0).childNode(0).outerHtml().trim());
        }
        elements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if (elements.isEmpty()) {
            item.setDescription("");
        } else {
            item.setDescription(elements.get(0).text().trim());
        }
        return item;
    }

}
