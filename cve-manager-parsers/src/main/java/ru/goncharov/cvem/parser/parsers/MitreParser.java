package ru.goncharov.cvem.parser.parsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.goncharov.cvem.model.data.CveSourceResult;
import ru.goncharov.cvem.parser.AbstractCveSourceParser;
import ru.goncharov.cvem.util.CveUtils;

import javax.swing.text.html.HTML;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * На момент времени: 07.11.2016
 *
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class MitreParser extends AbstractCveSourceParser {

    private static final Logger LOG = LogManager.getLogger(MitreParser.class.getName());

    public MitreParser() {
        super();
    }

    @Override
    public void parse() {
        CveSourceResult result = new CveSourceResult();
        setResult(result);

        getResult().setUrl(getCveUrl());
        getResult().setCveId(cveId());
        getResult().setDescription(description());
        getResult().setReferences(references());
        getResult().setDateEntryCreated(dateEntryCreated());
    }

    private String cveId() {
        return CveUtils.getCveFromUrl(getCveUrl());
    }

    private String description() {
        Element table = getTableWithCveInfo(getDocument());
        if (table == null) {
            return null;
        }
        Elements rows = getRows(table);
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        Element row = getDescriptionRow(rows);
        if (row == null) {
            return null;
        }
        Elements cols = getCols(row);
        if (cols == null || cols.isEmpty()) {
            return null;
        }
        Element element = getFirstCol(cols);
        if (element == null) {
            return null;
        }
        return getText(element);
    }

    private Set<String> references() {
        Element table = getTableWithCveInfo(getDocument());
        if (table == null) {
            return null;
        }
        Elements rows = getRows(table);
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        Element row = getReferencesRow(rows);
        if (row == null) {
            return null;
        }
        String referencesText = row.text().trim();
        return parseReferences(referencesText);
    }

    private String dateEntryCreated() {
        Element table = getTableWithCveInfo(getDocument());
        if (table == null) {
            return null;
        }
        Elements rows = getRows(table);
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        Element row = getDateEntryCreatedRow(rows);
        if (row == null) {
            return null;
        }
        Elements cols = getCols(row);
        if (cols == null || cols.isEmpty()) {
            return null;
        }
        Element element = getFirstCol(cols);
        if (element == null) {
            return null;
        }
        String dateEntryCreated = getText(element);
        return parseDate(dateEntryCreated);
    }

    private static Element getTableWithCveInfo(final Document document) {
        Element element = null;
        if (!Objects.isNull(document)) {
            element = document.select(HTML.Tag.TABLE.toString()).get(1);
        } else {
            LOG.error("Document is null");
        }
        return element;
    }

    private static Elements getRows(final Element table) {
        return table.select(HTML.Tag.TR.toString());
    }

    private static Elements getCols(final Element row) {
        return row.select(HTML.Tag.TD.toString());
    }

    private static Element getDescriptionRow(final Elements rows) {
        final int descriptionRowNumber = 3;
        return getRow(rows, descriptionRowNumber);
    }

    private static Element getReferencesRow(final Elements rows) {
        final int dateEntryCreatedRowNumber = 6;
        return getRow(rows, dateEntryCreatedRowNumber);
    }

    private static Element getDateEntryCreatedRow(final Elements rows) {
        final int dateEntryCreatedRowNumber = 8;
        return getRow(rows, dateEntryCreatedRowNumber);
    }

    private static Element getRow(final Elements rows, final int number) {
        if (rows.size() > number) {
            return rows.get(number);
        } else {
            return null;
        }
    }

    private static Element getFirstCol(final Elements cols) {
        return getCol(cols, 0);
    }

    private static Element getCol(final Elements cols, final int number) {
        return cols.get(number);
    }

    private static String getText(final Element element) {
        return element.text().trim();
    }

    private String parseDate(final String text) {
        final String inputFormat = "yyyyMMdd";
        final String outputFormat = "dd.MM.yyyy";
        String result = null;
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat, Locale.getDefault());
            Date date = inputDateFormat.parse(text);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());
            result = outputDateFormat.format(date);
        } catch (ParseException e) {
            LOG.error("date parse error");
        }
        return result;
    }

    private Set<String> parseReferences(final String referencesText) {
        final String httpPattern = ":http";
        Set<String> result = new HashSet<>();
        String[] allReferences = referencesText.split(" ");
        for (String reference : allReferences) {
            if (reference.trim().contains(httpPattern)) {
                int beginIndex = reference.indexOf(httpPattern) + 1;
                String parseReference = reference.substring(beginIndex);
                result.add(parseReference);
            }
        }
        return result;
    }
}
