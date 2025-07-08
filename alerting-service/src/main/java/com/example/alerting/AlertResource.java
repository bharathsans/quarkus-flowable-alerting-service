package com.example.alerting;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/alert")
public class AlertResource {

    @GET
    public String hello() {
        return "Alerting service is running!";
    }
}
