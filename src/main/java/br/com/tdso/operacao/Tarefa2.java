package br.com.tdso.operacao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Tarefa2 { //implements Operacao<String>{

     //@Override
    public CompletableFuture<String> executa() {

        System.out.println(" THREAD TAREFA 2 ANTES CHAMAR SUPPLY ASYNC => " + Thread.currentThread().getName());
        System.out.println(" ");

        return CompletableFuture.supplyAsync(()->{

            System.out.println(" THREAD TASK2 CF => " + Thread.currentThread().getName());
            System.out.println(" ");

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            try {
//                TimeUnit.SECONDS.wait(4);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            return "Tempo Task 4 seconds";

        });

    }
}
