/*
 *  Copyright 2010 Chen Wang.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.google.code.clickstream;

import com.google.code.clickstream.Clickstream;
import com.google.code.clickstream.ClickstreamRequest;
import java.io.PrintWriter;
import java.util.Map;

/**
 * This class provides common utilities for extending clickstream.
 *
 * @author <a href="mailto:contact@chenwang.org">Chen Wang</a>
 */
public class ClickstreamExtensionUtils {

    private ClickstreamExtensionUtils() {}

    public static String detectShowbots(String value) {
        String showbots = "false";
        if ("true".equalsIgnoreCase(value)) {
            showbots = "true";
        }
        else if ("both".equalsIgnoreCase(value)) {
            showbots = "both";
        }
        return showbots;
    }


    /**
     * Prints the page header.
     * @param out output writer
     * @param title page title
     */
    public static void printHeader(PrintWriter out, String title) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");

        out.println("<head><title>");
        out.println(title);
        out.println("</title></head>");

        out.println("<body>");
        out.print("<h3>");
        out.print(title);
        out.println("</h3>");
    }

    /**
     * Print out the active clickstream list.
     *
     * @param request HttpServletRequest
     * @param out PrintWriter
     */
    public static void printClickstreamList(Map<String, Clickstream> clickstreams, PrintWriter out, boolean isFragment, String showbots) {
        if (!isFragment) {
            printHeader(out, "Active Clickstreams");
        }

        out.println("<p>");

        if ("true".equals(showbots)) {
            out.println("<a href=\"?showbots=false\">User Streams</a>");
            out.println(" | ");
            out.println("<strong>Bot Streams</strong>");
        }
        else if ("both".equalsIgnoreCase(showbots)) {
            out.println("<a href=\"?showbots=false\">User Streams</a>");
            out.println(" | ");
            out.println("<a href=\"?showbots=true\">Bot Streams</a>");
        }
        else {
            out.println("<strong>User Streams</strong>");
            out.println(" | ");
            out.println("<a href=\"?showbots=true\">Bot Streams</a>");
        }

        out.println(" | ");

        // showBots is TRUE or FALSE
        if (!"both".equalsIgnoreCase(showbots)) {
            out.println("<a href=\"?showbots=both\">Both</a>");
        }
        else {
            out.println("<strong>Both</strong>");
        }

        out.println("</p>");

        if (clickstreams.isEmpty()) {
            out.println("<p>No clickstreams in progress.</p>");
        }
        else {
            synchronized (clickstreams) {
                out.print("<ol>");
                for (Map.Entry<String, Clickstream> entry : clickstreams.entrySet()) {
                    String key = entry.getKey();
                    Clickstream stream = entry.getValue();

                    if (showbots.equals("false") && stream.isBot()) {
                        continue;
                    }
                    else if (showbots.equals("true") && !stream.isBot()) {
                        continue;
                    }

                    String hostname = (stream.getHostname() != null && !"".equals(stream.getHostname()) ? stream
                            .getHostname() : "Stream");

                    out.print("<li>");

                    out.print("<a href=\"?sid=");
                    out.print(key);
                    out.print("&showbots=");
                    out.write(showbots);
                    out.write("\">");
                    out.write("<strong>");
                    out.print(hostname);
                    out.print("</strong>");
                    out.print("</a> ");
                    out.print("<small>[");
                    out.print(stream.getStream().size());
                    out.print(" reqs]</small>");

                    out.print("</li>");
                }
                out.print("</ol>");
            }
        }
    }


    /**
     * Received the "sid" parameter, print out the stream detail.
     *
     * @param request HttpServletRequest
     * @param out PrintWriter
     * @param sid session id
     */
    public static void printClickstreamDetail(Map<String, Clickstream> clickstreams, PrintWriter out, String sid, boolean isFragment, String showbots) {

        Clickstream stream = clickstreams.get(sid);

        out.println("<p align=\"right\"><a href=\"?");
        if (showbots != null) {
            out.print("showbots=");
            out.print(showbots);
        }
        out.print("\">All streams</a>");

        if (stream == null) {
            if (!isFragment) {
                printHeader(out, "Clickstream for " + sid);
            }

            out.write("<p>Session for " + sid + " has expired.</p>");
            return;
        }

        if (!isFragment) {
            printHeader(out, "Clickstream for " + stream.getHostname());
        }

        out.println("<ul>");

        if (stream.getInitialReferrer() != null) {
            out.println("<li>");
            out.println("<strong>Initial Referrer</strong>: ");
            out.print("<a href=\"");
            out.print(stream.getInitialReferrer());
            out.print("\">");
            out.print(stream.getInitialReferrer());
            out.println("</a>");
            out.println("</li>");
        }

        out.println("<li>");
        out.println("<strong>Hostname</strong>: ");
        out.println(stream.getHostname());
        out.println("</li>");

        out.println("<li>");
        out.println("<strong>Session ID</strong>: ");
        out.println(sid);
        out.println("</li>");

        out.println("<li>");
        out.println("<strong>Bot</strong>: ");
        out.println(stream.isBot() ? "Yes" : "No");
        out.println("</li>");

        out.println("<li>");
        out.println("<strong>Stream Start</strong>: ");
        out.println(stream.getStart());
        out.println("</li>");

        out.println("<li>");
        out.println("<strong>Last Request</strong>: ");
        out.println(stream.getLastRequest());
        out.println("</li>");

        out.println("<li>");
        out.println("<strong>Session Length</strong>: ");
        long streamLength = stream.getLastRequest().getTime() - stream.getStart().getTime();
        if (streamLength > 3600000) {
            out.print((streamLength / 3600000) + " hours ");
        }
        if (streamLength > 60000) {
            out.print(((streamLength / 60000) % 60) + " minutes ");
        }
        if (streamLength > 1000) {
            out.print(((streamLength / 1000) % 60) + " seconds");
        }
        out.println("</li>");

        out.println("<li>");
        out.println("<strong># of Requests</strong>: ");
        out.println(stream.getStream().size());
        out.println("</li>");

        out.println("</ul>");

        out.println("<h3>Click stream:</h3>");

        synchronized (stream) {

            out.print("<ol>");

            for (ClickstreamRequest cr : stream.getStream()) {
                String click = cr.toString();

                out.write("<li>");
                out.write("<a href=\"http://");
                out.print(click);
                out.write("\">");
                out.print(click);
                out.write("</a>");
                out.write("</li>");

            }
            out.print("</ol>");
        }

    }
}
