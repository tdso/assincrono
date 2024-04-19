package br.com.tdso.operacao;

import java.util.concurrent.CompletableFuture;

public interface Operacao<T> {

    public CompletableFuture<T> executa();
}
