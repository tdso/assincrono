package br.com.tdso.operacao.task;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Task2Runnable implements Runnable {
    @Override
    public void run() {

        System.out.println(" ");
        System.out.println("Thread Task2Runnable = " + Thread.currentThread().getName());
        System.out.println(" ");
        espera(2000);
    }

    private void espera(long tempo){
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
