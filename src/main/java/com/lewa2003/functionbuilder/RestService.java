package com.lewa2003.functionbuilder;

import java.util.List;

public class RestService {
    private String name;
    private String host;
    private String method;
    private String url;
    private List<Parameter> parameters;

    public String getName() {
        return name;
    }
    public String getHost() { return host; }
    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public List<Parameter> getParameters() { return parameters; }

    public void setName(String name) { this.name = name; }
    public void setHost(String host) { this.host = host; }
    public void setMethod(String method) { this.method = method; }
    public void setUrl(String url) { this.url = url; }
    public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
}

