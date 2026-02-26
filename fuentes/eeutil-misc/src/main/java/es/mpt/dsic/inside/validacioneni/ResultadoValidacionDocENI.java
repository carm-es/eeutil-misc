package es.mpt.dsic.inside.validacioneni;

public class ResultadoValidacionDocENI {

  private int numeroFirmas;
  private boolean firmaMultiple;
  private boolean valida;
  private String idFirma;
  private String refFirmaValor;

  public ResultadoValidacionDocENI() {}

  public ResultadoValidacionDocENI(int numeroFirmas, boolean refFirma, boolean valida) {
    super();
    this.numeroFirmas = numeroFirmas;
    this.firmaMultiple = refFirma;
    this.valida = valida;
  }


  public ResultadoValidacionDocENI(int numeroFirmas, boolean refFirma, boolean valida,
      String idFirma, String refFirmaValor) {
    super();
    this.numeroFirmas = numeroFirmas;
    this.firmaMultiple = refFirma;
    this.valida = valida;
    this.idFirma = idFirma;
    this.refFirmaValor = refFirmaValor;
  }

  public int getNumeroFirmas() {
    return numeroFirmas;
  }

  public void setNumeroFirmas(int numeroFirmas) {
    this.numeroFirmas = numeroFirmas;
  }

  public boolean isFirmaMultiple() {
    return firmaMultiple;
  }

  public void setFirmaMultiple(boolean firmaMultiple) {
    this.firmaMultiple = firmaMultiple;
  }

  public boolean isValida() {
    return valida;
  }

  public void setValida(boolean valida) {
    this.valida = valida;
  }

  public String getIdFirma() {
    return idFirma;
  }

  public void setIdFirma(String idFirma) {
    this.idFirma = idFirma;
  }

  public String getRefFirmaValor() {
    return refFirmaValor;
  }

  public void setRefFirmaValor(String refFirmaValor) {
    this.refFirmaValor = refFirmaValor;
  }

}
