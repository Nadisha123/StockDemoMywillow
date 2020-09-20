package com.hackerrank.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;
import com.hackerrank.stocktrade.repository.TradeRepository;
import com.hackerrank.stocktrade.repository.UserRepository;
import com.hackerrank.stocktrade.service.StockService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StockTradeApiRestController {

	@Autowired
	UserRepository userrepository;

	@Autowired
	TradeRepository traderepository;

	@Autowired
	StockService stockservice;

	public StockTradeApiRestController() {
		ArrayList<String> trades;
		ArrayList<String> users;
	}

	@DeleteMapping("/erase")
	ResponseEntity<Trade> erase() {
		traderepository.deleteAll();
		// trades.clear();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/trades")
	public ResponseEntity<User> addNewTrades(@RequestBody Trade trades) {

		List<Trade> tradeList = new ArrayList<>();
		Trade trade = new Trade();
		User user = new User();
		trade.setId(trades.getId());
		trade.setStockPrice(trades.getStockPrice());
		trade.setStockQuantity(trades.getStockQuantity());
		trade.setStockSymbol(trades.getStockSymbol());
		trade.setTradeTimestamp(trades.getTradeTimestamp());
		trade.setType(trades.getType());
		tradeList.add(trade);
		user.setId(trades.getUser().getId());
		user.setName(trades.getUser().getName());
		user.setTrade(tradeList);
		trade.setUser(trades.getUser());
		Long tradeId = trades.getId();
		if (userrepository.exists(tradeId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			stockservice.addNewTrades(user);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}

	}

	@GetMapping("/trades")
	public List<Trade> fetchallTrades() {
		return stockservice.fetchallTrades();

	}

	@GetMapping("/trades/users/{Id}")
	public ResponseEntity<List<Trade>> tradesById(@PathVariable("Id") Long Id) {
		List<Trade> tradeList = new ArrayList<>();
		if (userrepository.exists(Id)) {
			tradeList = stockservice.fetchByuserId(Id);
			return new ResponseEntity<List<Trade>>(tradeList, HttpStatus.OK);

		} else {
			return new ResponseEntity<List<Trade>>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/stocks/{stockSymbol}/price")
	public ResponseEntity<Object> fetchStock(@PathVariable("stockSymbol") String stockSymbol,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime startdate = LocalDateTime.from(formatter.parse(startDate));
		LocalDateTime enddate = LocalDateTime.from(formatter.parse(endDate));
		Timestamp timestampstartDate = Timestamp.valueOf(startdate);
		Timestamp timestampendDate = Timestamp.valueOf(enddate);
		Map<String, Object> stockMap = new HashMap<>();
		if (!traderepository.existsBystockSymbol(stockSymbol)) {
			stockMap.put("message", "There are no trades in the given date range");
			return new ResponseEntity<>(stockMap,HttpStatus.NOT_FOUND);
		} else {
			List<Trade> stock = traderepository.findBytradeTimestampBetween(timestampstartDate, timestampendDate);
			Trade min = stock.stream().min(Comparator.comparing(Trade::getStockPrice)).get();
			Trade max = stock.stream().max(Comparator.comparing(Trade::getStockPrice)).get();
			stockMap.put("symbol", stockSymbol);
			stockMap.put("highest", max.getStockPrice());
			stockMap.put("lowest", min.getStockPrice());

			return new ResponseEntity<>(stockMap, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/stocks/stats")
	public ResponseEntity<List<Trade>> fetchStockdata(
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		Map<String,Object> tradeMap = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime startdate = LocalDateTime.from(formatter.parse(startDate));
		LocalDateTime enddate = LocalDateTime.from(formatter.parse(endDate));
		Timestamp timestampstartDate = Timestamp.valueOf(startdate);
		Timestamp timestampendDate = Timestamp.valueOf(enddate);
		
		List<Trade> stock = traderepository.findBytradeTimestampBetween(timestampstartDate, timestampendDate);
        List<Trade> sortedStock = stock.stream().sorted(Comparator.comparing(Trade::getStockSymbol)).collect(Collectors.toList());
        List<Trade> newStock= sortedStock.stream().sorted(Comparator.comparing(Trade::getStockPrice)).collect(Collectors.toList());
      
        
	return new ResponseEntity<List<Trade>>(newStock,HttpStatus.OK);
		
	}
}
