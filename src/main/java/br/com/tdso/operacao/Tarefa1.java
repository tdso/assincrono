package br.com.tdso.operacao;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
@ApplicationScoped
public class Tarefa1 {//implements Operacao<String>{

    public CompletableFuture<String> executa() {

        return CompletableFuture.supplyAsync(()-> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Tempo Task 2 seconds";
        });
    }
}
