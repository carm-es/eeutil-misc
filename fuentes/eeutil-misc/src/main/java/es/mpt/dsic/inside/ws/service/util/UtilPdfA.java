package es.mpt.dsic.inside.ws.service.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import com.lowagie.text.pdf.PdfReader;

import es.mpt.dsic.eeutil.model.PdfConformance;
import es.mpt.dsic.inside.pdf.PdfUtils;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;

public class UtilPdfA {

  private static final String APPLICATION_PDF_MIME = "application/pdf";

  private static final String ERROR_AL_LEER_EL_NUMERO_DE_PAGINAS_A_CONVERTIR_MSG =
      "Error al leer el numero de paginas a convertir.";

  private static final String OS_NAME_PARAM = "os.name";

  private static final String Y_PARA_EL_ENTORNO_MSG = " y para el entorno ";

  // private static final String ERROR_LA_LICENCIA_DE_PDF_TOOLS_NO_ES_VALIDA_PARA_LA_VERSION =
  // "Error. La licencia de pdfTools no es valida o esta caducada para la version: ";

  protected static final Log logger = LogFactory.getLog(UtilPdfA.class);//

  @Value("${Path.temp}")
  private String rutaTemp;


  public int getPdfNumbersPages(File f, String password) throws EeutilException {
    PdfReader r = null;
    int retorno;
    try (FileInputStream fis = new FileInputStream(f);) {

      if (password == null || "".equals(password)) {
        r = new PdfReader(fis);
      } else {
        r = new PdfReader(fis, password.getBytes());
      }


      retorno = r.getNumberOfPages();
    } catch (IOException e) {
      // logger.error(e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new EeutilException(ERROR_AL_LEER_EL_NUMERO_DE_PAGINAS_A_CONVERTIR_MSG + e.getMessage(),
          e);
    } catch (final NoClassDefFoundError e) {

      if (e.getMessage().contains("bouncycastle")) {
        // logger.error("El fichero esta protegido y no ha podido abrirse.");
        throw new EeutilException(
            "Error al leer el numero de paginas a convertir.El fichero esta protegido y no ha podido abrirse."
                + e.getMessage(),
            e);
      } else {
        // logger.error(e.getMessage());
        throw new EeutilException(
            ERROR_AL_LEER_EL_NUMERO_DE_PAGINAS_A_CONVERTIR_MSG + e.getMessage(), e);
      }
    }

    finally {
      PdfUtils.close(r);
    }
    return retorno;
  }


  public int getPdfNumbersPages(byte[] data, String password) throws EeutilException {
    PdfReader r = null;
    int retorno;
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data);) {

      if (password == null || "".equals(password)) {
        r = new PdfReader(bis);
      } else {
        r = new PdfReader(bis, password.getBytes());
      }


      retorno = r.getNumberOfPages();
    } catch (IOException e) {
      // logger.error(e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new EeutilException(ERROR_AL_LEER_EL_NUMERO_DE_PAGINAS_A_CONVERTIR_MSG + e.getMessage(),
          e);
    } catch (final NoClassDefFoundError e) {

      if (e.getMessage().contains("bouncycastle")) {
        // logger.error("El fichero esta protegido y no ha podido abrirse.");
        throw new EeutilException(
            "Error al leer el numero de paginas a convertir.El fichero esta protegido y no ha podido abrirse."
                + e.getMessage(),
            e);
      } else {
        // logger.error(e.getMessage());
        throw new EeutilException(
            ERROR_AL_LEER_EL_NUMERO_DE_PAGINAS_A_CONVERTIR_MSG + e.getMessage(), e);
      }
    }

    finally {
      PdfUtils.close(r);
    }
    return retorno;
  }


  /**
   * Genera el objeto Conformance a partir del nivel de compilacion.
   * 
   * @param nivelCompilacion
   * @return
   * @throws Exception
   */
  public PdfConformance convertCodeToPartLevel(Integer nivelCompilacion) throws Exception {



    if (nivelCompilacion == null) {
      return new PdfConformance(2, "b");
    }

    // casos de pdf sin pdfa
    switch (nivelCompilacion) {
      case 0:
        throw new Exception(
            "El valor " + nivelCompilacion + " no es correcto para la conversion a PDF/A");
      case 4096:
        return new PdfConformance("1.0");
      case 4352:
        return new PdfConformance("1.1");
      case 4608:
        return new PdfConformance("1.2");
      case 4864:
        return new PdfConformance("1.3");
      case 5120:
        return new PdfConformance("1.4");

      case 5121:
        return new PdfConformance(1, "b");
      case 5122:
        return new PdfConformance(1, "a");

      case 5376:
        return new PdfConformance("1.5");
      case 5632:
        return new PdfConformance("1.6");
      case 5888:
        return new PdfConformance("1.7");

      case 5889:
        return new PdfConformance(2, "b");
      case 5890:
        return new PdfConformance(2, "u");
      case 5891:
        return new PdfConformance(2, "a");

      case 5905:
        return new PdfConformance(3, "b");
      case 5906:
        return new PdfConformance(3, "u");
      case 5907:
        return new PdfConformance(3, "a");



      case 8192:
        return new PdfConformance("2.0");


      default:
        throw new Exception(
            "El valor " + nivelCompilacion + " no es correcto para la conversion a PDF/A");

    }

  }

}
