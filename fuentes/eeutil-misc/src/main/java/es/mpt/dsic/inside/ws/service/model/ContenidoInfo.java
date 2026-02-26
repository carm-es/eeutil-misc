package es.mpt.dsic.inside.ws.service.model;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContenidoInfo", propOrder = {"contenido", "tipoMIME"})
public class ContenidoInfo {

  @XmlElement(required = true, name = "contenido")
  private byte[] contenido;
  @XmlElement(required = false, name = "tipoMIME")
  private String tipoMIME;

  public byte[] getContenido() {
    return contenido;
  }

  public void setContenido(byte[] contenido) {
    this.contenido = contenido;
  }

  public String getTipoMIME() {
    return tipoMIME;
  }

  public void setTipoMIME(String tipoMIME) {
    this.tipoMIME = tipoMIME;
  }

  @Override
  public String toString() {
    return "ContenidoInfo [contenido=" + Arrays.toString(contenido) + ", tipoMIME=" + tipoMIME
        + "]";
  }



}
