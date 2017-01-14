package com.rv.readData.yahooApi;

import java.io.IOException;

import yahoofinance.Stock;

import com.rv.readData.yahooApi.GetData.GetData;

public interface StockDataExecutor {

	void executeCheck(Stock stock) throws IOException;

	void finished();

	void start();

	GetData getGetData();

	default boolean isFound(){
		return false;
	}
}
