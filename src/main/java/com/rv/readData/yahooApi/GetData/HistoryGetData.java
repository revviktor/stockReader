/*
 * Copyright (c) 2016 GE. All Rights Reserved.
 * GE Confidential: Restricted Internal Distribution
 */
package com.rv.readData.yahooApi.GetData;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

@Component
public class HistoryGetData implements GetData{
  @Override
  public Map<String, Stock> getData(List<String> papers) throws IOException {

    Calendar from = Calendar.getInstance();
    from.add(Calendar.MONTH, -2);// ...set(Calendar.MONTH, 7);
    // from.set(Calendar.MONTH, Calendar.JUNE);
    // from.set(Calendar.DAY_OF_MONTH, 15);

    Calendar to = Calendar.getInstance();
    // to.set(Calendar.MONTH, Calendar.SEPTEMBER);
    // to.set(Calendar.DAY_OF_MONTH, 7);

    Interval interval = Interval.DAILY;
    // Can also be done with explicit from, to and Interval parameters
    Map<String, Stock> stocks = YahooFinance.get(
        papers.toArray(new String[] {}), from, to, interval);
    return stocks;
  }
}
