package br.com.tdso.operacao;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Tarefa3 implements Callable<String> {
    private final long timeSeconds;
    public Tarefa3(long timeSeconds){
        this.timeSeconds = timeSeconds;
    }

    @Override
    public String call() throws Exception {

        System.out.println(" THREAD TASK3 - CALLABLE => " + Thread.currentThread().getName());
        System.out.println(" ");


        System.out.println("iniciando a task 3 !!");
        try {
            Thread.sleep((this.timeSeconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Tempo Task 3 " + this.timeSeconds + " seconds";
    }

    public String exec() {
        System.out.println("Metodo EXEC tarefa 3 !!!");
        System.out.println(" ");
        System.out.println(" THREAD TASK3 - ME SupplyAsync => " + Thread.currentThread().getName());
        System.out.println(" ");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Tempo Task 3 Demorado - 6 " + " seconds";
    }

//    @Override
//    public String get() {
//        System.out.println("Metodo GET tarefa 3 !!!");
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "Tempo Task 3 Demorado - 6 " + " seconds";    }
}
