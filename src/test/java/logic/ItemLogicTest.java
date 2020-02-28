/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import common.TomcatStartUp;
import dal.EMFactory;
import entity.Item;
import entity.Image;
import entity.Item;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class ItemLogicTest {

    private ItemLogic logic;
    private Item expectedItem;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        // make the account to not rely on any logic functionality , just for testing
        Item item = new Item();
        item.setTitle("Dean");
        Date date = new Date(2018, 7, 7);
        item.setCategory(new CategoryLogic().getWithId(1));
        item.setImage(new ImageLogic().getWithId(1));
        item.setDate(date);
        item.setDescription("Good");
        item.setId(1111);
        item.setLocation("location");
        item.setPrice(BigDecimal.ONE);
        item.setTitle("something");
        item.setUrl("www.algonquin.com");
        EntityManager em = EMFactory.getEMFactory().createEntityManager();
        //start a Transaction 
        em.getTransaction().begin();
        //add an account to hibernate, account is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedItem = em.merge(item);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();

        logic = new ItemLogic();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedItem != null) {
            logic.delete(expectedItem);
        }
    }
    //method to avoid repeated code for full item test

    private void assertItemEquals(Item expected, Item actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCategory().getId(), actual.getCategory().getId());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImage().getId(), actual.getImage().getId());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getPrice().doubleValue(), actual.getPrice().doubleValue());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @org.junit.jupiter.api.Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Item> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();
        //make sure account was created successfully
        assertNotNull(expectedItem);
        //delete the new account
        logic.delete(expectedItem);
        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    @org.junit.jupiter.api.Test
    public void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "IMAGE_ID", "CATEGORY_ID", "PRICE", "TITLE", "DATE", "LOCATION", "DESCRIPTION", "URL"), list);
    }

    @org.junit.jupiter.api.Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(ItemLogic.ID, ItemLogic.IMAGE_ID, ItemLogic.CATEGORY_ID, ItemLogic.PRICE, ItemLogic.TITLE, ItemLogic.DATE, ItemLogic.LOCATION, ItemLogic.DESCRIPTION, ItemLogic.URL), list);
    }

    @org.junit.jupiter.api.Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedItem);
        assertEquals(expectedItem.getId(), list.get(0));
        assertEquals(expectedItem.getImage().getId(), list.get(1));
        assertEquals(expectedItem.getCategory().getId(), list.get(2));
        assertEquals(expectedItem.getPrice(), list.get(3));
        assertEquals(expectedItem.getTitle(), list.get(4));
        assertEquals(expectedItem.getDate(), list.get(5));
        assertEquals(expectedItem.getLocation(), list.get(6));
        assertEquals(expectedItem.getDescription(), list.get(7));
        assertEquals(expectedItem.getUrl(), list.get(8));
    }

    @org.junit.jupiter.api.Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ItemLogic.ID, new String[]{Integer.toString(expectedItem.getId())});
        sampleMap.put(ItemLogic.PRICE, new String[]{expectedItem.getPrice().toString()});
        sampleMap.put(ItemLogic.TITLE, new String[]{expectedItem.getTitle()});
        sampleMap.put(ItemLogic.DATE, new String[]{expectedItem.getDate().toString()});
        sampleMap.put(ItemLogic.LOCATION, new String[]{expectedItem.getLocation()});
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{expectedItem.getDescription()});
        sampleMap.put(ItemLogic.URL, new String[]{expectedItem.getUrl()});
        Item returnedItem = logic.createEntity(sampleMap);
        returnedItem.setCategory(new CategoryLogic().getWithId(1));
        returnedItem.setImage(new ImageLogic().getWithId(1));
        assertItemEquals(expectedItem, returnedItem);
    }

    @org.junit.jupiter.api.Test
    final void testSearch() {

        List<Item> list = logic.search("c");
        for (Item item : list) {
            assertTrue(
                    item.getTitle().contains("c")
                    || item.getUrl().contains("c")
                    || item.getDescription().contains("c")
                    || item.getLocation().contains("c")
                    || item.getCategory().getTitle().contains("c")
                    || item.getImage().getName().contains("c")
            );
        }
    }

    /**
     * Test of getWithId method.
     */
    @org.junit.jupiter.api.Test
    final void testGetWithId() {
        Item actualItem = logic.getWithId(expectedItem.getId());
        assertItemEquals(expectedItem, actualItem);
    }

    @org.junit.jupiter.api.Test
    final void testGetWithUrl() {
        Item actualItem = logic.getWithUrl(expectedItem.getUrl());
        assertItemEquals(expectedItem, actualItem);
    }

    private int evaluateIFinList(List<Item> list) {
        int inList = 0;
        for (Item item : list) {
            if (item.getId().equals(expectedItem.getId())) {
                assertItemEquals(expectedItem, item);
                inList++;
            };
        }
        return inList;
    }

    @org.junit.jupiter.api.Test
    final void testGetWithPrice() {
        List<Item> list = logic.getWithPrice(expectedItem.getPrice());
        int findFull = evaluateIFinList(list);
        assertEquals(1, findFull, "if zero means not found, if more than one means duplicate");
    }

    @org.junit.jupiter.api.Test
    final void testGetWithLocation() {
        List<Item> list = logic.getWithLocation(expectedItem.getLocation());
        int findFull = evaluateIFinList(list);
        assertEquals(1, findFull, "if zero means not found, if more than one means duplicate");
    }

    @org.junit.jupiter.api.Test
    final void testGetWithDescription() {
        List<Item> list = logic.getWithDescription(expectedItem.getDescription());
        int findFull = evaluateIFinList(list);
        assertEquals(1, findFull, "if zero means not found, if more than one means duplicate");
    }

    @org.junit.jupiter.api.Test
    final void testGetWithTitle() {
        List<Item> list = logic.getWithTitle(expectedItem.getTitle());
        for (Item item : list) {
            assertItemEquals(expectedItem, item);
        }
    }

    @org.junit.jupiter.api.Test
    final void testGetWithCategory() {
        List<Item> list = logic.getWithCategory(expectedItem.getCategory().getId());
        int findFull = evaluateIFinList(list);
        assertEquals(1, findFull, "if zero means not found, if more than one means duplicate");
    }

}
