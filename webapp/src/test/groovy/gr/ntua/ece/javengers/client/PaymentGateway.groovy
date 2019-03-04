package gr.ntua.ece.javengers.client

interface PaymentGateway {
    boolean makePayment(BigDecimal amount)
}