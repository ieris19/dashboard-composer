package com.ieris19.dashboard.data;

import com.ieris19.dashboard.Composer;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class DashboardBuilder {
    @Getter
    private final AssetManager assetManager;
    @Getter
    private final DashboardMetadata metadata;
    private final Document doc;
    @Getter
    private boolean hasSearch;
    @Getter
    private List<DashboardLink> links;

    public DashboardBuilder() throws IOException {
        this.assetManager = new AssetManager(Composer.DESTINATION_FOLDER);
        this.metadata = new DashboardMetadata(this);
        this.doc = Jsoup.parse(assetManager.getTemplate(), "UTF-8", Composer.DESTINATION_FOLDER.getAbsolutePath());
        this.doc.outputSettings(new Document.OutputSettings()
                .prettyPrint(false)
                .charset("UTF-8")
                .indentAmount(2)
                .syntax(Document.OutputSettings.Syntax.html));
    }

    public String getTitle() {
        return this.doc.title();
    }

    public DashboardBuilder setTitle(String title) {
        this.doc.title(title);
        return this;
    }

    public File getIcon() {
        return assetManager.getIcon();
    }

    public DashboardBuilder setIcon(File icon) {
        assetManager.setIcon(icon);
        return this;
    }

    public boolean hasSearch() {
        return this.hasSearch;
    }

    public DashboardBuilder includeSearch(boolean hasSearch) {
        this.hasSearch = hasSearch;
        return this;
    }

    public DashboardBuilder setLinks(List<DashboardLink> links) {
        this.links = links;
        return this;
    }

    public void export() {
        assetManager.exportIcon();
        setDocumentHead();
        configureSearch();
        try {
            configureLinks(Objects.requireNonNull(doc.selectFirst(".button-grid")), links);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Cannot find button-grid element", e);
        }
        try {
            Files.writeString(new File(Composer.DESTINATION_FOLDER, "index.html").toPath(), doc.outerHtml());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dashboard document", e);
        }
        this.metadata.export();
    }

    private void setDocumentHead() {
        assetManager.exportStylesheet();
        assetManager.exportScript();
        doc.select("head link[rel=icon]").attr("href", assetManager.getIconPath());
        doc.select("#logo").attr("src", assetManager.getIconPath());
        doc.select("#dashboard-title").forEach(e -> e.text(getTitle()));
        try {
            Objects.requireNonNull(doc.selectFirst("#dashboard-title")).text(getTitle());
        } catch (NullPointerException e) {
            throw new IllegalStateException("Cannot find title element", e);
        }
    }

    private void configureSearch() {
        if (!hasSearch) {
            doc.select("#search").remove();
        }
    }

    private void configureLinks(Element grid, List<DashboardLink> links) {
        if (links != null && !links.isEmpty()) {
            grid.children().remove();
            for (DashboardLink link : links) {
                doc.createElement("a")
                        .attr("href", link.getUrl())
                        .attr("target", "_blank")
                        .text(link.getTitle())
                        .appendTo(grid);
            }
        } else {
            grid.remove();
        }
    }
}
