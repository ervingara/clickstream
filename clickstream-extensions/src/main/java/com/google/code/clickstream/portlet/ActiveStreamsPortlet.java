package com.google.code.clickstream.portlet;

import com.google.code.clickstream.ClickstreamExtensionUtils;
import com.google.code.clickstream.Clickstream;
import com.google.code.clickstream.ClickstreamListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple portlet that shows active streams.
 * 
 * @author <a href="mailto:contact@chenwang.org">Chen Wang</a>
 */
public class ActiveStreamsPortlet extends GenericPortlet {

    private static final Log log = LogFactory.getLog(ActiveStreamsPortlet.class);

    private static final String CONFIG_FRAGMENT = "fragment";
        
    private boolean isFragment = false;

    @Override
    public void destroy() {
        log.info("destroyed at " + (new Date()));
    }

    @Override
    public void init(PortletConfig config) throws PortletException {
        String fragmentParam = config.getInitParameter(CONFIG_FRAGMENT);
        this.isFragment = ("true".equalsIgnoreCase(fragmentParam) || "yes".equalsIgnoreCase(fragmentParam));
        super.init(config);

        log.info("initialised with fragment [" + this.isFragment + "] at " + (new Date()));
    }


    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        Map<String, Clickstream> clickstreams = (Map<String, Clickstream>) this.getPortletContext().getAttribute(ClickstreamListener.CLICKSTREAMS_ATTRIBUTE_KEY);
        if (clickstreams == null) {
            throw new UnsupportedOperationException("The portal cannot support retrieving servlet context attributes from portlet context!");
        }

        // FIXME: always going to be text/html as content type?
//        if (! isFragment) {
            response.setContentType("text/html");
//        }

        String showbots = request.getParameter("showbots");
        String sid = request.getParameter("sid");
        
        PrintWriter out = response.getWriter();

        if (sid == null) {
            ClickstreamExtensionUtils.printClickstreamList(clickstreams, out, this.isFragment, ClickstreamExtensionUtils.detectShowbots(showbots));
        }
        else {
            ClickstreamExtensionUtils.printClickstreamDetail(clickstreams, out, sid, this.isFragment, showbots);
        }

        if (! isFragment) {
            out.println("</body></html>");
        }

        out.flush();
    }




}
