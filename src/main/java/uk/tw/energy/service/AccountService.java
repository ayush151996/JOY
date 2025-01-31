package uk.tw.energy.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final Map<String, String> smartMeterToPricePlanAccounts;
    // Constructor Injection.
    public AccountService(Map<String, String> smartMeterToPricePlanAccounts) {
        this.smartMeterToPricePlanAccounts = smartMeterToPricePlanAccounts;
    }

    public String getPricePlanIdForSmartMeterId(String smartMeterId) {
        return smartMeterToPricePlanAccounts.get(smartMeterId);
    }
}
//smartMeterToPricePlanAccounts has associated price plans fir different smart meterid.