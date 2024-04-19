package br.com.tdso.service;

import br.com.tdso.operacao.Tarefa3;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@ApplicationScoped
public class ServiceManagedExecutor {
    @Inject
    ManagedExecutor executor;

    public String executa(long seconds){

        System.out.println(" THREAD SERVICE MANAGED EXECUTOR => " + Thread.currentThread().getName());
        System.out.println(" ");

        Tarefa3 tarefa3 = new Tarefa3(seconds);
        System.out.println("chamando a tarefa 3");
        Future<String> future = executor.submit(tarefa3);
        System.out.println("Tarefa 3 submetida !!");
        System.out.println("aCIONANDO O GET PARA OBTER O RESULTADO !!");
        String retorno = null;
        try {
            retorno = future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Erro chamada GET para obter o retorno !!");
            throw new RuntimeException(e);
        }
        System.out.println("GET desbloqueou !!!");
        return retorno;
    }
    public String executaME(long seconds){

        System.out.println(" THREAD SERVICE MANAGED EXECUTOR => " + Thread.currentThread().getName());
        System.out.println(" ");

        Tarefa3 tarefa3 = new Tarefa3(seconds);
        System.out.println("chamando a tarefa 3");
        //Future<String> future = executor.submit(tarefa3);
        CompletableFuture<Object> future = executor.supplyAsync(()->tarefa3.exec());

        System.out.println("Tarefa 3 submetida !!");
        System.out.println("aCIONANDO O GET PARA OBTER O RESULTADO !!");
        String retorno = null;
        try {
            retorno = (String) future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Erro chamada GET para obter o retorno !!");
            throw new RuntimeException(e);
        }
        System.out.println("GET desbloqueou !!!");
        return retorno;
    }
}
