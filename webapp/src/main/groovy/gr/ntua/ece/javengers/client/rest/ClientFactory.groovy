package gr.ntua.ece.javengers.client.rest

import org.apache.http.impl.client.CloseableHttpClient

interface ClientFactory {
    CloseableHttpClient newClient()
}

