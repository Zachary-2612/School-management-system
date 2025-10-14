package School.Management.System;

/*
handling the invalid payment throw an exception
 */
public class PaymentException extends Exception{
    public PaymentException(String error) {
        super(error);
    }
}
