package demand;

import config.ApplicationProperties;
import config.factory.PropertiesFactory;
import deal.DealFileWay;
import demand.general.process.ArticleProcess;
import demand.general.GeneralArticleDeal;
import demand.general.process.process_way.InvalidProcess;
import demand.general.record.CsvRowRecordExtract;
import demand.general.record.RowRecordExtract;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RowRecordExtractTest {
    ApplicationProperties aps = new ApplicationProperties();

    RowRecordExtract rowRecordExtract;
    @Test
    public void testCsv() throws IOException {
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeTest"));
        String path = aps.getPrimaryProperties().getPath();
        rowRecordExtract = new CsvRowRecordExtract(',');
        List<String[]> result = rowRecordExtract.getRawRecords();
        System.out.println(result.size());
        System.out.println(result.get(0));
        List<String[]> result2 = rowRecordExtract.getRawRecords(2000);
        System.out.println(result2.size());
    }

    @Test
    public void testGeneralArticleDeal() throws IOException {
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeTest"));
        String path = aps.getPrimaryProperties().getPath();
        this.rowRecordExtract = new CsvRowRecordExtract(',');
        Object[] labelHeaders = aps.getPrimaryProperties().getLabels();
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();
        RowRecordExtract rowRecordExtract = new CsvRowRecordExtract();
        ArticleProcess articleProcess = new ArticleProcess(new InvalidProcess());
        DealFileWay dealFileWay = new GeneralArticleDeal(rowRecordExtract, articleProcess, aps);
        List<String> resutlList = dealFileWay.extractedValue(path);

        resutlList.forEach(System.out::println);
    }

    @Test
    public void testGeneralSpliceDeal() throws IOException {
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeTest"));
        String path = aps.getPrimaryProperties().getPath();
        this.rowRecordExtract = new CsvRowRecordExtract(',');
        Object[] labelHeaders = aps.getPrimaryProperties().getLabels();
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();
        RowRecordExtract rowRecordExtract = new CsvRowRecordExtract();
        ArticleProcess articleProcess = new ArticleProcess(new InvalidProcess());
        DealFileWay dealFileWay = new GeneralArticleDeal(rowRecordExtract, articleProcess, aps);
        List<String> resutlList = dealFileWay.extractedValue(path);

        resutlList.forEach(System.out::println);

    }


}