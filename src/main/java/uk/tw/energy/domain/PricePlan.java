package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class PricePlan {

    private final String energySupplier;
    private final String planName;
    private final BigDecimal unitRate; // unit price per kWh
    private final List<PeakTimeMultiplier> peakTimeMultipliers;

    public PricePlan( String planName, String energySupplier, BigDecimal unitRate, List<PeakTimeMultiplier> peakTimeMultipliers) {
        this.planName = planName;
        this.energySupplier = energySupplier;
        this.unitRate = unitRate;
        this.peakTimeMultipliers = peakTimeMultipliers;
    }

    public String getEnergySupplier() { //getting energy supplier.
        return energySupplier;
    }

    public String getPlanName() { //getting plan name 
        return planName;
    }

    public BigDecimal getUnitRate() { //getting unit rate given in constructor
        return unitRate;
    }

    public BigDecimal getPrice(LocalDateTime dateTime) {   //gets price by filtering peaktimemultiplier elements with similar dayofweeks and than multiplying unit rate with myultiplier of the peaktimemultiplier elrment else Unit Rate would be applied.
        return peakTimeMultipliers.stream()
                .filter(multiplier -> multiplier.dayOfWeek.equals(dateTime.getDayOfWeek()))
                .findFirst()
                .map(multiplier -> unitRate.multiply(multiplier.multiplier))
                .orElse(unitRate);
    }

    static class PeakTimeMultiplier {//static so that would not be accessible by outside class without static reference.

        DayOfWeek dayOfWeek;
        BigDecimal multiplier;

        public PeakTimeMultiplier(DayOfWeek dayOfWeek, BigDecimal multiplier) {
            this.dayOfWeek = dayOfWeek;
            this.multiplier = multiplier;
        }
    }
}
