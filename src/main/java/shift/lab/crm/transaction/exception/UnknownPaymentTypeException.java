package shift.lab.crm.transaction.exception;

public class UnknownPaymentTypeException extends RuntimeException {
    public UnknownPaymentTypeException(String message) {
        super(message);
    }
}
