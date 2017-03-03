package com.rv.readData.yahooApi;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.rv.readData.yahooApi.GetData.GetData;
import com.rv.readData.yahooApi.GetData.PaperGetData;

import hello.MessageSender;
import yahoofinance.Stock;

public class LowComparedTo200Executor implements StockDataExecutor {
	final static Logger logger = Logger.getLogger(LowComparedTo200Executor.class);


	private MessageSender messageSender;
	
	public LowComparedTo200Executor(MessageSender messageSender) {
		this.messageSender= messageSender;
	}

	@Override
	public void executeCheck(Stock stock) throws IOException {

		if(stock.getStats().getMarketCap().doubleValue() < 200000000){
			return;
		}

		BigDecimal avg200 = stock.getQuote().getChangeFromAvg200InPercent();
		if(avg200.intValue()<-30){
			String key = stock.getSymbol();

//			messageSender.sendMessage("I found "+stock.getName()+":");
//			messageSender.sendMessage("http://finance.yahoo.com/quote/" + key);

			messageSender.sendMessage("I found : <a target=\"_blank\" href='http://finance.yahoo.com/quote/" + key + "'>" + key
					+ "</a> " + stock.getName());
//		changeMap.put(stock.getSymbol(),avg200.doubleValue());
		}
	}

	@Override
	public void finished() {
//		sorted_map.putAll(changeMap);
//		writeResults(sorted_map.keySet(), changeMap);
		
	}

	@Override
	public void start() {
//		changeMap = new HashMap<>();
//        LowestValueComparator bvc = new LowestValueComparator(changeMap);
//        sorted_map = new TreeMap<String, Double>(bvc);
		
	}




	@Override
	public GetData getGetData() {
		return new PaperGetData();
	}
}
