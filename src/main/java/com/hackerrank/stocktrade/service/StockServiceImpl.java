package com.hackerrank.stocktrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;
import com.hackerrank.stocktrade.repository.TradeRepository;
import com.hackerrank.stocktrade.repository.UserRepository;
@Service
public class StockServiceImpl implements StockService{

	
	@Autowired
	TradeRepository traderepository;
	
	@Autowired
	UserRepository userrepository;
	
	@Override
	public User addNewTrades(User user) {
		return userrepository.save(user);
	}

	@Override
	public List<Trade> fetchallTrades() {
		// TODO Auto-generated method stub
		return traderepository.findAllByOrderByIdAsc();
	}

	@Override
	public List<Trade> fetchByuserId(Long Id) {
		// TODO Auto-generated method stub
return traderepository.findByUserIdOrderByIdAsc(Id);
	}

}
