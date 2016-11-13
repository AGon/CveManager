package ru.goncharov.cvem.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.goncharov.cvem.model.CveManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public final class CveUtils {

    private static final Logger LOG = LogManager.getLogger(CveUtils.class.getName());

    public static final String CVE_PREFIX = "CVE-";
    public static final String CVE_REGEX = CVE_PREFIX + "\\d{4}-\\d{4,7}";

    private CveUtils() {
    }

    public static boolean isEmpty(final String text) {
        return StringUtils.isBlank(text);
    }

    public static boolean isCveUrl(final String cveUrl) {
        if (isEmpty(cveUrl)) {
            return false;
        } else {
            String cveFromUrl = getCveFromUrl(cveUrl);
            return isCveId(cveFromUrl);
        }
    }

    public static String getCveFromUrl(final String cveUrl) {
        int from = cveUrl.lastIndexOf(CVE_PREFIX);
        int to = cveUrl.length();
        if (from != -1) {
            return cveUrl.substring(from, to);
        } else {
            return null;
        }
    }

    public static String getCveUrl(final CveManager cveManager, final String cveId) {
        if (cveManager == null) {
            throw new IllegalArgumentException("cveSource is null");
        } else {
            String cveUrl = null;
            if (isCveId(cveId)) {
                cveUrl = cveManager.getSourceInfo().getSearchUrl() + cveId;
            } else {
                LOG.error("cveId incorrect");
            }
            return cveUrl;
        }
    }

    public static boolean isCveId(final String cveId) {
        if (isEmpty(cveId)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(CVE_REGEX);
            Matcher matcher = pattern.matcher(cveId);
            return matcher.matches();
        }
    }
}
