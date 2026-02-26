package es.mpt.dsic.inside.ws.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.mpt.dsic.inside.exception.base.DataWsEntradaBase;
import es.mpt.dsic.inside.exception.interfaz.IMDCAble;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCNInfo", propOrder = {"contenido"})

public class TCNInfo extends DataWsEntradaBase implements IMDCAble {

  @XmlElement(required = true, name = "contenido")
  private ContenidoInfo contenido;

  public ContenidoInfo getContenido() {
    return contenido;
  }

  public void setContenido(ContenidoInfo contenido) {
    this.contenido = contenido;
  }

}
