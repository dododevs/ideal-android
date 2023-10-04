package revolver.ideal.io.exceptions;

public class InvalidAPDUPayloadException extends RuntimeException {

    public InvalidAPDUPayloadException(Exception e) {
        super(e);
    }

    public InvalidAPDUPayloadException(String reason) {
        super(String.format("Invalid APDU payload: %s", reason));
    }

}
