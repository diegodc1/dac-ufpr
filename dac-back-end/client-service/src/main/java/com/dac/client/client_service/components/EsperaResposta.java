package com.dac.client.client_service.components;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EsperaResposta {

    private final Map<String, CompletableFuture<Object>> pendentes = new ConcurrentHashMap<>();

    public <T> void aguardar(String chave, CompletableFuture<T> future) {
        pendentes.put(chave, (CompletableFuture<Object>) future);
    }

    public void resolver(String chave, Object resultado) {
        CompletableFuture<Object> future = pendentes.remove(chave);
        if (future != null) {
            future.complete(resultado);
        }
    }

    public void erro(String chave, String mensagemErro) {
        CompletableFuture<Object> future = pendentes.remove(chave);
        if (future != null) {
            future.completeExceptionally(new RuntimeException(mensagemErro));
        }
    }
}