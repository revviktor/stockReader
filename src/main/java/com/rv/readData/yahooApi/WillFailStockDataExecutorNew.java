package com.rv.readData.yahooApi;

import hello.MessageSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import com.rv.readData.yahooApi.GetData.GetData;
import com.rv.readData.yahooApi.GetData.HistoryGetDataNew;
import com.sun.mail.imap.protocol.Item;

public class WillFailStockDataExecutorNew implements StockDataExecutor {
	final static Logger logger = Logger.getLogger(WillFailStockDataExecutorNew.class);

	MessageSender messageSender;

	private boolean found;

	public WillFailStockDataExecutorNew(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@Override
	public void executeCheck(Stock stock) throws IOException {
		if (stock.getStats().getMarketCap().doubleValue() < 200000000) {
			return;
		}
//		List<HistoricalQuote> history = stock.getHistory();
//		LinkedList<HistoricalQuote> l = new LinkedList<>();
//		l.addAll(history);
//		boolean inlocalPeek = checkLocalPeek(l);
		boolean inlocalPeek = checkLocalPeek(stock);

		if (inlocalPeek) {
			String key = stock.getSymbol();
			// messageSender.sendMessage("I found : <a target=\"_blank\"
			// href='http://finance.yahoo.com/quote/" + key + "'>" + key
			// + "</a> " + stock.getName());
			messageSender.sendMessage("I found " + stock.getName() + ":");
			messageSender.sendMessage("http://finance.yahoo.com/quote/" + key);
			messageSender.sendMessage("https://www.bloomberg.com/quote/" + key + ":US");
			setFound(true);
		}
	}

	@Override
	public void finished() {

	}

	@Override
	public void start() {

	}

	public GetData getGetData() {
		return new HistoryGetDataNew();
	}

	private boolean checkLocalPeek(Stock history) {
		if(history.getQuote().getChangeFromAvg50InPercent().doubleValue() >100) return true;
		return false;
	}

	private boolean checkLocalPeek(LinkedList<HistoricalQuote> history) {

		HistoricalQuote last = history.poll();
		BigDecimal check = last.getHigh();
		while (history.size() > 0) {
			HistoricalQuote item = history.poll();
			if (item.getClose().doubleValue() * 2 < check.doubleValue()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

}
