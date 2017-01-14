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
public class PaperGetData implements GetData{
  @Override
  public Map<String, Stock> getData(List<String> papers) throws IOException {

    Map<String, Stock> stocks = YahooFinance.get(
        papers.toArray(new String[] {}));
    return stocks;
  }
}
