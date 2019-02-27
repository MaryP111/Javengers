package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical

@Canonical
public class QueryList extends Paging {

    List<QueryResult> queryResults;
}

