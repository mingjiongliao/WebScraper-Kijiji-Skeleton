/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mingjiongliao
 * 
 * Hello Shawn, For this Image deliver system, it doesn't work on my system. I have asked help from you.
 * But it seems like to be the problem from the Mac/Window system problem.
 * You have double checked/ debugged my code, it looks all good 
 * and you asked me to mark it here for not deducting my mark on this point.
 * thanks.
 */
@WebServlet(name = "ImageDelivery", urlPatterns = {"/image/*"})
public class ImageDelivery extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImageDelivery</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ImageDelivery at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

      ServletContext cntx= req.getServletContext();
      // Get the absolute path of the image
      String filename = System.getProperty("user.home")+"/KijijiImages/" + req.getPathInfo().substring(1);
      // retrieve mimeType dynamically
      String mime = cntx.getMimeType(filename);
      if (mime == null) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }

      resp.setContentType(mime);
      File file = new File(filename);
      resp.setContentLength((int)file.length());

      FileInputStream in = new FileInputStream(file);
      OutputStream out = resp.getOutputStream();

      // Copy the contents of the file to the output stream
       byte[] buf = new byte[1024];
       int count = 0;
       while ((count = in.read(buf)) >= 0) {
         out.write(buf, 0, count);
      }
    out.close();
    in.close();

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
