package stocReader;

import static org.junit.Assert.assertTrue;
import hello.MessageCollectorSender;
import hello.MessageSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.rv.readData.yahooApi.StockCheckerYahooApi;
import com.rv.readData.yahooApi.WillFailStockDataExecutorNew;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class YahooApiTest {
	final static Logger logger = Logger.getLogger(YahooApiTest.class);
	//@Test
	public void test1() throws IOException {
		Stock stock = YahooFinance.get("INTC");

		BigDecimal price = stock.getQuote().getPrice();
		BigDecimal change = stock.getQuote().getChangeInPercent();
		BigDecimal peg = stock.getStats().getPeg();
		BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
		stock.getStats().getPe();
		logger.info(stock.getStats());

		stock.print();
	}

//	@Test
	public void test2() throws IOException {
		Stock tesla = YahooFinance.get("TSLA", true);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.MONTH, 7);
		Interval interval = Interval.DAILY;
		List<HistoricalQuote> hist = tesla.getHistory(from, interval);
		for (HistoricalQuote his : hist) {
			System.out.println("" + his.getDate().getTime() + his.getClose());
			
		}
	}

//	@Test
	public void test3() throws IOException, InterruptedException {
		for (int i = 0; i < 100; i++) {
			Stock tesla = YahooFinance.get("TSLA", true);
			Calendar from = Calendar.getInstance();
			from.set(Calendar.MONTH, 7);
			Interval interval = Interval.DAILY;
			List<HistoricalQuote> hist = tesla.getHistory(from, interval);
			System.out.println(""+i + hist);
			Thread.sleep(100);
		}
	}
	
	//@Test
	public void test4() throws IOException, InterruptedException {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Calendar from = Calendar.getInstance();
		from.set(Calendar.MONTH, 7);
		Interval interval = Interval.DAILY;
		// Can also be done with explicit from, to and Interval parameters
		Map<String, Stock> stocks = YahooFinance.get(symbols, from, interval);
		Stock intel = stocks.get("INTC");
		Stock airbus = stocks.get("AIR.PA");
		System.out.println(intel.getHistory());
		System.out.println(airbus.getHistory());
	}
//	@Test
	public void test5() throws IOException, InterruptedException {
		String[] symbols = new String[] {"LN", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Calendar from = Calendar.getInstance();
		from.set(Calendar.MONTH, 7);
		Interval interval = Interval.DAILY;
		WillFailStockDataExecutorNew ex = new WillFailStockDataExecutorNew(new MessageCollectorSender());
		
		// Can also be done with explicit from, to and Interval parameters
		Map<String, Stock> stocks = YahooFinance.get(symbols, from, interval);
		Stock intel = stocks.get("LN");
		Stock airbus = stocks.get("AIR.PA");
		ex.executeCheck(intel);
		System.out.println(intel.getHistory());
		System.out.println(airbus.getHistory());
	}
	
	//@Test
	public void fileCleen() throws IOException{
		File inputFile = new File("nyse.txt");
		File tempFile = new File("myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String lineToRemove = ".*[-\\.].*";
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.split("	")[0].matches(lineToRemove)) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		inputFile.delete();
		boolean successful = tempFile.renameTo(inputFile);
		
		assertTrue(successful);
	}

}
