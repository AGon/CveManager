package ru.goncharov.cvem.parser;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import ru.goncharov.cvem.model.CveManager;
import ru.goncharov.cvem.model.data.CveSourceInfo;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class AbstractCveSourceParserTest extends Assert {

    final String TEST_URL = "http://example.com/";
    final String TEST_CVE = "CVE-2016-5833";
    final String TEST_CVE_URL = TEST_URL + TEST_CVE;

    @Mock
    CveSourceParser parser;

    @Mock
    CveManager manager;

    @Before
    public void setUp() {
        parser = mock(AbstractCveSourceParser.class, Mockito.CALLS_REAL_METHODS);

        manager = mock(CveManager.class);
        CveSourceInfo sourceInfo = mock(CveSourceInfo.class);
        when(sourceInfo.getSearchUrl()).thenReturn(TEST_URL);
        when(manager.getSourceInfo()).thenReturn(sourceInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_throw_IllegalArgumentException() {
        parser.update(null, null);
    }

    @Test
    public void update_call_setter() {
        parser.update(manager, TEST_CVE);
        assertThat(TEST_CVE_URL, equalTo(parser.getCveUrl()));
    }

    @Test
    public void update_call_parse_method() {
        parser.update(manager, TEST_CVE);
        verify(parser, times(1)).parse();
    }

    @Test
    public void setCveUrl_call_setter_for_change_document() {
        when(parser.getCveUrl()).thenReturn(TEST_CVE_URL);

        parser.setCveUrl(TEST_CVE_URL);
        verify(parser, times(0)).setDocument(null);

        parser.setCveUrl(TEST_URL + "CVE-2016-5834");
        verify(parser, times(1)).setDocument(null);
    }

    @Test
    public void getDocument_not_null() {
        Document document = mock(Document.class);
        when(parser.getDocument()).thenReturn(document);

        assertThat(parser.getDocument(), equalTo(document));
    }

    @Test
    public void getDocument_null_and_call_load_method() throws Exception {
        AbstractCveSourceParser abstractCveSourceParser = new AbstractCveSourceParser() { // TODO Разобраться
            @Override
            public void parse() {
            }
        };
        AbstractCveSourceParser spy = PowerMockito.spy(abstractCveSourceParser);
        spy.getDocument();
        verifyPrivate(spy, times(1)).invoke("loadDocument");
    }

    @Test
    public void loadDocument_return_null() throws Exception {
        when(parser.getCveUrl()).thenReturn("- - - - - -");
        Document document = Whitebox.invokeMethod(parser, "loadDocument");
        assertThat(document, is(nullValue()));
    }

    @Test
    public void loadDocument_throw_IOException() throws Exception {
        when(parser.getCveUrl()).thenReturn("my_http://my_domain/CVE-2016-5833");
        Document document = null;
        try {
            document = Whitebox.invokeMethod(parser, "loadDocument");
        } catch (IOException | IllegalArgumentException e) {
            fail();
        }
        assertThat(document, is(nullValue()));
    }
}
