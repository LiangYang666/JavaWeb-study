<html>
<body>
<h2>Hello World!</h2>
<form action="${pageContext.request.contextPath }/user.do" method="post" enctype="multipart/form-data">

    <p> 性别：<input type="text" name="text1"> value="男"</p>
    <p> <input type="file" name="file1"></p>
    <p> <input type="file" name="file2"></p>
    <p> <input type="submit" > | <input type="reset"></p>
</form>

</body>
</html>
