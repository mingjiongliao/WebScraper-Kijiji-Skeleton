/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import entity.Account;
import entity.Item;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mingjiongliao
 */
/**ERR rules:
 * +ItemDAL() +findAll() : List<Item>
 * +findById(id : int) : Item +findByPrice(price : String) : List<Item>
 * +findByTitle(title : String) : List<Item>
 * +findByDate(date : String) : List<Item>
 * +findByLocation(location : String) : List<Item>
 * +findByDescription(description : String) : List<Item>
 * +findByUrl(url : String) : Item +findByCategory(categoryId : String) :
 * List<Item>
 */
public class ItemDAL extends GenericDAL<Item> {

    public ItemDAL() {
        super(Item.class);
    }

    public List<Item> findAll() {
        return findResults("Item.findAll", null);
    }

    public Item findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult("Item.findById", map);
    }

    public List<Item> findByPrice(BigDecimal price) {
        Map<String, Object> map = new HashMap<>();
        map.put("price", price);
        return findResults("Item.findByPrice", map);
    }

    public List<Item> findByTitle(String title) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        return findResults("Item.findByIitle", map);
    }

    public List<Item> findByDate(String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        return findResults("Item.findByDate", map);
    }

    public List<Item> findByLocation(String location) {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        return findResults("Item.findByLocation", map);
    }

    public List<Item> findByDescription(String description) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        return findResults("Item.findByDescription", map);
    }

    public Item findByUrl(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResult("Item.findByUrl", map);
    }

    public List<Item> findByCategory(int categoryId) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryid", categoryId);
        return findResults("Item.findByCategory", map);
    }

    public List<Item> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults("Item.findContaining", map);
    }
}
