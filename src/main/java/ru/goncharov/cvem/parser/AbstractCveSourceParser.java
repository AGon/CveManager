package ru.goncharov.cvem.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.goncharov.cvem.model.CveManager;
import ru.goncharov.cvem.model.data.CveSourceResult;
import ru.goncharov.cvem.util.CveUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public abstract class AbstractCveSourceParser implements CveSourceParser {

    private static final Logger LOG = LogManager.getLogger(AbstractCveSourceParser.class.getName());

    private String cveUrl;
    private Document document;
    private CveSourceResult cveSourceResult;

    public AbstractCveSourceParser() {
    }

    @Override
    public void update(final CveManager source, final String cveId) {
        String cveUrl = CveUtils.getCveUrl(source, cveId);
        setCveUrl(cveUrl);

        parse();
    }

    @Override
    public String getCveUrl() {
        return cveUrl;
    }

    @Override
    public void setCveUrl(final String cveUrl) {
        if (getCveUrl() != null && !getCveUrl().equals(cveUrl)) { // Загрузить новый документ для другого CVE
            setDocument(null);
        }
        this.cveUrl = cveUrl;
    }

    @Override
    public Document getDocument() {
        if (Objects.isNull(this.document)) {
            setDocument(loadDocument());
        }
        return document;
    }

    @Override
    public void setDocument(final Document document) {
        this.document = document;
    }

    @Override
    public CveSourceResult getResult() {
        return cveSourceResult;
    }

    @Override
    public void setResult(final CveSourceResult cveSourceResult) {
        this.cveSourceResult = cveSourceResult;
    }

    private Document loadDocument() {
        if (CveUtils.isCveUrl(getCveUrl())) {
            try {
                return Jsoup.connect(getCveUrl()).get();
            } catch (IOException | IllegalArgumentException e) {
                LOG.error("connect error");
            }
        } else {
            LOG.error("cveUrl incorrect");
        }
        return null;
    }
}
