package br.com.tdso.rest;

import br.com.tdso.operacao.task.Task1;
import br.com.tdso.operacao.task.Task2Runnable;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.MultiSubscribe;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.resteasy.reactive.RestResponse;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Path("/rest")
public class RestAssincrono {

    @Inject
    Task1 task1;
    @Inject
    Task2Runnable task2Runnable;

    @Inject
    ManagedExecutor managedExecutor;

    @GET
    public Uni<String> getUni(){
        System.out.println(Thread.currentThread().getName());
        return Uni.createFrom().item("item")
                .onItem()
                .transform(String::toUpperCase);
//                .onItem()
//                .transform(ResponseBuilder::build);
    }
    @GET
    @Blocking
    @Path("/block")
    public Uni<String> getUniBlock() {
        System.out.println(Thread.currentThread().getName());
        return Uni.createFrom().item("item")
                .onItem()
                .transform(String::toUpperCase);
    }
    @Path("/cf")
    @GET
    public CompletableFuture<String> getCF(){
        System.out.println(Thread.currentThread().getName());
        // pode ocorrer problema de contexto ? como no TradeSystem ?
        return CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "completable future wait";
        });
    }

    @Path("/cf2")
    @GET
    public Uni<String> getCF2(){
        System.out.println(" ");
        System.out.println(" INICIO REQUISICAO ");
        System.out.println("getCF2 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

        return Uni.createFrom().item(() -> task1.executaTask1(4200))
                .onItem().call(c -> Uni.createFrom().item(task1.executaTask1(4000)))
                //.onItem().transform(String::toUpperCase)
                //.onItem().invoke(task2Runnable)
                .onItem().transform(x ->  "final ==> " + x)
                .onItem().transform(x -> {
                    long tempoFim = System.currentTimeMillis();
                    System.out.println("Tempo FIM: " + tempoFim);
                    System.out.println("Duracao: " + (tempoFim - tempoInicio));
                    return x;
                })
                .runSubscriptionOn(managedExecutor);
    }
    @Path("/cf3")
    @GET
    //@Blocking
    public Uni<String> getCF3() {
        System.out.println(" ");
        System.out.println(" INICIO REQUISICAO 3");
        System.out.println("getCF3 ==> " + Thread.currentThread().getName());
        System.out.println("Tempo Inicio: " + System.currentTimeMillis());

        Uni.combine().all()
                .unis(task1.executaTask1(3000), task1.executaTask1(4000))
                .asTuple()
                .runSubscriptionOn(managedExecutor)
                .subscribe().with(tuple -> {
                    System.out.println("Programming Quote: " + tuple.getItem1());
                    System.out.println("Chuck Norris Quote: " + tuple.getItem2());
                    System.out.println("Tempo FIM: " + System.currentTimeMillis());
                });
        return Uni.createFrom().item("Task 1");

    }

    @Path("/cf4")
    @GET
    //@Blocking
    public Uni<String> getCF4() {
        System.out.println(" ");
        System.out.println(" INICIO REQUISICAO 4");
        System.out.println("getCF4 ==> " + Thread.currentThread().getName());
        System.out.println("Tempo Inicio: " + System.currentTimeMillis());

        Multi.createFrom().items(
                        task1.executaTask1(3000),
                        task1.executaTask1(4000)
                )
                .runSubscriptionOn(managedExecutor)
                .subscribe().with(uni -> {

                                        System.out.println("RODOU OK >  " + uni);
                                        System.out.println("Tempo FIM: " + System.currentTimeMillis());

                        });

                                            //y -> System.out.println("Erro Y >> " + y))
                return Uni.createFrom().item("Task 1");

    }

    @Path("/cf5")
    @GET
    //@Blocking
    public Uni<String> getCF5() {
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 5");
        System.out.println("getCF5 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

        return Uni.createFrom().emitter(em -> {
            Uni<String> result = task1.executaTask1(3000);
            em.complete(result.toString());
            //
        }).onItem().transform(x -> x.toString() + " !!! ")
          .onItem().transform(x -> {
                    long tempoFim = System.currentTimeMillis();
                    System.out.println("Tempo FIM: " + tempoFim);
                    System.out.println("Duracao: " + (tempoFim - tempoInicio));

                    return x;
                })
                .runSubscriptionOn(managedExecutor);


    }

    @Path("/cf6")
    @GET
    public Multi<String> getCF6() {
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 5");
        System.out.println("getCF6 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

        String string = Multi.createFrom().items(3000, 2000, 4000)
                //.onItem().transformToMultiAndConcatenate(t -> task1.executaTask1(t).toMulti())
                .onItem().transform(t -> task1.executaTask11(t))
                .onItem().transform(String::toUpperCase)
                .onItem().transform(texto -> texto + "@@")
                .runSubscriptionOn(managedExecutor)
                .subscribe().toString();


        long tempoFim = System.currentTimeMillis();
        System.out.println("Multis = " + string);
        System.out.println("Tempo FIM : " + tempoFim);
        System.out.println("Duracao: " + (tempoFim - tempoInicio));

        return Multi.createFrom().item("1");
    }

    @Path("/cf7")
    @GET
    public Multi<String> getCF7() {
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 7");
        System.out.println("getCF7 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

        String string = Multi.createFrom().items(3000, 2000, 4000)
                //.onItem().transformToMultiAndConcatenate(t -> task1.executaTask1(t).toMulti())
                .emitOn(Executors.newFixedThreadPool(3))
                .onItem().transform(t -> task1.executaTask11(t))
                .onItem().transform(String::toUpperCase)
                .onItem().transform(texto -> texto + "@@")
                //.runSubscriptionOn(managedExecutor)
                //.runSubscriptionOn(Executors.newFixedThreadPool(3))
                //.runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                //.emitOn(Infrastructure.getDefaultWorkerPool()) // executou na thread Vertx - bloqueia
                //.emitOn(managedExecutor) // executou na worker thread sequencial
                .subscribe().with(x -> {    // roda para cada item
                    System.out.println("Dentro do subscribe - valor de X = " + x);
                    long tempoFim = System.currentTimeMillis();
                    System.out.println("Tempo FIM DENTRO SUBSCRIBE : " + tempoFim);
                    System.out.println("Duracao IN SUBSCRIBE: " + (tempoFim - tempoInicio));
                }).toString();

        long tempoFim = System.currentTimeMillis();
        System.out.println("Multis = " + string);
        System.out.println("Tempo FIM : " + tempoFim);
        System.out.println("Duracao: " + (tempoFim - tempoInicio));

        return Multi.createFrom().item("1");
    }

    @Path("/cf8")
    @GET
    public Multi<String> getCF8() {
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 8");
        System.out.println("getCF8 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

        Multi<String> multi1 = Multi.createFrom().item(task1.executaTask11(3000));
        Multi<String> multi2 = Multi.createFrom().item(task1.executaTask11(2000));

        Multi.createBy().combining().streams(multi1, multi2).asTuple().onItem()
                .transform(g -> g + "!!!")
                //.runSubscriptionOn(managedExecutor)
                .subscribe().with(x -> {    // roda para cada item
                    System.out.println("Dentro do subscribe - valor de X = " + x);
                    long tempoFim = System.currentTimeMillis();
                    System.out.println("Tempo FIM DENTRO SUBSCRIBE : " + tempoFim);
                    System.out.println("Duracao IN SUBSCRIBE: " + (tempoFim - tempoInicio));
                });

        long tempoFim = System.currentTimeMillis();
        System.out.println("FIM PGM = " );
        System.out.println("Tempo FIM : " + tempoFim);
        System.out.println("Duracao: " + (tempoFim - tempoInicio));

        return Multi.createFrom().item("1");
    }

    @Path("/cf9")
    @GET
    public Multi<String> getCF9() {
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 9");
        System.out.println("getCF9 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);

//        List<Uni<String>> results = List.of(
//                Uni.createFrom().item(task1.executaTask11(3000)),
//                Uni.createFrom().item(task1.executaTask11(2000))
//        );
        //Uni<List<String>> combined = Uni.combine().all().unis(results);
//        System.out.println("1 execucao " );
//        Uni<String> uniA = task1.executaTask1(3000);
//
//        System.out.println("2 execucao " );
//
//        Uni<String> uniB = task1.executaTask1(2000);
//
//        System.out.println("PASSO MEIO " );

        Uni.combine()
                .all().unis(task1.executaTask1(3000), task1.executaTask1(2000)).asTuple()
                .runSubscriptionOn(managedExecutor)
                .subscribe().with(tuple -> {
                    System.out.println("Response from A: " + tuple.getItem1());
                    System.out.println("Response from B: " + tuple.getItem2());
                    long tempoFim = System.currentTimeMillis();
                    System.out.println("Duracao SUBSCRIBE: " + (tempoFim - tempoInicio));
                })
        ;

//        Uni.combine()
//                .all().unis(uniA, uniB).asTuple()
//                .subscribe().with(tuple -> {
//                    System.out.println("Response from A: " + tuple.getItem1());
//                    System.out.println("Response from B: " + tuple.getItem2());
//                    long tempoFim = System.currentTimeMillis();
//                    System.out.println("Duracao SUBSCRIBE: " + (tempoFim - tempoInicio));
//                });

        long tempoFim = System.currentTimeMillis();
        System.out.println("FIM PGM DATA = " );
        System.out.println("Tempo FIM : " + tempoFim);
        System.out.println("Duracao: " + (tempoFim - tempoInicio));

        return Multi.createFrom().item("1");
    }

    @GET
    @Path("/cf10")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Long> longPublisher() {
        return Multi.createFrom()
                .ticks().every(Duration.ofMillis(500));
    }

    // Exemplo abaixo funcionou - executando em paralelo as tarefas
    @GET
    @Path("/cf11")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Uni<String> longPublisher11() {
        final Long[] tempoFim = new Long[2];
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 11");
        System.out.println("getCF11 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);
        //Uni<String> uni = task1.executaTask1(4000);

        Uni<String> uni = Uni.createFrom().item(() -> task1.executaTask11(4000))
                .runSubscriptionOn(managedExecutor);

        Uni<String> uni2 = Uni.createFrom().item(() -> task1.executaTask11(5000))
                .runSubscriptionOn(managedExecutor);

        System.out.println("Efeito Lazing");

        final String[] str1 = new String[2];
        uni.onItem().transform(texto -> texto.toUpperCase()).onItem().transform(texto -> {
            str1[0] = texto;
            return texto;
        }).subscribe().with(x -> {
            System.out.println("--------------------------------------------------");
            System.out.println("Dentro do processo de 4s");
            tempoFim[0] = System.currentTimeMillis();
            System.out.println("Duracao: " + (tempoFim[0] - tempoInicio));
            System.out.println("Resposta: " + x);
        });

        uni2.onItem().transform(texto -> texto.toUpperCase()).onItem().transform(texto -> {
            str1[1] = texto;
            return texto;
        }).subscribe().with( x -> {
            System.out.println("--------------------------------------------------");
            System.out.println("Dentro do processo de 5s");
            tempoFim[1] = System.currentTimeMillis();
            System.out.println("Duracao: " + (tempoFim[1] - tempoInicio));
            System.out.println("Resposta: " + x);
            System.out.println("Intervalo entre execucoes: " + (tempoFim[1] - tempoFim[0]));
        });

        System.out.println("continou --> str[0] = " + str1[0]);

        //String str2 = uni2.onItem().transform(texto -> texto.toUpperCase()).toString();

        return Uni.createFrom().item(str1[0]);
//        return uni.onItem().transform(texto -> texto.toUpperCase())
//                .onFailure().recoverWithItem("SEM DADOS");
    }

    // exemplo abaixo e uma evolucao do anterior - no abaixo encadeamos chamadas dependentes que rodam
    // fora da thread i/o porque são bloqueantes - se abrirmos 2 instancias do browser e chamarmos o rest
    // veremos que a thread i/o não está bloqueada e responde as 2 requisicoes
    @GET
    @Path("/cf12")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Uni<String> longPublisher12() {
        final Long[] tempoFim = new Long[2];
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" INICIO REQUISICAO 12");
        System.out.println("getCF12 ==> " + Thread.currentThread().getName());
        long tempoInicio = System.currentTimeMillis();
        System.out.println("Tempo Inicio: " + tempoInicio);
        //Uni<String> uni = task1.executaTask1(4000);

        Uni<String> uni = Uni.createFrom().item(() -> task1.executaTask11(4000))
                .runSubscriptionOn(managedExecutor);

         System.out.println("Efeito Lazing");

        final String[] str = new String[2];
        uni.onItem().transform(texto -> texto.toUpperCase()).onItem().transform(texto -> {
            str[0] = texto;
            return texto;
        })
                .onItem().transformToUni(arg -> task1.executaTask1(4000))
                .onItem().transform(w -> {
                    str[1] = w.toUpperCase();
                    return "2 Transform = " + w;
                })
                .subscribe().with(x -> {
            System.out.println("--------------------------------------------------");
            System.out.println("Dentro do processo de 8s");
            tempoFim[0] = System.currentTimeMillis();
            System.out.println("Duracao: " + (tempoFim[0] - tempoInicio));
            System.out.println("Resposta: " + str[0] + " / " + str[1]);
        });

        System.out.println("continou --> str[0] = " + str[0]);

        //String str2 = uni2.onItem().transform(texto -> texto.toUpperCase()).toString();

        return Uni.createFrom().item(str[0]+str[1]);
//        return uni.onItem().transform(texto -> texto.toUpperCase())
//                .onFailure().recoverWithItem("SEM DADOS");
    }


}
