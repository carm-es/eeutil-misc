package es.mpt.dsic.inside.ws.service.model;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaValidacionENI", propOrder = {"global", "detalle"})
public class RespuestaValidacionENI {

  @XmlElement(required = true, name = "global")
  private String global;
  @XmlElement(required = true, name = "detalle")
  private List<Detalle> detalle;



  public String getGlobal() {
    return global;
  }



  public void setGlobal(String global) {
    this.global = global;
  }



  public List<Detalle> getDetalle() {
    return detalle;
  }



  public void setDetalle(List<Detalle> detalle) {
    this.detalle = detalle;
  }



  @Override
  public String toString() {
    return "RespuestaValidacionENI [global=" + global + ", " + detalle.toString() + "]";
  }



}
