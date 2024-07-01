package hr.game.pandemic.exeption;

public class WrongPlayerNameException extends RuntimeException {

    public WrongPlayerNameException(String message) {
        super(message);
    }
}
