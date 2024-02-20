package com.ieris19.dashboard.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Dashboard {
    private static final String DEFAULT_ICON_NAME = "default-icon.svg";
    private String dashboardTitle;
    private File iconSource;
    private boolean searchBox;
    private List<DashboardLink> linkList;

public static Dashboard fromXML(File metadataFile) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(metadataFile);
            Element root = doc.getDocumentElement();
            String title = root.getAttribute("title");
            String iconName = root.getAttribute("icon");
            boolean search = Boolean.parseBoolean(root.getElementsByTagName("search").item(0).getAttributes().getNamedItem("include").getTextContent());
            List<DashboardLink> links = new ArrayList<>();
            NodeList linkNodes = root.getElementsByTagName("link");
            for (int i = 0; i < linkNodes.getLength(); i++) {
                String url = linkNodes.item(i).getAttributes().getNamedItem("url").getTextContent();
                String linkTitle = linkNodes.item(i).getTextContent();
                links.add(new DashboardLink(linkTitle, url));
            }
            File icon = new File(metadataFile.getParent(), "assets/images/" + iconName);
            return new Dashboard(title, icon, search, links);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid XML file", e);
        }
}

    public String getDocument() {
        return this.composeDocument(readTemplate());
    }

    private String readTemplate() {
        try (InputStream in = this.getClass().getResourceAsStream("index.html")) {
            if (in == null) {
                throw new IOException();
            }
            byte[] text = in.readAllBytes();
            if (text == null) {
                throw new IOException();
            }
            return new String(text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading internal template");
        }
    }

    private String composeDocument(String template) {
        return template.replace("$title", this.getDashboardTitle())
                .replace("$iconName", getIconName())
                .replace("$search", this.isSearchBox() ? searchBoxHtml() : "")
                .replace("$grid", buttonGridHtml());
    }

    private String searchBoxHtml() {
        return """
                    <div class="search">
                        <label for="search-bar">Google: </label>
                        <input type="text" id="search-bar" placeholder="Search...">
                        <button id="search-btn">
                            &#x1F50E; <!-- Magnifying Glass Emoji -->
                        </button>
                    </div>
                    <script type="application/javascript">
                        addSearchBarJS()
                    </script>
                """;
    }

        private String buttonGridHtml() {
        StringBuilder sb = new StringBuilder("<div class=\"button-grid\">");
        for (DashboardLink link : this.getLinkList()) {
            sb.append("\n<a target=\"_blank\" href=\"")
                    .append(link.getUrl())
                    .append("\">")
//                     .append(getLinkImage(link.getUrl())
                    .append(link.getTitle())
                    .append("</a>");

        }
        return sb.append("\n</div>\n").toString();
    }

    public String getIconName() {
        return iconSource == null ? DEFAULT_ICON_NAME : iconSource.getName();
    }

    public Document toXML() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("dashboard");
            Element search = doc.createElement("search");
            search.setAttribute("include", String.valueOf(searchBox));
            root.appendChild(search);
            root.setAttribute("title", dashboardTitle);
            root.setAttribute("icon", getIconName());
            doc.appendChild(root);
            Element grid = doc.createElement("grid");
            root.appendChild(grid);
            for (DashboardLink link : linkList) {
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
}

