package com.google.code.clickstream.logger;

import com.google.code.clickstream.Clickstream;

/**
 * A simple ClickstreamLogger that outputs nothing (Null Design Pattern).
 *
 * @author <a href="mailto:m.bogaert@memenco.com">Mathias Bogaert</a>
 */
public class NullClickstreamLogger implements ClickstreamLogger {
    public void log(Clickstream clickstream) {
    }
}
