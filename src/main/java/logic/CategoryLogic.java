/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import dal.CategoryDAL;
import entity.Category;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mingjiongliao
 * 
 */
/*  
UML from the PRO
+TITLE : String = "title"
+URL : String = "url"
+ID : String = "id"
+CategoryLogic()
+getAll() : List<Category>
+getWithId(id : int) : Category
+getWithUrl(url : String) : Category
+getWithTitle(title : String) : Category
+search(search : String) : List<Category>
+createEntity(parameterMap : Map<String, String[]>) : Category
+getColumnNames() : List<String>
+getColumnCodes() : List<String>
+extractDataAsList(e : Category) : List<?>
*/
public class CategoryLogic extends GenericLogic<Category,CategoryDAL>{
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ID = "id";
            
    public CategoryLogic() {
        super(new CategoryDAL());
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Url", "Title");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, URL, TITLE); 
    }

    @Override
    public List<?> extractDataAsList(Category e) {
        return Arrays.asList(e.getId(),e.getUrl(),e.getTitle());
    }

    @Override
    public List<Category> getAll() {
        return get(()->dao().findAll()); 
    }

    @Override
    public Category getWithId(int id) {
        return get(()->dao().findById(id));
    }
    
    public Category getWithUrl(String url) {
        return get(()->dao().findByUrl(url));
    }
    
    public Category getWithTitle(String title) {
        return get(()->dao().findByTitle(title));
    }
    
    public List<Category> search(String search){
        return get(()->dao().findContaining(search));
    }
    
    //+createEntity(parameterMap : Map<String, String[]>) : Category
    @Override
    public Category createEntity(Map<String, String[]> parameterMap){
        Category category = new Category();
        if(parameterMap.containsKey(ID)){
            category.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        category.setUrl(parameterMap.get(URL)[0]);
        category.setTitle(parameterMap.get(TITLE)[0]);
        return category;
    }
}
