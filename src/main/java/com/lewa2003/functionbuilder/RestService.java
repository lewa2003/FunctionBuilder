package com.lewa2003.functionbuilder;

import java.util.ArrayList;
import java.util.List;

public class RestService {
    private String name;
    private String host;
    private String method;
    private String url;
    private List<Parameter> parameters;

    public void actualize() {
        if (name == null) {
            name = new String();
        }
        if (host == null) {
            host = new String();
        }
        if (method == null) {
            method = new String();
        }
        if (url == null) {
            url = new String();
        }
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RestService service = (RestService) o;
        return (name == null ? service.getName() == null : name.equals(service.getName())) &&
                (host == null ? service.getHost() == null : host.equals(service.getHost())) &&
                (method == null ? service.getMethod() == null : method.equals(service.getMethod())) &&
                (url == null ? service.getUrl() == null : url.equals(service.getUrl())) &&
                (parameters == null ? service.getParameters() == null : parameters.equals(service.getParameters()));
    }
}

