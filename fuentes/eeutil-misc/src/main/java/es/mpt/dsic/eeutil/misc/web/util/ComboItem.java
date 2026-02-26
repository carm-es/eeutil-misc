package es.mpt.dsic.eeutil.misc.web.util;

import java.io.Serializable;

public class ComboItem implements Serializable {
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 8005534523110356854L;

  private String key;
  private String value;
  private String type;

  public ComboItem(String key, String value, String type) {
    super();
    this.key = key;
    this.value = value;
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
