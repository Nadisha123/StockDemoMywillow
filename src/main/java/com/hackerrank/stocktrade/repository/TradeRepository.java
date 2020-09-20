package com.hackerrank.stocktrade.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackerrank.stocktrade.model.Trade;

public interface TradeRepository extends JpaRepository<Trade,Long>{
	
	List<Trade> findAllByOrderByIdAsc();
	List<Trade> findByUserIdOrderByIdAsc(Long Id);
	boolean existsBystockSymbol(String stockSymbol);
	List<Trade> findBytradeTimestampBetween(Timestamp TimestampStartDate,Timestamp TimestampEndDate);
	List<Trade> findBytradeTimestamp(Timestamp TimestampDate);
}
