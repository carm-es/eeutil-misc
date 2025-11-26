package es.mpt.dsic.inside.ws.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;

import java.io.File;
import java.io.FileInputStream;

public class UtilsPdfA {
  protected final static Log logger = LogFactory.getLog(UtilsPdfA.class);

  /**
   * Valida un PDF contra el estándar PDF/A usando veraPDF
   */
  public static boolean validarConVeraPDF(File pdfFile, PDFAFlavour flavour) {
    try {
      // Inicializar veraPDF
      // VeraGreenfieldFoundryProvider.initialise();

      PDFAFlavour flavourAUsar = (flavour != null) ? flavour : PDFAFlavour.PDFA_2_B;

      try (PDFAParser parser =
          Foundries.defaultInstance().createParser(new FileInputStream(pdfFile), flavourAUsar)) {

        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavourAUsar, false);

        ValidationResult result = validator.validate(parser);

        return result.isCompliant();
      }
    } catch (Exception e) {
      logger.error("Error validando PDF/A con veraPDF", e);
      return false;
    }
  }

  /**
   * Obtiene detalles de los errores de validación
   */
  public static String obtenerDetallesValidacion(File pdfFile, PDFAFlavour flavour) {
    try {
      // VeraFoundryProvider.initialise();

      PDFAFlavour flavourAUsar = (flavour != null) ? flavour : PDFAFlavour.PDFA_2_B;

      try (PDFAParser parser =
          Foundries.defaultInstance().createParser(new FileInputStream(pdfFile), flavourAUsar)) {

        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavourAUsar, false);

        ValidationResult result = validator.validate(parser);

        StringBuilder detalles = new StringBuilder();
        detalles.append("Conformidad: ").append(result.isCompliant()).append(". ");
        detalles.append("Total de checks: ").append(result.getTotalAssertions()).append(". ");

        if (!result.isCompliant() && result.getTestAssertions() != null) {
          int failedCount = 0;
          detalles.append(" Primeros errores: ");
          for (org.verapdf.pdfa.results.TestAssertion assertion : result.getTestAssertions()) {
            if (assertion.getStatus() == org.verapdf.pdfa.results.TestAssertion.Status.FAILED) {
              failedCount++;
              if (failedCount <= 3) {
                detalles.append(assertion.getRuleId()).append(": ").append(assertion.getMessage())
                    .append("; ");
              }
            }
          }
          detalles.append("Checks fallidos: ").append(failedCount).append(".");
        }

        return detalles.toString();
      }
    } catch (Exception e) {
      return "No se pudieron obtener detalles: " + e.getMessage();
    }
  }

  /**
   * Mapea el nivel de compilación al flavour de veraPDF
   */
  public static PDFAFlavour mapearFlavourVeraPDF(Object nivelCompilacion) {
    if (nivelCompilacion == null) {
      return PDFAFlavour.PDFA_2_B;
    }

    String nivel = nivelCompilacion.toString().toUpperCase();

    // Mapeo de niveles comunes
    if (nivel.contains("1A") || nivel.contains("1_A")) {
      return PDFAFlavour.PDFA_1_A;
    } else if (nivel.contains("1B") || nivel.contains("1_B")) {
      return PDFAFlavour.PDFA_1_B;
    } else if (nivel.contains("2A") || nivel.contains("2_A")) {
      return PDFAFlavour.PDFA_2_A;
    } else if (nivel.contains("2B") || nivel.contains("2_B")) {
      return PDFAFlavour.PDFA_2_B;
    } else if (nivel.contains("2U") || nivel.contains("2_U")) {
      return PDFAFlavour.PDFA_2_U;
    } else if (nivel.contains("3A") || nivel.contains("3_A")) {
      return PDFAFlavour.PDFA_3_A;
    } else if (nivel.contains("3B") || nivel.contains("3_B")) {
      return PDFAFlavour.PDFA_3_B;
    } else if (nivel.contains("3U") || nivel.contains("3_U")) {
      return PDFAFlavour.PDFA_3_U;
    }

    // Por defecto PDF/A-2B
    return PDFAFlavour.PDFA_2_B;
  }
}
