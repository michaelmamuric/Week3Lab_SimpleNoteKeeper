/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.*;
import domain.Note;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 799470
 */
public class NoteServlet extends HttpServlet {
    
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
    }
    
    /**
     * Reads note contents using BufferedReader
     * @param request servlet request
     * @throws IOException 
     */
    
    private Note readNote (HttpServletRequest request) throws IOException {
        String path, title = "", contents = "";
        ArrayList<String> noteContents = new ArrayList<>();
            
        // Get note.txt path
        path = getServletContext().getRealPath("/WEB-INF/note.txt");
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            
        // If BufferedReader is ready, read file
        while(br.ready()) {
            noteContents.add(br.readLine());
        }
        
        // Get title - first line
        title = noteContents.get(0);
        
        // Get contents - second line onwards
        for(int i = 1; i < noteContents.size(); i++) {
            contents += noteContents.get(i) + "\n";
        }
        
        Note note = new Note(title, contents);
        
        // Close buffered reader
        br.close();
        
        return note;
    }
    
    /**
     * Writes to note file using PrintWriter
     * @param request servlet request
     * @throws IOException 
     */
    
    private Note writeNote(HttpServletRequest request) throws IOException {
        String path, formTitle, formContents;
        
        // Get note.txt path
        path = getServletContext().getRealPath("/WEB-INF/note.txt");
        
        // Get form values
        formTitle    = request.getParameter("title");
        formContents = request.getParameter("contents");
        
        // Declare PrintWriter
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
        
        // Write to file
        pw.printf("%s%n", formTitle);
        pw.printf(formContents);
        
        // Close PrintWriter
        pw.close();
        
        Note newNote = new Note(formTitle, formContents);
        
        return newNote;
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
        response.setContentType("text/html;charset=UTF-8");

        String edit = request.getParameter("edit");
        
        Note note = readNote(request);
        request.setAttribute("note", note);
        
        if(edit != null)
            getServletContext().getRequestDispatcher("/WEB-INF/editnote.jsp").forward(request, response);
        else
            getServletContext().getRequestDispatcher("/WEB-INF/viewnote.jsp").forward(request, response);
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
        //processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
        Note newNote = writeNote(request);
        request.setAttribute("note", newNote);
        getServletContext().getRequestDispatcher("/WEB-INF/viewnote.jsp").forward(request, response);    
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
