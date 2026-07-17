package com.edatasite.workforce.core.service;

import java.util.function.Supplier;

public interface SessionContextService {

    <T> T getInNewTransaction(Supplier<T> supplier);
}
