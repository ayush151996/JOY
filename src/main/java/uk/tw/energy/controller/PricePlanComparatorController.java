package uk.tw.energy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;

@RestController
@RequestMapping("/price-plans")
public class PricePlanComparatorController {

    public static final String PRICE_PLAN_ID_KEY = "pricePlanId";
    public static final String PRICE_PLAN_COMPARISONS_KEY = "pricePlanComparisons";
    private final PricePlanService pricePlanService;
    private final AccountService accountService;
    // Constructor Injection
    public PricePlanComparatorController(PricePlanService pricePlanService, AccountService accountService) {
        this.pricePlanService = pricePlanService;
        this.accountService = accountService;
    }

    @GetMapping("/compare-all/{smartMeterId}") // after getting pricePlan key from Meter Reading Service and priceplanid from smartMeterToPricePlanAccounts Map than stores in price plan comparision map. 
    public ResponseEntity<Map<String, Object>> calculatedCostForEachPricePlan(@PathVariable String smartMeterId) {
        String pricePlanId = accountService.getPricePlanIdForSmartMeterId(smartMeterId); //Get price by smartMeterToPricePlanAccounts Map.
        Optional<Map<String, BigDecimal>> consumptionsForPricePlans = pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(smartMeterId);//get consumptionforpriceplans from map smartMeterToPricePlanAccounts of Meter Reading Service.

        if (!consumptionsForPricePlans.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> pricePlanComparisons = new HashMap<>();  //than storing the value in pricePlanComparisons.
        pricePlanComparisons.put(PRICE_PLAN_ID_KEY, pricePlanId);
        pricePlanComparisons.put(PRICE_PLAN_COMPARISONS_KEY, consumptionsForPricePlans.get());

        return consumptionsForPricePlans.isPresent()
                ? ResponseEntity.ok(pricePlanComparisons)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/recommend/{smartMeterId}") //Recommends cheapest price plan - queries smartMeterToPricePlanAccounts to get map from meterAssociatedReadings Map of MeterReading Service. Taking this as value and planid as key returns map. Than storing the entries as list and sorts it upto limit to get recommendations.
    public ResponseEntity<List<Map.Entry<String, BigDecimal>>> recommendCheapestPricePlans(
            @PathVariable String smartMeterId, @RequestParam(value = "limit", required = false) Integer limit) {
        Optional<Map<String, BigDecimal>> consumptionsForPricePlans = pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(smartMeterId);
        // BigDecimal is a class in Java used for representing arbitrary-precision decimal numbers.
        if (!consumptionsForPricePlans.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Map.Entry<String, BigDecimal>> recommendations = new ArrayList<>(consumptionsForPricePlans.get().entrySet());
        recommendations.sort(Comparator.comparing(Map.Entry::getValue)); // sorting value of hashmap based on value.

        if (limit != null && limit < recommendations.size()) { //If limit is not null and small than limit given than create sublist of o, limit and set it as recommendations. 
            recommendations = recommendations.subList(0, limit); // subList creates sublist from start to end.
        }

        return ResponseEntity.ok(recommendations);
    }
}
