package uk.tw.energy.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.service.MeterReadingService;

@RestController
@RequestMapping("/readings")
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @PostMapping("/store")
    public ResponseEntity storeReadings(@RequestBody MeterReadings meterReadings) { //meterReadings is a Record.
        if (!isMeterReadingsValid(meterReadings)) {// Here, it was proposed to include condition, if (meterReadings.getElectricityReadings() == null || meterReadings.getElectricityReadings().isEmpty()) {
 //           throw new IllegalArgumentException("Electricity readings cannot be null or empty.");
   //     }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        meterReadingService.storeReadings(meterReadings.smartMeterId(), meterReadings.electricityReadings()); //uses meterAssociatedReadings map in meter reading service to store.
        return ResponseEntity.ok().build();
    }

    private boolean isMeterReadingsValid(MeterReadings meterReadings) {
        String smartMeterId = meterReadings.smartMeterId();
        List<ElectricityReading> electricityReadings = meterReadings.electricityReadings();
        return smartMeterId != null
                && !smartMeterId.isEmpty()
                && electricityReadings != null
                && !electricityReadings.isEmpty();
    }

    @GetMapping("/read/{smartMeterId}")
    public ResponseEntity readReadings(@PathVariable String smartMeterId) { //get smartmeter reading from meterAssociatedReadings map.
        Optional<List<ElectricityReading>> readings = meterReadingService.getReadings(smartMeterId);
        return readings.isPresent()
                ? ResponseEntity.ok(readings.get())
                : ResponseEntity.notFound().build();
    }

    // ResponseEntity is a generic class used to represent the HTTP response in a more flexible and explicit way.
    // It provides control over the response body, HTTP status code, and HTTP headers, making it ideal for creating
    // RESTful APIs.
}



//We store MeterReadings in a map meterAssociatedReadings. To read via smartmeterid, we need value from meterAssociatedReadings map of key smartmeterid.
//smartmeterid map stores readings list for a smartmeter id.