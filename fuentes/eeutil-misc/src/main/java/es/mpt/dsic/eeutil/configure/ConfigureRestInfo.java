package es.mpt.dsic.eeutil.configure;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.utils.exception.EeutilException;

@Component
public class ConfigureRestInfo {

  public static ConfigureRestInfo laInstancia;

  private String baseUrlRestIgae;
  private String baseUrlPdfatools;

  private String tokenNameRestIgae;
  private String tokenNamePdfatools;
  private String tokenValueRestIgae;
  private String tokenValuePdfatools;



  public String getBaseUrlRestIgae() {
    return baseUrlRestIgae;
  }



  public String getTokenNameRestIgae() {
    return tokenNameRestIgae;
  }



  public String getTokenValueRestIgae() {
    return tokenValueRestIgae;
  }



  public String getBaseUrlPdfatools() {
    return baseUrlPdfatools;
  }



  public String getTokenNamePdfatools() {
    return tokenNamePdfatools;
  }



  public String getTokenValuePdfatools() {
    return tokenValuePdfatools;
  }



  public static ConfigureRestInfo getInstance() throws EeutilException, IOException {


    if (laInstancia == null) {
      laInstancia = new ConfigureRestInfo();
      // obtenemos el path de las propiedades externas.
      String path = EeutilApplicationDataConfig.CONFIG_PATH;

      // FileReader fReader= null;

      try (FileReader fReader = new FileReader(path + "/" + "eeutil.properties");) {
        // fReader=new FileReader(path+"/"+"eeutil.properties");

        Properties prop = new Properties();
        prop.load(fReader);

        laInstancia.baseUrlRestIgae = prop.getProperty("eeutil.igae.rs.base.url");
        laInstancia.tokenNameRestIgae = prop.getProperty("eeutil.igae.token.name");
        laInstancia.tokenValueRestIgae = prop.getProperty("eeutil.igae.token.value");

        laInstancia.baseUrlPdfatools = prop.getProperty("eeutil.pdfatools.rs.base.url");
        laInstancia.tokenNamePdfatools = prop.getProperty("eeutil.pdfatools.token.name");
        laInstancia.tokenValuePdfatools = prop.getProperty("eeutil.pdfatools.token.value");
      }
    }

    return laInstancia;



  }

}
