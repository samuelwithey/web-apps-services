package com.sw501.onlinepaymentservice.entity;

public enum CurrencyType {
    GB_POUNDS("£"), 
    US_DOLLARS("$"), 
    EURO("€");
    
    public final String label;
    
    private CurrencyType(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    @Override
    public String toString() {
        return label;
    }
}
