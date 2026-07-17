package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request;

public class GetStatement {
    private Long from;
    private Long to;

    public GetStatement(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }
}
