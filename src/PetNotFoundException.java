package PACKAGE_NAME;

public class PetNotFoundException extends RuntimeException {
  public PetNotFoundException(String message) {
    super(message);
  }
}
