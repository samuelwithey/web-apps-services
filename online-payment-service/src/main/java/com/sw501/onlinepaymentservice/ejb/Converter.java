package com.sw501.onlinepaymentservice.ejb;

import com.sw501.onlinepaymentservice.entity.CurrencyType;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Singleton
public class Converter {
    
    private Client client = ClientBuilder.newClient();
    
    public double currencyConversion(CurrencyType currency1, CurrencyType currency2, double amount) {
         WebTarget myResource = client.target("http://localhost:10000/CurrencyConversionRS/conversion")
                 .path("{currency1}").resolveTemplate("currency1", currency1.name())
                 .path("{currency2}").resolveTemplate("currency2", currency2.name())
                 .path("{amount_of_currency}").resolveTemplate("amount_of_currency", amount);
        String response = myResource.request(MediaType.APPLICATION_JSON).get(String.class);
        return Double.parseDouble(response);
    }
}
