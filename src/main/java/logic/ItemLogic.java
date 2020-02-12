/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.ItemDAL;
import entity.Category;
import entity.Image;
import entity.Item;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mingjiongliao
 */

/**
* from UML
+DESCRIPTION : String = "description"
+CATEGORY_ID : String = "categoryId"
+IMAGE_ID : String = "imageId"
+LOCATION : String = "location"
+PRICE : String = "price"
+TITLE : String = "title"
+DATE : String = "date"
+URL : String = "url"
+ID : String = "id"
+ItemLogic()
+getAll() : List<Item>
+getWithId(id : int) : Item
+getWithPrice(price : String) : List<Item>
+getWithTitle(title : String) : List<Item>
+getWithDate(date : String) : List<Item>
+getWithLocation(location : String) : List<Item>
+getWithDescription(description : String) : List<Item>
+getWithUrl(url : String) : Item
+getWithCategory(categoryId : String) : List<Item>
+search(search : String) : List<Item>
+createEntity(parameterMap : Map<String, String[]>) : Item
+getColumnNames() : List<String>
+getColumnCodes() : List<String>
+extractDataAsList(e : Item) : List<?>
 */
public class ItemLogic extends GenericLogic<Item,ItemDAL>{
    
    public static final String URL = "url";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_ID = "categoryId";
    public static final String IMAGE_ID = "imageId";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    private Category category;
    private Image image;
    public ItemLogic() {
        super(new ItemDAL());
    }
    /**
             *from mysql
             * 'id', 'int(10) unsigned', 'NO', 'PRI', NULL, ''
             * 'image_id', 'int(10) unsigned', 'NO', 'MUL', NULL, ''
             * 'category_id', 'int(11)', 'NO', 'MUL', NULL, ''
             * 'price', 'decimal(15,2)', 'YES', '', NULL, ''
             * 'title', 'varchar(255)', 'NO', '', NULL, ''
             * 'date', 'date', 'YES', '', NULL, ''
             * 'location', 'varchar(45)', 'YES', '', NULL, ''
             * 'description', 'text', 'NO', '', NULL, ''
             * 'url', 'varchar(255)', 'NO', 'UNI', NULL, ''
             **/
    @Override
    public List<String> getColumnNames() {
         return Arrays.asList("ID","IMAGE_ID","CATEGORY_ID","PRICE","TITLE","DATE","LOCATION","DESCRIPTION","URL");   
    }

    @Override
    public List<String> getColumnCodes() {
         return Arrays.asList(ID,IMAGE_ID,CATEGORY_ID,PRICE,TITLE,DATE,LOCATION,DESCRIPTION,URL); 
    }

    @Override
    public List<?> extractDataAsList(Item e) {
           return Arrays.asList(e.getId(),e.getImage().getId(),e.getCategory().getId(),e.getPrice(),e.getTitle(),e.getDate(),e.getLocation(),e.getDescription(),e.getUrl()); 
    }

    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();
            /**
             *from mysql
             * 'id', 'int(10) unsigned', 'NO', 'PRI', NULL, ''
             * 'image_id', 'int(10) unsigned', 'NO', 'MUL', NULL, ''
             * 'category_id', 'int(11)', 'NO', 'MUL', NULL, ''
             * 'price', 'decimal(15,2)', 'YES', '', NULL, ''
             * 'title', 'varchar(255)', 'NO', '', NULL, ''
             * 'date', 'date', 'YES', '', NULL, ''
             * 'location', 'varchar(45)', 'YES', '', NULL, ''
             * 'description', 'text', 'NO', '', NULL, ''
             * 'url', 'varchar(255)', 'NO', 'UNI', NULL, ''
             **/
            
            if(parameterMap.containsKey(ID)){
                item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            }
            try{
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("dd-MMM-yy");
            date = formatter.parse(parameterMap.get(DATE)[0]);
            item.setDate(date);
        } catch (ParseException ex) {
            item.setDate(new Date());
        }
            item.setDescription(parameterMap.get(DESCRIPTION)[0]);
            item.setLocation(parameterMap.get(LOCATION)[0]);
            item.setPrice(new BigDecimal(parameterMap.get(PRICE)[0]));
            item.setTitle(TITLE);
            item.setUrl(URL);
        return item;
    }

    @Override
    public List<Item> getAll() {
        return get(()->dao().findAll());
    }

    @Override
    public Item getWithId(int id) {
        return get(()->dao().findById(id));
    }
    
    public List<Item> getWithPrice(String price) {
        return get(()->dao().findByPrice(price));
    }
    
    public List<Item> getWithTitle(String title) {
        return get(()->dao().findByTitle(title));
    }
    
    public List<Item> getWithDate(String date) {
        return get(()->dao().findByDate(date));
    }
    
    public List<Item> getWithLocation(String location) {
        return get(()->dao().findByLocation(location));
    }
    
    public List<Item> getWithDescription(String description) {
        return get(()->dao().findByDescription(description));
    }
    
    public Item getWithUrl(String url) {
        return get(()->dao().findByUrl(url));
    }
    
    public List<Item> getWithCategory(String categoryId) {
        return get(()->dao().findByCategory(categoryId));
    }
    
     public List<Item> search(String search){
        return get(()->dao().findContaining(search));
    }
}
