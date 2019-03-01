package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical;

import java.sql.Date;

@Canonical class Entry {

    String id
    Double price
    Date dateFrom
    Date dateTo
    Long productId
    Long shopId
}


