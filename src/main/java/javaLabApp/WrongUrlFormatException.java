package javaLabApp;

public class WrongUrlFormatException extends Exception {
    private String message;
    private GlobalFunctions service = new GlobalFunctions();

    WrongUrlFormatException(String message){
        this.message=message;
    }
    void handleException(){
        service.setExceptionFrame(message);
    }

    public String getMessage() {
        return message;
    }

}
