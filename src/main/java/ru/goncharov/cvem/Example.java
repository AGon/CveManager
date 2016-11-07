package ru.goncharov.cvem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.goncharov.cvem.model.CveManager;
import ru.goncharov.cvem.parser.CveSourceParser;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Example {

    private static ApplicationContext context;

    static {
        context = new ClassPathXmlApplicationContext("classpath:app-config.xml");
    }

    public static void main(String[] args) {
        CveManager source = context.getBean("mitre", CveManager.class);
        CveSourceParser parser = source.getParser();
        for (int lastDigit = 0; lastDigit < 10; lastDigit++) {
            String cveId = "CVE-2015-583" + lastDigit;
            parser.update(source, cveId);
            printCveInfo(parser);
        }
    }

    private static void printCveInfo(final CveSourceParser parser) {
        AtomicReference<StringBuilder> builder = new AtomicReference<>(new StringBuilder());
        builder.get().append("[");
        builder.get().append(parser.getResult().getCveId());
        builder.get().append("] ");
        builder.get().append(parser.getResult().getDescription());
        builder.get().append(" (");
        builder.get().append(parser.getResult().getDateEntryCreated());
        builder.get().append(")");
        builder.get().append("\n");
        builder.get().append("\t").append("> ").append(parser.getResult().getUrl()).append("\n");
        Set<String> references = parser.getResult().getReferences();
        for (String reference : references) {
            builder.get().append("\t").append("- ").append(reference).append("\n");
        }
        System.out.println(builder.get().toString());
    }
}
