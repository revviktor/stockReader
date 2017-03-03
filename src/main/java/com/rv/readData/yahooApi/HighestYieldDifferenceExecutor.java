package com.rv.readData.yahooApi;

import hello.MessageSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.rv.readData.HighestValueComparator;
import com.rv.readData.LowestValueComparator;
import com.rv.readData.ValueComparator;
import com.rv.readData.yahooApi.GetData.GetData;
import com.rv.readData.yahooApi.GetData.HistoryGetData;
import com.rv.readData.yahooApi.GetData.PaperGetData;

import org.springframework.beans.factory.annotation.Autowired;
import yahoofinance.Stock;

public class HighestYieldDifferenceExecutor implements StockDataExecutor {
	final static Logger logger = Logger.getLogger(HighestYieldDifferenceExecutor.class);

	Map<Stock,Double> changeMap;
	TreeMap<Stock, Double> sorted_map;

	private MessageSender messageSender;
	
	public HighestYieldDifferenceExecutor(MessageSender messageSender) {
		this.messageSender= messageSender;
	}

	@Override
	public void executeCheck(Stock stock) throws IOException {
		BigDecimal yield = stock.getDividend().getAnnualYieldPercent();
		if(yield!=null)
		changeMap.put(stock, yield.doubleValue());
	}

	@Override
	public void finished() {
		sorted_map.putAll(changeMap);
		writeResults(sorted_map.keySet(), changeMap);
		
	}

	@Override
	public void start() {
		changeMap = new HashMap<>();
        HighestValueComparator<Stock> bvc = new HighestValueComparator<>(changeMap);
        sorted_map = new TreeMap<Stock, Double>(bvc);
		
	}


	private void writeResults(Set<Stock> set,Map<Stock, Double> result) {
		int i=1;
		messageSender.sendMessage("The Highest yields are:");
		for(Stock key : set){
			
			messageSender.sendMessage(i+":<a target=\"_blank\" href='http://finance.yahoo.com/quote/"+key.getSymbol()+"'>"+key.getSymbol()+"</a>; yield:"+result.get(key) + ",p/e:"
			+key.getStats().getPe());
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
