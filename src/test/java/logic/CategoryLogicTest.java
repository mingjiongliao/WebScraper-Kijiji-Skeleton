package logic;

import entity.Category;
import static org.junit.jupiter.api.Assertions.assertEquals;
import common.TomcatStartUp;
import dal.EMFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import logic.CategoryLogic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static logic.CategoryLogic.ID;
import static logic.CategoryLogic.TITLE;
import static logic.CategoryLogic.URL;

/**
 *
 * @author mingjiongliao
 */
public class CategoryLogicTest {

    private CategoryLogic logic;
    private Category expectedCategory;

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
        Category category = new Category();
        category.setTitle("Dean");
        category.setUrl("www.algonquincollege.com");
        category.setId(11);
        EntityManager em = EMFactory.getEMFactory().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //add an account to hibernate, account is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedCategory = em.merge(category);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();

        logic = new CategoryLogic();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedCategory != null) {
            logic.delete(expectedCategory);
        }
    }

    /**
     * Test of getColumnNames method, of class CategoryLogic.
     */
    @Test
    public void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Url", "Title"), list);
    }

    /**
     * Test of getColumnCodes method, of class CategoryLogic.
     */
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(CategoryLogic.ID, CategoryLogic.URL, CategoryLogic.TITLE), list);
    }

    /**
     * Test of extractDataAsList method, of class CategoryLogic.
     */
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedCategory);
        assertEquals(expectedCategory.getId(), list.get(0));
        assertEquals(expectedCategory.getUrl(), list.get(1));
        assertEquals(expectedCategory.getTitle(), list.get(2));
    }

    /**
     * Test of getAll method, of class CategoryLogic.
     */
    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Category> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull(expectedCategory);
        //delete the new account
        logic.delete(expectedCategory);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    //method to avoid repeated code for full acount test
    private void assertCategoryEquals(Category expected, Category actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    /**
     * Test of getWithId method, of class CategoryLogic.
     */
    @Test
    final void testGetWithId() {
        Category actualCategory = logic.getWithId(expectedCategory.getId());
        assertCategoryEquals(expectedCategory, actualCategory);
    }

    /**
     * Test of getWithUrl method, of class CategoryLogic.
     */
    @Test
    final void testGetWithUrl() {
        Category actualCategory = logic.getWithUrl(expectedCategory.getUrl());
        assertCategoryEquals(expectedCategory, actualCategory);
    }

    /**
     * Test of getWithTitle method, of class CategoryLogic.
     */
    @Test
    final void testGetWithTitle() {
        Category actualCategory = logic.getWithTitle(expectedCategory.getTitle());
        assertCategoryEquals(expectedCategory, actualCategory);
    }

    /**
     * Test of search method, of class CategoryLogic.
     */
    @Test
    final void testSearch() {

        List<Category> list = logic.search("c");
        for (Category category : list) {
            assertTrue(
                    category.getTitle().contains("c")
                    || category.getUrl().contains("c")
            );
        }
    }

    /**
     * Test of createEntity method, of class CategoryLogic.
     */
    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.ID, new String[]{Integer.toString(expectedCategory.getId())});
        sampleMap.put(CategoryLogic.URL, new String[]{expectedCategory.getUrl()});
        sampleMap.put(CategoryLogic.TITLE, new String[]{expectedCategory.getTitle()});
        Category returnedCategory = logic.createEntity(sampleMap);
        assertCategoryEquals(expectedCategory, returnedCategory);

    }

}
