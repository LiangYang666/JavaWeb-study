package com.liang.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.Format;
import java.util.Random;

public class ImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("refresh", "3");
        BufferedImage image = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 80, 20);
        g.setColor(Color.blue);
        g.setFont(new Font(null, Font.BOLD,20));
        g.drawString(getRandomNum(), 0, 20);
        resp.setContentType("image/jpeg");
        resp.setDateHeader("expires", -1);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragrma", "no-cache");

        ImageIO.write(image, "jpg", resp.getOutputStream());

    }
    private String getRandomNum(){
        Random random = new Random();
        int i = random.nextInt(999999);
        String num = String.format("%6d", i);
        return num;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
