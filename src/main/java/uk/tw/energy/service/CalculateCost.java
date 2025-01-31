package uk.tw.energy.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.service.MeterReadingService;

import org.springframework.stereotype.Service;

import uk.tw.energy.domain.ElectricityReading;

@Service
public class CalculateCost {
	
	   private final MeterReadingService meterReadingService;

	    public CalculateCost(MeterReadingService meterReadingService) {
	        this.meterReadingService = meterReadingService;
	    }



		
	}
	
	   
	
	
	
	
	

