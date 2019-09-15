<%@ page contentType="text/html; charset=UTF-8"  %>
<html>
<head>
    <title>tit</title>
    <meta http-equiv="content-type" content="txt/html; charset=utf-8" />
</head>
<body>
<h1>Hello world!</h1>
<h3>wechatLogin.jsp</h3>
<form action="/wechatLogin" method="post">
    <div>
        帐号:<input name="openId" value="openId"/>
    </div>
    <div>
        密码:<input name="password" value="123456"/>
    </div>
    <input type="submit" value="登 录"/>
</form>
</body>
</html>