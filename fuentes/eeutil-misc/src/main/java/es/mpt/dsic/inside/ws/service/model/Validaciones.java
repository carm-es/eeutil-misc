package es.mpt.dsic.inside.ws.service.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.mpt.dsic.inside.exception.base.DataWsEntradaBase;
import es.mpt.dsic.inside.exception.interfaz.IMDCAble;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Validaciones",
    propOrder = {"validaSchema", "validaDir3", "validaSIA", "validaFirma"})
public class Validaciones extends DataWsEntradaBase implements IMDCAble {

  @XmlElement(required = false, name = "validaSchema", type = Boolean.class)
  private boolean validaSchema;
  @XmlElement(required = false, name = "validaDir3", type = Boolean.class)
  private boolean validaDir3;
  @XmlElement(required = false, name = "validaSIA", type = Boolean.class)
  private boolean validaSIA;
  @XmlElement(required = false, name = "validaFirma", type = Boolean.class)
  private boolean validaFirma;


  public boolean isValidaSchema() {
    return validaSchema;
  }

  public void setValidaSchema(boolean validaSchema) {
    this.validaSchema = validaSchema;
  }

  public boolean isValidaDir3() {
    return validaDir3;
  }

  public void setValidaDir3(boolean validaDir3) {
    this.validaDir3 = validaDir3;
  }

  public boolean isValidaSIA() {
    return validaSIA;
  }

  public void setValidaSIA(boolean validaSIA) {
    this.validaSIA = validaSIA;
  }

  public boolean isValidaFirma() {
    return validaFirma;
  }

  public void setValidaFirma(boolean validaFirma) {
    this.validaFirma = validaFirma;
  }



}
