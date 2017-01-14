package com.rv.readData.yahooApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

public class StockCheckerYahooApiTest {

  //@Test
  public void filterStocks() throws IOException {

    String file = "symbols/nasdaq.txt";
    File tempFile = new File("myTempFile.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    Path path = FileSystems.getDefault().getPath(".", file);
    InputStream in = Files.newInputStream(path);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    List<String> lines = new ArrayList<>();
    String line = null;
    Calendar from = Calendar.getInstance();
    from.add(Calendar.MONTH, -2);
    Calendar to = Calendar.getInstance();
    Interval interval = Interval.DAILY;
    while ((line = reader.readLine()) != null) {
      String symbol = line.split("	")[0];
      try {
        Stock stock= YahooFinance.get(
            symbol, from, to, interval);
        writer.write(line + System.getProperty("line.separator"));

      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    reader.close();
    writer.close();
    //tempFile.renameTo(path.toFile());
  }
}
