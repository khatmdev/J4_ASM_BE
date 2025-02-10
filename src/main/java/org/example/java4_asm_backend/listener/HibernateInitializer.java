package org.example.java4_asm_backend.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.example.java4_asm_backend.utils.EntityManagerFactoryUtil;
import org.example.java4_asm_backend.utils.XFile;

import java.util.Collections;
import java.util.List;

@WebListener
public class HibernateInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Khởi tạo EntityManagerFactory khi server khởi động
        System.out.println("Initializing Hibernate EntityManagerFactory...");
        EntityManagerFactoryUtil.initialize(); // Kích hoạt khởi tạo

        // Đọc số đếm từ nguồn lưu trữ file
        XFile.createFileIfNotExists("visitor-count.txt");
        List<String> listString = XFile.readFile("visitor-count.txt");
        Integer visitors = null;
        if(listString.size() > 0) {
            visitors = Integer.parseInt(listString.get(0));
        }

        ServletContext context = sce.getServletContext();
        context.setAttribute("visitors", visitors);
        System.out.println("Visitor counter initialized with: " + visitors);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Lưu số đếm vào nguồn lưu trữ
        ServletContext context = sce.getServletContext();
        Integer visitors = (Integer) context.getAttribute("visitors");
        if (visitors != null) {
            System.out.println("Saving visitor count: " + visitors);
            // Lưu số đếm vào file hoặc database
            List<String> visitorsList = Collections.singletonList(visitors.toString());
            String notify = XFile.modifyFile("visitor-count.txt", visitorsList) ? "Saved" : "Failed";
            System.out.println(notify);
        }

        // Đóng EntityManagerFactory khi server dừng
        System.out.println("Closing Hibernate EntityManagerFactory...");
        EntityManagerFactoryUtil.closeEntityManagerFactory();
    }
}
