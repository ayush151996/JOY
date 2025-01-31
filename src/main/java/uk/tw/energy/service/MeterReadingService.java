package uk.tw.energy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;

@Service
public class MeterReadingService {

    private final Map<String, List<ElectricityReading>> meterAssociatedReadings;//suggested for ConcurrentHashMap
    
    private static final Logger logger = LoggerFactory.getLogger(MeterReadingService.class);// Other enhancements possible for logger.

    public MeterReadingService(Map<String, List<ElectricityReading>> meterAssociatedReadings) {
    	logger.info("Inside MeterReadingService.");
        this.meterAssociatedReadings =  meterAssociatedReadings; // than we will have to cast if using concurrenthashmap, (ConcurrentHashMap<String, List<ElectricityReading>>)
    }

    // Optional - may or may not contain non null value.

    public Optional<List<ElectricityReading>> getReadings(String smartMeterId) {
    	logger.info("Inside getReadings");
        return Optional.ofNullable(meterAssociatedReadings.get(smartMeterId)); //creates an optional if meterAssociatedReadings.get(smartMeterId) is not null.
    }

    public void storeReadings(String smartMeterId, List<ElectricityReading> electricityReadings) {
    	logger.info("storeReadings");
        if (!meterAssociatedReadings.containsKey(smartMeterId)) {
            meterAssociatedReadings.put(smartMeterId, new ArrayList<>());
        }
        meterAssociatedReadings.get(smartMeterId).addAll(electricityReadings);
    }
}
