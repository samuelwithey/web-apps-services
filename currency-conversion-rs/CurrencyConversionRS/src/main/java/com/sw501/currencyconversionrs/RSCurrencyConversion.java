package com.sw501.currencyconversionrs;

import java.util.Hashtable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton

@Path("/conversion")
public class RSCurrencyConversion {
    
    private Hashtable<String, Double> currency_rates = new Hashtable();

    public RSCurrencyConversion() {
        this.currency_rates.put("GBP_to_USD_rate", 1.24);
        this.currency_rates.put("GBP_to_EUR_rate", 1.14);
        this.currency_rates.put("USD_to_GBP_rate", 0.81);
        this.currency_rates.put("USD_to_EUR_rate", 0.91);
        this.currency_rates.put("EUR_to_GBP_rate", 0.88);
        this.currency_rates.put("EUR_to_USD_rate", 1.09);
    }
    
    @GET
    @Path("{currency1}/{currency2}/{amount_of_currency}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response CurrencyConversion(@PathParam("currency1") String currency1, @PathParam("currency2") String currency2, @PathParam("amount_of_currency") double amount_of_currency) {
        String conversion_rate = currency1 + "_to_" + currency2 + "_rate";
        if (currency_rates.containsKey(conversion_rate)) {
            double conversion_amount = amount_of_currency * currency_rates.get(conversion_rate);
            return Response.ok(conversion_amount).build();
        } else {
            throw new NotFoundException();
        }
    }
    
    @PostConstruct
    public void init() {
        System.out.println("Singleton Object for this RESTfull Web Service has been created!");
    }

    @PreDestroy
    public void clean() {
        System.out.println("Singleton Object for this RESTfull Web Service has been cleaned!");
    }
}

