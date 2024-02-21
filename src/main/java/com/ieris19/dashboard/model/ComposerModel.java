package com.ieris19.dashboard.model;

import com.ieris19.dashboard.data.DashboardBuilder;
import com.ieris19.dashboard.data.DashboardLink;
import com.ieris19.lib.ui.mvvm.Model;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ComposerModel implements Model {

    private DashboardBuilder builder;

    public ComposerModel() {
        try {
            this.builder = new DashboardBuilder();
            this.builder.getMetadata().load();
        } catch (IOException e) {
            log.error("Error creating dashboard builder", e);
        }
    }

    public void submit(String title, File icon, boolean hasSearch, List<DashboardLink> links) {
        this.builder.setTitle(title);
        this.builder.setIcon(icon);
        this.builder.includeSearch(hasSearch);
        this.builder.setLinks(links);
        this.builder.export();
    }

    public DashboardBuilder getCurrentDashboard() {
        return this.builder;
    }
}
