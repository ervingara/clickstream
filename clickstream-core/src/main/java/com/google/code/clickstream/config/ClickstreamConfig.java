package com.google.code.clickstream.config;

import java.util.List;
import java.util.ArrayList;

/**
 * Clickstream configuration data.
 *
 * @author <a href="plightbo@hotmail.com">Patrick Lightbody</a>
 * @author <a href="contact@chenwang.org">Chen Wang</a>
 */
public class ClickstreamConfig {
    private String loggerClass;
    private List<String> botAgents = new ArrayList();
    private List<String> botHosts = new ArrayList();

    public String getLoggerClass() {
        return loggerClass;
    }

    public void setLoggerClass(String loggerClass) {
        this.loggerClass = loggerClass;
    }

    public void addBotAgent(String agent) {
        botAgents.add(agent);
    }

    public void addBotHost(String host) {
        botHosts.add(host);
    }

    public List<String> getBotAgents() {
        return botAgents;
    }

    public List<String> getBotHosts() {
        return botHosts;
    }
}
