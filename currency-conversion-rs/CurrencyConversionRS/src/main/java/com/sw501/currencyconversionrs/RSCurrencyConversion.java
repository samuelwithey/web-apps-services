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
        this.currency_rates.put("GBP", 1.0);
        this.currency_rates.put("EUR", 1.14);
        this.currency_rates.put("USD", 1.24);
    }
    
    @GET
    @Path("{currency1}/{currency2}/{amount_of_currency}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response CurrencyConversion(@PathParam("currency1") String currency1, @PathParam("currency2") String currency2, @PathParam("amount_of_currency") double amount_of_currency) {
        return Response.ok(convert(currency_rates.get(currency1), currency_rates.get(currency2), amount_of_currency)).build();
    }
    
    public double convert(double currency_rate1, double currency_rate2, double amount) {
        return (amount / currency_rate1) * currency_rate2;
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

