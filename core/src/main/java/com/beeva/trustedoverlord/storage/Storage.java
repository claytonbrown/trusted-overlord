package com.beeva.trustedoverlord.storage;

import java.util.concurrent.CompletableFuture;

/**
 * @param <I> Bean to extract data to store
 * @param <O> Bean returned when stored
 */
public interface Storage<I, O> {

    void save(String profileName, I profile, final CompletableFuture<O> future);

}
