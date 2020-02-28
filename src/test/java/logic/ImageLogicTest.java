/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import org.junit.jupiter.api.Test;
import common.TomcatStartUp;
import dal.EMFactory;
import entity.Category;
import entity.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author mingjiongliao
 */
public class ImageLogicTest {
    private ImageLogic logic;
    private Image expectedImage;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }
    
    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    public void setUp() {
        Image image = new Image();
        image.setId(11123);
        image.setName("cat");
        image.setPath("myPath");
        image.setUrl("myUrl");
        EntityManager em = EMFactory.getEMFactory().createEntityManager();
        //start a Transaction 
        em.getTransaction().begin();
        //add an account to hibernate, account is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedImage = em.merge(image);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
        logic = new ImageLogic();
    }
    
    @AfterEach
    public void tearDown() {
          if (expectedImage != null) {
            logic.delete(expectedImage);
        }
    }

       /**
     * Test of getColumnNames method, of class ImageLogic.
     */
    @Test
    public void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID","URL","PATH","NAME"), list);
    }
    /**
     * Test of getColumnCodes method, of class ImageLogic.
     */
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(ImageLogic.ID, ImageLogic.URL, ImageLogic.PATH, ImageLogic.NAME), list);
    }
    /**
     * Test of extractDataAsList method, of class ImageLogic.
     */
    @Test
    final void testExtractDataAsList() {
       List<?> list = logic.extractDataAsList(expectedImage);
        assertEquals(expectedImage.getId(), list.get(0));
        assertEquals(expectedImage.getUrl(), list.get(1));
        assertEquals(expectedImage.getPath(), list.get(2));
        assertEquals(expectedImage.getName(), list.get(3));
    }

    /**
     * Test of getAll method, of class ImageLogic.
     */
    @Test
    final void testGetAll() {
         //get all the accounts from the DB
        List<Image> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull(expectedImage);
        //delete the new account
        logic.delete(expectedImage);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }
    //method to avoid repeated code for full acount test
  private void assertImageEquals(Image expected, Image actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getUrl(), actual.getUrl());
    }
    /**
     * Test of getWithId method, of class ImageLogic.
     */
    @Test
    final void testGetWithId() {
       Image actualImage = logic.getWithId(expectedImage.getId());
       assertImageEquals(expectedImage, actualImage);
    }

    /**
     * Test of getWithUrl method, of class ImageLogic.
     */
    @Test
    final void testGetWithUrl() {
       List<Image> actualImage = logic.getWithUrl(expectedImage.getUrl());
        for (Image oneOfImage : actualImage) {
            assertImageEquals(expectedImage, oneOfImage);
        }
       
    }

    /**
     * Test of getWithTitle method, of class ImageLogic.
     */
    @Test
    final void testGetWithName() {
       List<Image> actualImage = logic.getWithName(expectedImage.getName());
        for (Image oneOfImage : actualImage) {
            assertImageEquals(expectedImage, oneOfImage);
        }
    }
    
    @Test
    final void testGetWithPath() {
       Image actualImage = logic.getWithPath(expectedImage.getPath());
        assertImageEquals(expectedImage, actualImage);
    }
    /**
     * Test of search method, of class ImageLogic.
     */
    @Test
    final void testSearch() {

    List<Image> list = logic.search("c");
    for(Image image : list){
        assertTrue(
        image.getName().contains("c")
        || image.getUrl().contains("c")
        );
    }
    }

    /**
     * Test of createEntity method, of class ImageLogic.
     */
    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
        sampleMap.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
        sampleMap.put(ImageLogic.NAME, new String[]{expectedImage.getName()});
        sampleMap.put(ImageLogic.PATH, new String[]{expectedImage.getPath()});
        Image returnedImage = logic.createEntity(sampleMap);
        assertImageEquals(expectedImage, returnedImage);
        
    }

    
    
}
