package es.mpt.dsic.eeutil.misc.web.util;


/**
 * Clase para mostrar mensajes al usuario en pantalla.
 * 
 */
public class MessageObject {

  /** Nivel del mensaje: INFO, ERROR, WARNING, etc. Ver clase de Constantes. */
  private int level;

  /** Cadena con el mensaje a mostrar. */
  private String message;

  /**
   * Constructor por defecto.
   */
  public MessageObject() {
    super();
  }

  /**
   * Constructor con par�metros level y message.
   * 
   * @param level
   * @param message
   */
  public MessageObject(int level, String message) {
    super();
    this.level = level;
    this.message = message;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "MessageObject [level=" + level + ", message=" + message + "]";
  }

}
