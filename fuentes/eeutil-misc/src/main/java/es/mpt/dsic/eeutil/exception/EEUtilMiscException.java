package es.mpt.dsic.eeutil.exception;

import es.mpt.dsic.inside.security.exception.EEUtilException;

/**
 * Clase para gestionar excepciones de la aplicacion EEUTilMiscExcpeion
 * 
 * @author miguel.moral
 *
 */
public class EEUtilMiscException extends EEUtilException {

  /**
   * Constructor con mensaje.
   * 
   * @param msg mensaje.
   */
  public EEUtilMiscException(String msg) {
    super(msg);
  }

  /**
   *
   * Constructor con mensaje y excepcion
   * 
   * @param msg
   * @param e
   */
  public EEUtilMiscException(String msg, Throwable e) {
    super(msg, e);
  }

  public EEUtilMiscException() {
    super();
  }



}
