package  com.mk.spark;
import com.mk.spark.ExternalResource;
import com.mk.spark.WordCountApp;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordCountAppTest {
    private static SparkSession spark;

    @BeforeAll
    public static void setup() {
        System.out.println("SPARK SESSION INITIALIZE");
        spark = SparkSession.builder()
                .appName("WordCountAppTest")
                .master("local[*]")
                .getOrCreate();
    }

    @Test
    public void testSparkSession() {
        assertNotNull(spark, "Spark session should not be null");
    }

    @Test
    public void testProcess() {
        // Mock the ExternalDataSource
        ExternalResource mockDataSource = mock(ExternalResource.class);
        List<String> mockData = Arrays.asList("hello world", "hello Spark");
        when(mockDataSource.fetchDataset()).thenReturn(mockData);

        // Create an instance of WordCountApp
        WordCountApp app = new WordCountApp(mockDataSource);

        // Call the process method
        Dataset<Row> result = app.process(spark);

        // Verify the results
        List<Row> output = result.collectAsList();
        assertEquals(3, output.size());
        long helloCount = result.filter(col("value").equalTo("hello")).first().getLong(1);
        assertEquals(2, helloCount);
    }
}
