package ru.goncharov.cvem.model.data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class CveSourceResult {

    private String url;
    private String cveId;
    private String description;
    private Set<String> references = new HashSet<>();
    private String dateEntryCreated;

    public CveSourceResult() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getCveId() {
        return cveId;
    }

    public void setCveId(final String cveId) {
        this.cveId = cveId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<String> getReferences() {
        return references;
    }

    public void setReferences(final Set<String> references) {
        this.references = references;
    }

    public String getDateEntryCreated() {
        return dateEntryCreated;
    }

    public void setDateEntryCreated(final String dateEntryCreated) {
        this.dateEntryCreated = dateEntryCreated;
    }
}
