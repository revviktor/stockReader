package com.rv.readData.yahooApi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import hello.MessageSender;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import com.rv.readData.yahooApi.GetData.GetData;
import com.rv.readData.yahooApi.GetData.HistoryGetData;

public class WillFailStockDataExecutor implements StockDataExecutor {
	final static Logger logger = Logger.getLogger(WillFailStockDataExecutor.class);

	private static final double FIRST_PHASE_BOOM = 1.3;

	MessageSender messageSender;
	public WillFailStockDataExecutor(MessageSender messageSender) {
		this.messageSender =  messageSender;
	}

	@Override
	public void executeCheck(Stock stock) throws IOException {
		List<HistoricalQuote> history = stock.getHistory();
		LinkedList<HistoricalQuote> l = new LinkedList<>();
		l.addAll(history);
		HistoricalQuote current = history.get(0);
		boolean inlocalPeek = checkLocalPeek(l);

		if (inlocalPeek) {
			String key = stock.getSymbol();
			messageSender.sendMessage("I found : <a target=\"_blank\" href='http://finance.yahoo.com/quote/" + key + "'>" + key
					+ "</a> " + stock.getName());
			boolean wasHigher = wasHigher(l, current.getClose());
			if (wasHigher) {
				messageSender.sendMessage("II found : "+stock.getSymbol()+" " + stock.getName());
				if (madeGreatBoom(l)) {
					messageSender.sendMessage("Total Match : "+stock.getSymbol()+" " + stock.getName());
				}
			}

		}
		// for (HistoricalQuote his : history) {
		// System.out.println(his.getSymbol()+his.getDate().getTime() +
		// his.getClose());

		// }
	}

	@Override
	public void finished() {

	}

	@Override
	public void start() {

	}
@Autowired
	HistoryGetData getData;
	@Override
	public GetData getGetData() {
		//TODO
		return new HistoryGetData();
	}

	private boolean madeGreatBoom(LinkedList<HistoricalQuote> history) {
		BigDecimal reference = history.poll().getClose();
		int i = 0;
		BigDecimal lastChecked = history.poll().getClose();
		while (i < 10 && history.size() > 0) {
			i++;
			lastChecked = history.poll().getClose();
		}
		return lastChecked.doubleValue() * FIRST_PHASE_BOOM < reference.doubleValue();
	}

	private boolean wasHigher(LinkedList<HistoricalQuote> history,
			BigDecimal localPeek) {
		while (history.size() > 0) {

			BigDecimal current = history.peek().getClose();
			if (current.doubleValue() > localPeek.doubleValue()) {
				return true;
			}
			history.poll();

		}
		return false;
	}

	private boolean checkLocalPeek(LinkedList<HistoricalQuote> history) {

		HistoricalQuote last = history.poll();
		BigDecimal lastClose = last.getClose();
		BigDecimal previous = history.poll().getClose(), current = history
				.poll().getClose(), beforePrevious = lastClose;
		while (history.size() > 0) {
			if (current.doubleValue() > beforePrevious.doubleValue()
					&& current.doubleValue() > previous.doubleValue()) {
				break;
			}
			beforePrevious = previous;
			previous = current;
			current = history.poll().getClose();
		}
		return current.doubleValue() * 1.2 < lastClose.doubleValue();
	}

}
