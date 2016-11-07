package ru.goncharov.cvem.model;

import ru.goncharov.cvem.model.data.CveSourceInfo;
import ru.goncharov.cvem.parser.CveSourceParser;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public class CveManagerEntity implements CveManager {

    private final CveSourceParser sourceParser;
    private final CveSourceInfo sourceInfo;

    public CveManagerEntity(final CveSourceParser sourceParser, final CveSourceInfo sourceInfo) {
        this.sourceParser = sourceParser;
        this.sourceInfo = sourceInfo;
    }

    @Override
    public CveSourceParser getParser() {
        return sourceParser;
    }

    @Override
    public CveSourceInfo getSourceInfo() {
        return sourceInfo;
    }

    @Override
    public String toString() {
        return getSourceInfo().getSourceName() + " (" + getSourceInfo().getSourceSite() + ")";
    }
}
