package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical;

import java.sql.Date;

@Canonical class Entry {

    String id
    Double price
    String dateFrom
    String dateTo
    Long productId
    Long shopId
}


