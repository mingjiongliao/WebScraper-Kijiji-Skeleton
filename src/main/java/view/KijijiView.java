/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import dal.ImageDAL;
import java.util.function.Consumer;
import entity.Category;
import entity.Image;
import entity.Item;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import scraper.kijiji.Kijiji;
import scraper.kijiji.KijijiItem;

/**
 *
 * @author mingjiongliao github link:
 * https://github.com/mingjiongliao/WebScraper-Kijiji-Skeleton
 */
@WebServlet(name = "KijijiView", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            ItemLogic logic = new ItemLogic();
            List<Item> entities = logic.getAll();
            // use all the entites to map in the web page
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet KijijiView</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<h1>Servlet KijijiView at " + request.getContextPath() + "</h1>");
            out.println("<div class=\"center-column\">");
            //map the items
            for (Item e : entities) {
                out.println("<div class=\"item\">");
                out.println("<div class=\"image\">");
                out.printf("<img src=\"image/%s.jpg\" style=\"max-width: 250px; max-height: 200px;\" />", e.getId());
                out.println("</div>");
                out.println("<div class=\"details\">");
                out.println("<div class=\"title\">");
                out.printf("<a href=\"%s\" target=\"_blank\">%s</a>", e.getUrl(), e.getTitle());
                out.println("</div>");
                out.println("<div class=\"price\">");
                out.println(e.getPrice());
                out.println("</div>");
                out.println("<div class=\"date\">");
                out.println(e.getDate());
                out.println("</div>");
                out.println("<div class=\"location\">");
                out.println(e.getLocation());
                out.println("</div>");
                out.println("<div class=\"description\">");
                out.println(e.getDescription());
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("DoGet");
        //set the image path 
        String ImagePath = System.getProperty("user.home") + "/KijijiImages/";
        //Creating a File object
        File file = new File(ImagePath);

        if (file == null) {
            //Creating the directory
            file.mkdirs();
        }
        //get the URL for category you want from CategoryLogic.
        ItemLogic ilogic = new ItemLogic();
        Category cat = new CategoryLogic().getWithId(1);
        String kijijiUrl = cat.getUrl();
        //download the kijiji items
        Kijiji kij = new Kijiji().downloadPage(kijijiUrl).findAllItems();
        // This method takes a lambda which will be executed for each KijijiItem. Type of this lambda is Consumer.
        Consumer<KijijiItem> downLoads = (KijijiItem i)
                -> {
            Map<String, String[]> itemMap = new HashMap<>();
            Map<String, String[]> imagemMap = new HashMap<>();
            Image img = new Image();
            Item item = null;

            //add image first
            //Use FileUtility.downloadAndSaveFile(String url, String dest, String name) 
            //to download to System.getProperty("user.home")+"/KijijiImages/". Name is Item id plus “.jpg”.
            if (new ItemLogic().getWithId(Integer.parseInt(i.getId())) == null && !i.getId().isEmpty() && !i.getUrl().isEmpty()) {
                if (new ItemLogic().getWithUrl(i.getUrl()) == null) {
                    if ((!i.getImageUrl().isEmpty()) && (!i.getImageName().isEmpty()) && (new ImageLogic().getWithPath(System.getProperty("user.home") + "/KijijiImages/" + i.getId() + ".jpg") == null)) {
                        FileUtility.downloadAndSaveFile(i.getImageUrl(), System.getProperty("user.home") + "/KijijiImages/", i.getId() + ".jpg");
                        imagemMap.put(ImageLogic.URL, new String[]{i.getImageUrl()});
                        imagemMap.put(ImageLogic.NAME, new String[]{i.getImageName()});
                        imagemMap.put(ImageLogic.PATH, new String[]{System.getProperty("user.home") + "/KijijiImages/" + i.getId() + ".jpg"});
                        img = new ImageLogic().createEntity(imagemMap);
                        try {
                            new ImageLogic().add(img);
                        } catch (Exception e) {

                        }

                        //then add item to db
                        itemMap.put(ItemLogic.ID, new String[]{i.getId()});
                        itemMap.put(ItemLogic.URL, new String[]{i.getUrl()});
                        itemMap.put(ItemLogic.DATE, new String[]{i.getDate()});
                        itemMap.put(ItemLogic.TITLE, new String[]{i.getTitle()});
                        itemMap.put(ItemLogic.PRICE, new String[]{i.getPrice()});
                        itemMap.put(ItemLogic.LOCATION, new String[]{i.getLocation()});
                        itemMap.put(ItemLogic.DESCRIPTION, new String[]{i.getDescription()});

                        item = ilogic.createEntity(itemMap);
                        item.setCategory(cat);
                        item.setImage(img);
                        ilogic.add(item);
                    }
                }
            }
        };
        kij.proccessItems(downLoads);
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
