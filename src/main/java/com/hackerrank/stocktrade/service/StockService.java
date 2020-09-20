package com.hackerrank.stocktrade.service;

import java.util.List;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;

public interface StockService {
	
	public User addNewTrades(User user);
	public List<Trade> fetchallTrades();
	public List<Trade> fetchByuserId(Long Id);

}
