package com.iscas.kong;

/**
 * Created by Summer on 2017/2/15.
 */
public class APIObject {
    private String upstream_url;
    private String created_at;
    private String request_path;
    private String id;
    private String name;
    private String preserve_host;
    private String strip_request_path;
    private String request_host;

    public String getUpstream_url() {
        return upstream_url;
    }

    public void setUpstream_url(String upstream_url) {
        this.upstream_url = upstream_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRequest_path() {
        return request_path;
    }

    public void setRequest_path(String request_path) {
        this.request_path = request_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreserve_host() {
        return preserve_host;
    }

    public void setPreserve_host(String preserve_host) {
        this.preserve_host = preserve_host;
    }

    public String getStrip_request_path() {
        return strip_request_path;
    }

    public void setStrip_request_path(String strip_request_path) {
        this.strip_request_path = strip_request_path;
    }

    public String getRequest_host() {
        return request_host;
    }

    public void setRequest_host(String request_host) {
        this.request_host = request_host;
    }
}
