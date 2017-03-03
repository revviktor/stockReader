package com.rv.readData.yahooApi;

import hello.MessageSender;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import yahoofinance.Stock;

import com.rv.readData.StockChecker;

@Component
public class StockCheckerYahooApi implements StockChecker {
	final static Logger logger = Logger.getLogger(StockCheckerYahooApi.class);
	StockDataExecutor executor;
	MessageSender messageSender;

	public StockCheckerYahooApi(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@Override
	public boolean compareStocks(List<String> papers, String logic)
			throws IOException {
		switch (logic) {
		case "willfail": {
			executor = new WillFailStockDataExecutorNew(messageSender);
			break;
		}
		case "lowestpe": {
			executor = new LowestPEExecutor(messageSender);
			break;
		}
		case "Pbmulpe": {
			executor = new PperBookMulPeExecutor(messageSender);
			break;
		}
		case "low200": {
			executor = new LowComparedTo200Executor(messageSender);
			break;
		}
		case "highyield": {
			executor = new HighestYieldDifferenceExecutor(messageSender);
			break;
		}
		}

		executor.start();
		messageSender.sendMessage("reading starts");
		for (int i = 0; i < papers.size(); i += 50) {
			int end = i + 50 > papers.size() ? papers.size() : i + 50;
			List<String> proc = papers.subList(i, end);
			try {
				Map<String, Stock> stocks = executor.getGetData().getData(proc);
				processData(stocks);
			} catch (Exception e) {
				for (String pap : proc) {
					try {
						Map<String, Stock> stocks = executor.getGetData()
								.getData(Collections.singletonList(pap));
						processData(stocks);
					} catch (Exception e2) {
						System.out.println("Error processing:" + pap
								+ e2.getLocalizedMessage());

					}
				}
			}
		}
		executor.finished();
		messageSender.sendMessage("reading done");
		return executor.isFound();
	}

	private void processData(Map<String, Stock> stocks) {
		for (String key : stocks.keySet()) {
			try {
				Stock stock = stocks.get(key);
				executor.executeCheck(stock);
			} catch (Exception e) {
				System.out.println("Error processing stock:" + key
						+ ". Reason:" + e.getLocalizedMessage());
			}
		}

	}

}
