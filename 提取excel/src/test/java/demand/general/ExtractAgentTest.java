package demand.general;

import config.ApplicationProperties;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExtractAgentTest {
    ExtractAgent extractAgent = new ExtractAgent();
    @Test
    public void appendKeywordFromEventExcel() throws IOException {
        extractAgent.setAps(new ApplicationProperties());
        extractAgent.appendKeywordFromEventExcel();
    }
}