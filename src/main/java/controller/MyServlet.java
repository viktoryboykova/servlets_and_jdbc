package controller;

import com.google.gson.Gson;
import model.Cat;
import service.MyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class MyServlet extends HttpServlet {

    private MyService myService = new MyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setAttribute("cats", myService.getAll());
        List<Cat> someObject = (List<Cat>) myService.getAll();
        String json = new Gson().toJson(someObject);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
//        req.getRequestDispatcher("my.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
//        myService.save(
//                new Cat(req.getParameter("name"), Integer.parseInt(req.getParameter("age"))
//        ));
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cat cat = new Gson().fromJson(jb.toString(), Cat.class);
        myService.save(cat);
        resp.sendRedirect(req.getContextPath() + "/check");
    }
}
