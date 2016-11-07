package ru.goncharov.cvem.model.data;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class CveSourceInfo {

    private final String sourceName;
    private final String sourceSite;
    private final String searchUrl;

    public CveSourceInfo(final String sourceName, final String sourceSite, final String searchUrl) {
        this.sourceName = sourceName;
        this.sourceSite = sourceSite;
        this.searchUrl = searchUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceSite() {
        return sourceSite;
    }

    public String getSearchUrl() {
        return searchUrl;
    }
}
