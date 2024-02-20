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

    public static boolean verifyURL(String url) {
        if (!(url.startsWith("https://") || url.startsWith("http://") || url.startsWith("file://")))
            return false;
        try (InputStream stream = new URL(url).openStream()) {
            List<Integer> integers = new ArrayList<>();
            while (stream.available() > 0 && integers.isEmpty()) {
                integers.add(stream.read());
            }
            return !integers.isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return title + ' ' + '(' + url + ')';
    }
}
