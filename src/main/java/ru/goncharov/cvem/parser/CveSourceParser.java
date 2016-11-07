package ru.goncharov.cvem.parser;

import org.jsoup.nodes.Document;
import ru.goncharov.cvem.model.CveManager;
import ru.goncharov.cvem.model.data.CveSourceResult;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public interface CveSourceParser {

    String getCveUrl();

    void setCveUrl(String cveUrl);

    Document getDocument();

    void setDocument(Document document);

    void parse();

    CveSourceResult getResult();

    void setResult(CveSourceResult result);

    void update(CveManager source, String cveId);

}
