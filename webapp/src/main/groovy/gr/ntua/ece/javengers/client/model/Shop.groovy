package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical

@Canonical class Shop {

    String id
    String name
    String address
    double lat
    double lng
    List<String> tags
    boolean withdrawn

}

