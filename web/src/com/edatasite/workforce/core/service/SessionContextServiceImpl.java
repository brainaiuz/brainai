package com.edatasite.workforce.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service("sessionContextService")
public class SessionContextServiceImpl implements SessionContextService {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T getInNewTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

}
