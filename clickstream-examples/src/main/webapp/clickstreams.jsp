<%@ page import="java.util.*, com.google.code.clickstream.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
    final Map clickstreams = (Map) application.getAttribute("clickstreams");

    String showbots = com.google.code.clickstream.ClickstreamExtensionUtils.detectShowbots(request.getParameter("showbots"));
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Active Clickstreams</title>
    </head>

    <body>
        <h3>Active Clickstreams</h3>

        <p>
            <a href="?showbots=false">User Streams</a> |
            <a href="?showbots=true">Bot Streams</a> |
            <a href="?showbots=both">Both</a>
        </p>

        <% if (clickstreams.isEmpty()) { %>
        <p>No clickstreams in progress.</p>
        <% } else { %>
        <ol>
        <%
            synchronized(clickstreams) {
                Iterator it = clickstreams.keySet().iterator();
                while (it.hasNext())
                {
                    String key = (String)it.next();
                    Clickstream stream = (Clickstream)clickstreams.get(key);

                    if (showbots.equals("false") && stream.isBot())
                    {
                        continue;
                    }
                    else if (showbots.equals("true") && !stream.isBot())
                    {
                        continue;
                    }

                    try {
                %>
                <li><a href="viewstream.jsp?sid=<%= key %>"><b><%= (stream.getHostname() != null && !stream.getHostname().equals("") ? stream.getHostname() : "Stream") %></b></a> <small>[<%= stream.getStream().size() %> reqs]</small></li>
                <%
                    }
                    catch (Exception e)
                    {
                %>
                    An error occurred - <%= e %><br>
                <%
                    }
                }
            }
        }
        %>
        </ol>
    </body>
</html>