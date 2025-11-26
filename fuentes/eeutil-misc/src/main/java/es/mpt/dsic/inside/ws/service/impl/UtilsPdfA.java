package es.mpt.dsic.inside.ws.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.parser.PreflightParser;

import java.io.File;
import java.io.FileInputStream;

public class UtilsPdfA {
  protected final static Log logger = LogFactory.getLog(UtilsPdfA.class);

  /**
   * Valida un PDF contra el estándar PDF/A usando PDFBox Preflight
   */
  public static boolean validarConPDFBox(File pdfFile, String flavour) {
    try {
      PreflightParser parser = new PreflightParser(pdfFile);
      parser.parse();

      try (PreflightDocument document = parser.getPreflightDocument()) {
        document.validate();
        ValidationResult result = document.getResult();
        return result.isValid();
      }
    } catch (Exception e) {
      logger.error("Error validando PDF/A con PDFBox Preflight", e);
      return false;
    }
  }

  /**
   * Obtiene detalles de los errores de validación
   */
  public static String obtenerDetallesValidacion(File pdfFile, String flavour) {
    try {
      PreflightParser parser = new PreflightParser(pdfFile);
      parser.parse();

      try (PreflightDocument document = parser.getPreflightDocument()) {
        document.validate();
        ValidationResult result = document.getResult();

        StringBuilder detalles = new StringBuilder();
        detalles.append("Conformidad: ").append(result.isValid()).append(". ");

        if (!result.isValid() && result.getErrorsList() != null) {
          detalles.append("Errores: ").append(result.getErrorsList().size()).append(". ");
          int count = 0;
          for (org.apache.pdfbox.preflight.ValidationResult.ValidationError error : result
              .getErrorsList()) {
            if (count < 3) {
              detalles.append(error.getErrorCode()).append(": ").append(error.getDetails())
                  .append("; ");
            }
            count++;
          }
        }

        return detalles.toString();
      }
    } catch (Exception e) {
      return "No se pudieron obtener detalles: " + e.getMessage();
    }
  }

  /**
   * Mapea el nivel de compilación al flavour de PDFBox
   */
  public static String mapearFlavourPDFBox(Object nivelCompilacion) {
    if (nivelCompilacion == null) {
      return "PDFA_1_B";
    }

    String nivel = nivelCompilacion.toString().toUpperCase();

    // PDFBox Preflight principalmente soporta PDF/A-1b
    if (nivel.contains("1A") || nivel.contains("1_A")) {
      return "PDFA_1_A";
    } else if (nivel.contains("1B") || nivel.contains("1_B")) {
      return "PDFA_1_B";
    }

    // Por defecto PDF/A-1B (el más soportado por PDFBox Preflight)
    return "PDFA_1_B";
  }
}
