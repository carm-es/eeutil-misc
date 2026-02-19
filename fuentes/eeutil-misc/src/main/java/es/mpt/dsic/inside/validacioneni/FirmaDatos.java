package es.mpt.dsic.inside.validacioneni;


public class FirmaDatos {

  private Object datosFirmados;
  private Object contenidoFirma;
  private boolean firmaMultiple;
  private String idFirma;
  private String refFirma;


  public Object getDatosFirmados() {
    return datosFirmados;
  }

  public void setDatosFirmados(Object datosFirmados) {
    this.datosFirmados = datosFirmados;
  }

  public Object getContenidoFirma() {
    return contenidoFirma;
  }

  public void setContenidoFirma(Object contenidoFirma) {
    this.contenidoFirma = contenidoFirma;
  }

  public boolean isFirmaMultiple() {
    return firmaMultiple;
  }

  public void setFirmaMultiple(boolean firmaMultiple) {
    this.firmaMultiple = firmaMultiple;
  }

  public String getIdFirma() {
    return idFirma;
  }

  public void setIdFirma(String idFirma) {
    this.idFirma = idFirma;
  }

  public String getRefFirma() {
    return refFirma;
  }

  public void setRefFirma(String refFirma) {
    this.refFirma = refFirma;
  }

}
