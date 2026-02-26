package es.mpt.dsic.eeutil.model;

public class PdfConformance {

  String version;

  Integer level;

  String compilance;

  public PdfConformance(Integer level, String compilance) {
    super();
    this.level = level;
    this.compilance = compilance;
  }


  public PdfConformance(String version) {
    super();
    this.version = version;
  }


  public String getVersion() {
    return version;
  }


  public Integer getLevel() {
    return level;
  }


  public String getCompilance() {
    return compilance;
  }



}
