package ru.goncharov.cvem.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ru.goncharov.cvem.model.CveManager;
import ru.goncharov.cvem.model.data.CveSourceInfo;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.goncharov.cvem.util.CveUtils.*;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class CveUtilsTest extends Assert {

    @Mock
    CveManager manager;

    @Before
    public void setUp() {
        manager = mock(CveManager.class);
        CveSourceInfo sourceInfo = mock(CveSourceInfo.class);
        when(sourceInfo.getSearchUrl()).thenReturn("http://example.com/");
        when(manager.getSourceInfo()).thenReturn(sourceInfo);
    }

    @Test
    public void isEmptyTest() {
        assertTrue(isEmpty(""));
        assertTrue(isEmpty("     "));
        assertTrue(isEmpty(null));
    }

    @Test
    public void isCveUrlTest() {
        assertFalse(isCveUrl(""));
        assertFalse(isCveUrl("a"));
        assertFalse(isCveUrl("CVE"));
        assertFalse(isCveUrl("CVE-"));
        assertFalse(isCveUrl("CVE-CVE-CVE-"));
        assertFalse(isCveUrl("https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-201644-5833"));

        assertTrue(isCveUrl("https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2016-5833"));
    }

    @Test
    public void getCveUrlTest() {
        assertThat(getCveUrl(manager, "CVE-201666-5833"), is(nullValue()));
        assertThat("http://example.com/CVE-2016-5833", equalTo(getCveUrl(manager, "CVE-2016-5833")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCveUrl_throw_IllegalArgumentException() {
        getCveUrl(null, "");
        getCveUrl(null, null);
    }

    @Test
    public void isCveIdTest() {
        assertFalse(isCveId(""));
        assertFalse(isCveId("THECVE-5555-5555"));
        assertFalse(isCveId("CVE-55555-5555"));
        assertFalse(isCveId("CVE-5555-555"));
        assertFalse(isCveId("CVE-5555-5555555555"));

        assertTrue(isCveId("CVE-5555-5555"));
        assertTrue(isCveId("CVE-5555-55555"));
        assertTrue(isCveId("CVE-5555-555555"));
        assertTrue(isCveId("CVE-5555-5555555"));
    }
}
