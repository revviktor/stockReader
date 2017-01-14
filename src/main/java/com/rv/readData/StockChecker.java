package com.rv.readData;

import java.io.IOException;
import java.util.List;

public interface StockChecker {

	public abstract boolean compareStocks(List<String> papers, String logic) throws IOException;

}