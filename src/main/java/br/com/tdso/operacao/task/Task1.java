package br.com.tdso.operacao.task;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Task1 {

    //@Blocking
    public String executaTask11 (long tempo) {

        System.out.println(" ");
        System.out.println("Thread Task1.executaTask11 = " + Thread.currentThread().getName());
        System.out.println(" ");
        espera(tempo);
        return "Task 11";
    }
    //@Blocking
    public Uni<String> executaTask1 (long tempo) {

        System.out.println(" ");
        System.out.println("Thread Task1.executaTask1 = " + Thread.currentThread().getName());
        System.out.println(" ");
        espera(tempo);
        return Uni.createFrom().item("Task 1");
    }

    private void espera(long tempo){
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
