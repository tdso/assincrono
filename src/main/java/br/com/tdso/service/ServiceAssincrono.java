package br.com.tdso.service;

import br.com.tdso.operacao.Tarefa1;
import br.com.tdso.operacao.Tarefa2;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@ApplicationScoped
public class ServiceAssincrono {


    @Inject
    Tarefa1 tarefa1;
    @Inject
    Tarefa2 tarefa2;
    public String consultaOperacoes() {

        System.out.println(" THREAD SERVICE CF => " + Thread.currentThread().getName());
        System.out.println(" ");

        System.out.println("Processors = " + Runtime.getRuntime()
                .availableProcessors());

        LocalTime start = LocalTime.now();

        //CompletableFuture<String> task1Assincrono = managedExecutor.supplyAsync(() -> tarefa1.executa());
        CompletableFuture<String> task2Assincrono = tarefa2.executa();

//        task1Assincrono.handle((dado, err) ->{
//            if (err == null){
//                System.out.println("Handle TASK1 sem Erro > " + dado);
//            } else {
//                System.out.println("Handle TASK1 com Erro > " + err.getMessage() + err.getCause());
//            }
//            return "";
//        });
//        task2Assincrono.handle((dado, err) ->{
//           if (err == null){
//               System.out.println("Handle TASK2 sem Erro > " + dado);
//           } else {
//               System.out.println("Handle TASK2 com Erro > " + err.getMessage() + err.getCause());
//           }
//           return "";
//        });

        //task1Assincrono.thenAccept(System.out::println);
        task2Assincrono.thenAccept(System.out::println);

        try {
            System.out.println(task2Assincrono.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        LocalTime end = LocalTime.now();
        System.out.println("DESBLOQUEOU >> Tempo Decorrido: " + Duration.between(start, end));


//        try {
//            TimeUnit.SECONDS.wait(6);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return "teste";
    }

}
