package com.liang.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class MailDemo01 {

    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");       //设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp");    //邮件发送协议
        prop.setProperty("mail.smtp.auth", "true");     //需要验证用户名密码


//        //关于QQ邮箱 还要设置SSL加密 加上以下代码即可  测试加了不能通 不加可
//        MailSSLSocketFactory sf = new MailSSLSocketFactory();
//        sf.setTrustAllHosts(true);
//        prop.put("mail.smtp.ssl.enable", "true");
//        prop.put("mail.smtp.ssl.socketFactory", sf);


        //使用JavaMail 发送邮件的五个步骤
        //1、创建升格应用程序所需的环境信息的 Session 对象
        //使用QQ邮箱时才需要， 其它邮箱不需要这一段代码
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1041612547@qq.com", "tuhjwbaojbzrbcdb");
            }
        });
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        // 2、通过Session得到transport对象
        Transport ts = session.getTransport();

        // 3、使用邮箱的用户名和授权码连上SMTP邮件服务器 即登录
        ts.connect("smtp.qq.com", "1041612547@qq.com", "tuhjwbaojbzrbcdb");

        // 4、创建邮件对象MimeMessage 即相当于网页上的写信按钮
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("1041612547@qq.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("mail_liangyang@163.com"));
        message.setSubject("简单邮件发送实现");
        message.setContent("<h2 style=\"color: red\">你好，我测试下简单邮件能否发送</h2>", "text/html;charset=UTF-8");

        // 5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());

        // 6、关闭连接对象 即关闭服务器上的连接资源
        ts.close();

    }
}
