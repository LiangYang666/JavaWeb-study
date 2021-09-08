package com.liang.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailDemo01Use163 {
    public static void main(String[] args) throws Exception{
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.163.com");  //设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

//        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
//        MailSSLSocketFactory sf = new MailSSLSocketFactory();
//        sf.setTrustAllHosts(true);
//        prop.put("mail.smtp.ssl.enable", "true");
//        prop.put("mail.smtp.ssl.socketFactory", sf);

        //使用JavaMail发送邮件的5个步骤

        //1、创建定义整个应用程序所需的环境信息的 Session 对象
        //使用QQ邮箱的时候才需要，其他邮箱不需要这一段代码
        Session session = Session.getDefaultInstance(prop, new Authenticator() {//获取和SMTP服务器的连接对象
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication("mail_liangyang@163.com", "DCADSIOHWCCLOOCT");
            }
        });

        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        //2、通过session得到transport对象
        Transport ts = session.getTransport();//通过这一次和SMTP服务器的连接对象获取发送邮件的传输对象

        //3、使用邮箱的用户名和授权码连上SMTP邮件服务器，即登陆
        ts.connect("smtp.163.com", "mail_liangyang@163.com", "DCADSIOHWCCLOOCT");

        //4、创建邮件对象MimeMessage——点击网页上的写信
        //创建一个邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人——在网页上填写发件人
        message.setFrom(new InternetAddress("mail_liangyang@163.com"));//设置发件人
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发——在网页上填写收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1041612547@qq.com"));//设置收件人
        //邮件的标题——在网页上填写邮件标题
        message.setSubject("简单邮件发送实现");//设置邮件主题
        //邮件的文本内容——在网页上填写邮件内容
        message.setContent("<h2 style='color:red'>你好啊！</h2>", "text/html;charset=UTF-8");//设置邮件的具体内容

        //5、发送邮件——在网页上点击发送按钮
        ts.sendMessage(message, message.getAllRecipients());

        //6、关闭连接对象，即关闭服务器上的连接资源
        ts.close();

    }
}
