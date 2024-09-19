package community.register.documents.generators;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DocumentGenerator {
    void generateDocument() throws IOException;
}
