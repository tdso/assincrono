package br.com.tdso.rest;

import br.com.tdso.service.ServiceAssincrono;
import br.com.tdso.service.ServiceManagedExecutor;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.time.LocalTime;

@Path("/api")
public class Rest {

    @Inject
    ServiceAssincrono service;
    @Inject
    ServiceManagedExecutor serviceManagedExecutor;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAssincrono(){
        System.out.println("################################################################################");


        System.out.println(" THREAD REST => " + Thread.currentThread().getName());
        System.out.println(" ");

       LocalTime start = LocalTime.now();

       String retorno = service.consultaOperacoes();
       LocalTime end = LocalTime.now();
       System.out.println("REST >> Tempo Decorrido: " + Duration.between(start, end));
       return "Tempo Decorrido: " + Duration.between(start, end);

    }
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/me")
    public String getManagedExecutor(){
        System.out.println("################################################################################");

        System.out.println(" THREAD REST => " + Thread.currentThread().getName());
        System.out.println(" ");

        LocalTime start = LocalTime.now();
        String retorno = serviceManagedExecutor.executa(3L);
        LocalTime end = LocalTime.now();
        System.out.println("REST >> Tempo Decorrido: " + Duration.between(start, end));
        System.out.println("################################################################################");
        return retorno + " >> Tempo Decorrido: " + Duration.between(start, end);
    }
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/me/async")
    public String getMESupplyAsync(){
        System.out.println("################################################################################");

        System.out.println(" THREAD REST ASYNC => " + Thread.currentThread().getName());
        System.out.println(" ");

        LocalTime start = LocalTime.now();
        String retorno = serviceManagedExecutor.executaME(3L);
        LocalTime end = LocalTime.now();
        System.out.println("REST >> Tempo Decorrido: " + Duration.between(start, end));
        System.out.println("################################################################################");
        return retorno + " >> Tempo Decorrido: " + Duration.between(start, end);
    }
}
