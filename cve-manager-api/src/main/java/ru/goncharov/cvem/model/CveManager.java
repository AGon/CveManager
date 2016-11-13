package ru.goncharov.cvem.model;

import ru.goncharov.cvem.model.data.CveSourceInfo;
import ru.goncharov.cvem.parser.CveSourceParser;

/**
 * @author <a href="mailto:andre_goncharov@hotmail.com">Andre Goncharov</a>
 */
public interface CveManager {

    CveSourceParser getParser();

    CveSourceInfo getSourceInfo();

}
