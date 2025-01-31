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


	public BigDecimal calculatecostpastsevendays(String smartmeterid) {
		
		BigDecimal d = null;
		List<ElectricityReading> ElectricityReadingpasstseven = new ArrayList<>(); 
		List<ElectricityReading> ElectricityReading = new ArrayList<>(); 
		
		Optional<List<ElectricityReading>> readings = meterReadingService.getReadings(smartmeterid));
		 
		ElectricityReadingpasstseven=readings.get().stream().filter(/*past 7 days logic*/).collect(Collectors.toList());
		
		
	
		return null;
		
	}
	
	   
	
	
	
	
	
}
/*As an electricity consumer, I want to be able to view my usage cost of the last week so that I can monitor my spending
Acceptance Criteria:
- Given I have a smart meter ID with price plan attached to it and usage data stored, when I request the usage cost then I am shown the correct cost of last week's usage
- Given I have a smart meter ID without a price plan attached to it and usage data stored, when I request the usage cost then an error message is displayed
How to calculate usage cost
- Unit of meter readings : kW (Kilowatt)
- Unit of Time : Hour (h)
- Unit of Energy Consumed : kW x Hour = kWh
- Unit of Tariff : $ per kWh (ex 0.2 $ per kWh)
To calculate the usage cost for a duration (D) in which lets assume we have captured N electricity readings (er1,er2,er3....erN)
Average reading in KW = (er1.reading + er2.reading + ..... erN.Reading)/N
Usage time in hours = Duration(D) in hours
Energy consumed in kWh = average reading x usage time
Cost = tariff unit prices x energy consumed
*/
// --Use logs, concurrent hash map for better performances.

		//Take Electricity Readings from Meter AssociateReading map past 7 daYS FROM NOw and store in list.
		List<ElectricityReading> 
		
		//INITIATE DEfault meter to 0. Add meter readings of each Meter Readings of object as filtered above.
		//Take size of above list and take average by total sum/size.
		
		//To calculate duration, take instant of each meter readings and check another meter reading with same date - parsing required.
		//total energy consumed formula.
