package com.rv.readData.yahooApi;

import hello.MessageSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.rv.readData.LowestValueComparator;
import com.rv.readData.ValueComparator;
import com.rv.readData.yahooApi.GetData.GetData;
import com.rv.readData.yahooApi.GetData.HistoryGetData;
import com.rv.readData.yahooApi.GetData.PaperGetData;

import org.springframework.beans.factory.annotation.Autowired;
import yahoofinance.Stock;

public class PperBookMulPeExecutor implements StockDataExecutor {
	final static Logger logger = Logger.getLogger(PperBookMulPeExecutor.class);

	Map<String,Double> changeMap;
	TreeMap<String, Double> sorted_map;

	private MessageSender messageSender;
	
	public PperBookMulPeExecutor(MessageSender messageSender) {
		this.messageSender= messageSender;
	}

	@Override
	public void executeCheck(Stock stock) throws IOException {
		BigDecimal pe = stock.getStats().getPe();
		double pb = stock.getQuote().getPreviousClose().doubleValue() / stock.getStats().getBookValuePerShare().doubleValue();
		if(pe!=null && pe.doubleValue()>0)
		changeMap.put(stock.getSymbol(), pe.doubleValue()* pb);
	}

	@Override
	public void finished() {
		sorted_map.putAll(changeMap);
		writeResults(sorted_map.keySet(), changeMap);
		
	}

	@Override
	public void start() {
		changeMap = new HashMap<>();
        LowestValueComparator bvc = new LowestValueComparator(changeMap);
        sorted_map = new TreeMap<String, Double>(bvc);
		
	}


	private void writeResults(Set<String> set,Map<String, Double> result) {
		int i=1;
		messageSender.sendMessage("The Lowest P/E -sare:");
		for(String key : set){
			
			messageSender.sendMessage(i+":<a target=\"_blank\" href='http://finance.yahoo.com/quote/"+key+"'>"+key+"</a>; change:"+result.get(key));
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
      i++;
			if(i>100){
				break;
			}
		}
		
	}

	@Override
	public GetData getGetData() {
		return new PaperGetData();
	}
}
