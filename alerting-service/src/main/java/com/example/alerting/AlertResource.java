package com.example.alerting;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alert")
public class AlertResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response triggerAlert(JsonObject input) {
        System.out.println(".....The flow has been invoked. Trying to send alert....");

        try {
            String alertMessage = input.getString("alertMessage", "Default Alert");
            String severity = input.getString("severity", "Low");
            String email = input.getString("email", "unknown@example.com");
            String phoneNumber = input.getString("phoneNumber", "0000000000");

            JsonObject payload = Json.createObjectBuilder()
                .add("processDefinitionKey", "centralAlertFlow")
                .add("variables", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                        .add("name", "alertMessage")
                        .add("value", alertMessage)
                    )
                    .add(Json.createObjectBuilder()
                        .add("name", "severity")
                        .add("value", severity)
                    )
                    .add(Json.createObjectBuilder()
                        .add("name", "email")
                        .add("value", email)
                    )
                    .add(Json.createObjectBuilder()
                        .add("name", "phoneNumber")
                        .add("value", phoneNumber)
                    )
                ).build();

            // Send POST to Flowable REST API
            URL url = new URL("https://solid-yodel-ggwpxq9qg92r6p-8081.app.github.dev/flowable-task/process-api/runtime/process-instances");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Basic " +
                java.util.Base64.getEncoder().encodeToString("admin:test".getBytes())
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.toString().getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                return Response.ok(Json.createObjectBuilder()
                    .add("status", "Process started")
                    .add("message", alertMessage)
                    .add("severity", severity)
                    .add("email", email)
                    .add("phoneNumber", phoneNumber)
                    .build()).build();
            } else {
                return Response.status(responseCode).entity("Failed to start process").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Exception occurred: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/sms")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendSmsMock(JsonObject input) {
        System.out.println("Mock: Sending SMS to " + input);
        return Response.ok("SMS triggered from Flowable!").build();
    }

    @POST
    @Path("/email")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendEmailMock(JsonObject input) {
        System.out.println(" Mock: Sending Email to " + input);
        return Response.ok("Email triggered from Flowable!").build();
    }



    @GET
    @Path("/ping")
    public String ping() {
        System.out.println("Flowable-Quarkus handshake successfull");
        return "pong";
    }

    @GET
    @Path("/end")
    public String end() {
        System.out.println("End of the alert flow!!!");
        return "end";
    }

}
