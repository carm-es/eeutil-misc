package es.mpt.dsic.inside.ws.service.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Detalle", propOrder = {"idObjeto", "codigoRespuesta", "detalleRespuesta"})
public class Detalle {

  @XmlElement(required = true, name = "idObjeto")
  private String idObjeto;
  @XmlElement(required = true, name = "codigoRespuesta")
  private String codigoRespuesta;
  @XmlElement(required = true, name = "detalleRespuesta")
  private String detalleRespuesta;

  public String getIdObjeto() {
    return idObjeto;
  }

  public void setIdObjeto(String idObjeto) {
    this.idObjeto = idObjeto;
  }

  public String getCodigoRespuesta() {
    return codigoRespuesta;
  }

  public void setCodigoRespuesta(String codigoRespuesta) {
    this.codigoRespuesta = codigoRespuesta;
  }

  public String getDetalleRespuesta() {
    return detalleRespuesta;
  }

  public void setDetalleRespuesta(String detalleRespuesta) {
    this.detalleRespuesta = detalleRespuesta;
  }

  @Override
  public String toString() {
    return "Detalle [idObjeto=" + idObjeto + ", codigoRespuesta=" + codigoRespuesta
        + ", detalleRespuesta=" + detalleRespuesta + "]";
  }

}
