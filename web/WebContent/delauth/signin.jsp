<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="-1"/>
    <title>Windows Live ID&trade; Delegated Authentication Sample</title>
</head>

<body>
<table width="320">
    <tr>
        <td>
            <iframe
                    id="WebAuthControl"
                    name="WebAuthControl"
                    src="http://login.live.com/controls/WebAuth.htm?appid=000000004001B438&style=font-size%3A+10pt%3B+font-family%3A+verdana%3B+background%3A+white%3B"
                    width="80px"
                    height="20px"
                    marginwidth="0"
                    marginheight="0"
                    align="middle"
                    frameborder="0"
                    scrolling="no">
            </iframe>
            <%

                String userid = request.getParameter("userid");
                String email = request.getParameter("email");
                if (email != null) {
                    out.println();
                    out.println("User Unique ID: " + userid);
                    out.println("Welcome " + email);
                } else
                    out.println("You are not authorized");

            %>
        </td>
    </tr>
</table>
</body>
</html>
