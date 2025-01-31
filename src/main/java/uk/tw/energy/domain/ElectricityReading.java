package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @param reading kW
 */

// Encapsulation and Information Hiding approach - Getter and setter methods in models
public record ElectricityReading(Instant time, BigDecimal reading) {}
