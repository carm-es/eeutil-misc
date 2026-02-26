package es.mpt.dsic.inside.ws.service.util;

public class TipoFirmaException extends Exception {

  private static final long serialVersionUID = 1L;

  private final String message;

  public TipoFirmaException(String message) {
    super();
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

}
