package com.don.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {
    @JsonProperty("entry")
    private TicketData data;

    public TicketData getData() {
        return data;
    }

    public void setData(TicketData data) {
        this.data = data;
    }
}
