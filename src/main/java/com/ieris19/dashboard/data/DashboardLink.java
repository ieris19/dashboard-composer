package com.ieris19.dashboard.data;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DashboardLink {
    private final String title;
    private final String url;
    private static int defaultEnumerator = 0;

    public DashboardLink(String title, String url) throws MalformedURLException {
        this.title = title.isBlank() ? "Button " + defaultEnumerator++ : title;
        this.url = url;
    }

    @Override
    public String toString() {
        return title + ' ' + '(' + url + ')';
    }
}
