package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical

@Canonical
class PriceInfoList extends Paging {

    List<PriceInfo> prices
}

