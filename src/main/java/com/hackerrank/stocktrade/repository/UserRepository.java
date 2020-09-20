package com.hackerrank.stocktrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Trade save(Trade trade);
	
}
