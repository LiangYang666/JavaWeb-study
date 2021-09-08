package com.liang.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class FileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!ServletFileUpload.isMultipartContent(req)){
            System.out.println("这是普通表单");
            return;
        }

        String uploadDir = this.getServletContext().getRealPath("/WEB-INF/upload");
//        System.out.println(uploadDir);
        File uploadDirIns = new File(uploadDir);
        if (!uploadDirIns.exists()){
            if (uploadDirIns.mkdir()){
                System.out.println("创建上传文件夹"+uploadDir);
            }
        }
        String tempDir = this.getServletContext().getRealPath("/WEB-INF/temp");
        File tempDirIns = new File(tempDir);
        if (!tempDirIns.exists()){
            if (tempDirIns.mkdir()){
                System.out.println("创建上传文件夹"+tempDir);
            }
        }
        //--- 1
        //设置工厂模式 即设置缓冲区缓冲下载文件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*1024);
        factory.setRepository(tempDirIns);

        //--- 2
        // 获取 ServletFileUpload
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setProgressListener(new ProgressListener() {
            public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("总大小："+pContentLength+" 已上传："+pBytesRead);
            }
        });
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(1024*1024*10);    //10M

        //--- 3
        //处理上传文件
        try {
            List<FileItem> fileItems = upload.parseRequest(req);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");
                    System.out.println("得到的其它请求"+ name +" :" +value);
                } else {
                    String uploadFileName = fileItem.getName();
                    System.out.println("上传的文件名全称：" + uploadFileName);
                    if (uploadFileName==null || uploadFileName.trim().equals("")){
                        continue;
                    }
                    String filename = uploadFileName.substring(uploadFileName.lastIndexOf("/")+1);
                    String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
                    System.out.println("处理后的文件名：" +filename);
                    String uuidPath = UUID.randomUUID().toString();
                    String saveDir = uploadDir+"/"+uuidPath;
                    File saveDirIns = new File(saveDir);
                    if(!saveDirIns.exists()) {
                        if(saveDirIns.mkdir()){
                            System.out.println("创建目录成功 "+saveDir);
                        }
                    }
                    //处理上传流
                    InputStream inputStream = fileItem.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(saveDir + "/" + filename);
                    //写入流
                    int len = 0;
                    byte[] bytes = new byte[1024 * 1024];
                    while((len=inputStream.read(bytes))>0){
                        fileOutputStream.write(bytes, 0, len);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    String msg = "文件上传成功";
                    fileItem.delete();
                    System.out.println(msg);
                    if (msg.equals("文件上传成功")){
                        req.setAttribute("msg", msg);
                        req.getRequestDispatcher("/info.jsp").forward(req, resp);
                    } else {
                        msg = "上传失败";
                        req.setAttribute("msg", msg);
                        req.getRequestDispatcher("/info.jsp").forward(req, resp);
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }


    }

    public static DiskFileItemFactory getDiskFileItemFactory(File file){
        return null;
    }
    @Test
    public void test1(){
        String property = System.getProperty("java.io.tmpdir");
        System.out.println(property);
//        new DiskFileItemFactory

    }

}
