<html>
    <body>
        <h2>Hello World!</h2>
        <form action="${pageContext.request.contextPath}/login" method="get">
            用户名: <input type="text" name="username"> <br>
            密码: <input type="password" name="password"> <br>

            <input type="checkbox" name="favorite" value="nba"> NBA ;  //favorite
            <input type="checkbox" name="favorite" value="music"> 音乐 ; //favorite
            <input type="checkbox" name="favorite" value="movie"> 电影 ;  //favorite
            <input type="checkbox" name="favorite" value="internet"> 上网 ; //favorite
            <input type="submit">
        </form>
    </body>
</html>
