/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.ImageDAL;
import entity.Category;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mingjiongliao
 */
/**
 *
 * +PATH : String = "path" +NAME : String = "name" +URL : String = "url" +ID :
 * String = "id" +ImageLogic() +getAll() : List<Image>
 * +getWithId(id : int) : Image +getWithUrl(url : String) : List<Image>
 * +getWithPath(path : String) : Image +getWithName(name : String) : List<Image>
 * +search(search : String) : List<Image>
 * +createEntity(parameterMap : Map<String, String[]>) : Image +getColumnNames()
 * : List<String>
 * +getColumnCodes() : List<String>
 * +extractDataAsList(e : Image) : List<?>
 */
public class ImageLogic extends GenericLogic<Image, ImageDAL> {

    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";
    public static final String PATH = "path";

    public ImageLogic() {
        super(new ImageDAL());
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "URL", "PATH", "NAME");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, URL, PATH, NAME);
    }

    @Override
    public List<?> extractDataAsList(Image e) {
        return Arrays.asList(e.getId(), e.getUrl(), e.getPath(), e.getName());
    }

    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        Image image = new Image();
        if (parameterMap.containsKey(ID)) {
            image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        image.setUrl(parameterMap.get(URL)[0]);
        image.setPath(parameterMap.get(PATH)[0]);
        image.setName(parameterMap.get(NAME)[0]);
        return image;
    }

    @Override
    public List<Image> getAll() {
        return get(() -> dao().findAll());
    }

    @Override
    public Image getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public List<Image> getWithUrl(String url) {
        return get(() -> dao().findByUrl(url));
    }

    public Image getWithPath(String path) {
        return get(() -> dao().findByPath(path));
    }

    public List<Image> getWithName(String name) {
        return get(() -> dao().findByName(name));
    }

    public List<Image> search(String search) {
        return get(() -> dao().findContaining(search));
    }

}
