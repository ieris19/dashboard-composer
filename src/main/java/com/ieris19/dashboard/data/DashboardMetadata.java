package com.ieris19.dashboard.data;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ieris19.dashboard.Composer.DESTINATION_FOLDER;

@Slf4j
public class DashboardMetadata {
    private final DashboardBuilder builder;

    public DashboardMetadata(DashboardBuilder builder) {
        this.builder = builder;
    }

    public void export() {
        Document metadata = this.toXML();
        File metadataFile = new File(DESTINATION_FOLDER, "metadata.xml");
        try (FileOutputStream out = new FileOutputStream(metadataFile, false)) {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(metadata), new StreamResult(out));
        } catch (TransformerConfigurationException e) {
            log.error("Error creating metadata", e);
        } catch (IOException | TransformerException e) {
            throw new IllegalStateException("Cannot write metadata", e);
        }
    }

    public Document toXML() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("dashboard");
            Element search = doc.createElement("search");
            search.setAttribute("include", String.valueOf(this.builder.hasSearch()));
            root.appendChild(search);
            root.setAttribute("title", this.builder.getTitle());
            root.setAttribute("icon", this.builder.getAssetManager().getIconPath());
            doc.appendChild(root);
            Element grid = doc.createElement("grid");
            root.appendChild(grid);
            for (DashboardLink link : this.builder.getLinks()) {
                Element linkElement = doc.createElement("link");
                linkElement.setAttribute("url", link.getUrl());
                linkElement.setTextContent(link.getTitle());
                grid.appendChild(linkElement);
            }
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        File metadataFile = new File(DESTINATION_FOLDER, "metadata.xml");
        if (metadataFile.exists()) {
            parse(metadataFile);
        } else {
            propagateToBuilder("", null, true, null);
        }
    }

    public void parse(File metadataFile) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(metadataFile);
            Element root = doc.getDocumentElement();
            String title = root.getAttribute("title");
            String iconName = root.getAttribute("icon");
            boolean search = Boolean.parseBoolean(root.getElementsByTagName("search").item(0).getAttributes().getNamedItem("include").getTextContent());
            NodeList linkNodes = root.getElementsByTagName("link");
            List<DashboardLink> links = new ArrayList<>();
            for (int i = 0; i < linkNodes.getLength(); i++) {
                String url = linkNodes.item(i).getAttributes().getNamedItem("url").getTextContent();
                String linkTitle = linkNodes.item(i).getTextContent();
                links.add(new DashboardLink(linkTitle, url));
            }
            File icon = new File(metadataFile.getParent(), iconName);
            propagateToBuilder(title, icon, search, links);
        } catch (ParserConfigurationException | IOException |
                 SAXException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid XML file", e);
        }
    }

    private void propagateToBuilder(String title, File icon, boolean search, List<DashboardLink> links) {
        this.builder.setTitle(title);
        this.builder.includeSearch(search);
        this.builder.setLinks(links);
        this.builder.getAssetManager().setIcon(icon);
    }
}
