package com.rv.readData.yahooApi.GetData;/*
 * Copyright (c) 2016 GE. All Rights Reserved.
 * GE Confidential: Restricted Internal Distribution
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;

public interface GetData {
  Map<String, Stock>  getData(List<String> proc) throws IOException;
}
