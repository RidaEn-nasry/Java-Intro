class TransactionNotFoundException extends RuntimeException {
    TransactionNotFoundException(String message) {
        super(message);
    }
}