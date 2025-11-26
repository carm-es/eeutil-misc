/*
 * Copyright (C) 2012-13 MINHAP, Gobierno de España This program is licensed and may be used,
 * modified and redistributed under the terms of the European Public License (EUPL), either version
 * 1.1 or (at your option) any later version as soon as they are approved by the European
 * Commission. Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * more details. You should have received a copy of the EUPL1.1 license along with this program; if
 * not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */

package es.mpt.dsic.inside.ws.service.util;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lowagie.text.pdf.PdfReader;
import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.pdf.PdfUtils;
import es.mpt.dsic.inside.utils.file.FileUtil;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import javax.mail.util.ByteArrayDataSource;

public class UtilPdfA {

  protected final static Log logger = LogFactory.getLog(UtilPdfA.class);//

  // Converter properties
  private String converterLibrary;
  private String converterKey;
  private boolean converterReportDetails;
  private boolean converterReportSummary;
  private boolean converterSubsetFonts;
  private boolean converterPostAnalyze;

  // Validator properties
  private String validatorLibrary;
  private String validatorKey;
  private int validatorReportingLevel;

  public void configure() {}

  public String getConverterLibrary() {
    return converterLibrary;
  }

  public void setConverterLibrary(String converterLibrary) {
    this.converterLibrary = converterLibrary;
  }

  public String getConverterKey() {
    return converterKey;
  }

  public void setConverterKey(String converterKey) {
    this.converterKey = converterKey;
  }

  public boolean isConverterReportDetails() {
    return converterReportDetails;
  }

  public void setConverterReportDetails(boolean converterReportDetails) {
    this.converterReportDetails = converterReportDetails;
  }

  public boolean isConverterReportSummary() {
    return converterReportSummary;
  }

  public void setConverterReportSummary(boolean converterReportSummary) {
    this.converterReportSummary = converterReportSummary;
  }

  public boolean isConverterSubsetFonts() {
    return converterSubsetFonts;
  }

  public void setConverterSubsetFonts(boolean converterSubsetFonts) {
    this.converterSubsetFonts = converterSubsetFonts;
  }

  public boolean isConverterPostAnalyze() {
    return converterPostAnalyze;
  }

  public void setConverterPostAnalyze(boolean converterPostAnalyze) {
    this.converterPostAnalyze = converterPostAnalyze;
  }

  public String getValidatorLibrary() {
    return validatorLibrary;
  }

  public void setValidatorLibrary(String validatorLibrary) {
    this.validatorLibrary = validatorLibrary;
  }

  public String getValidatorKey() {
    return validatorKey;
  }

  public void setValidatorKey(String validatorKey) {
    this.validatorKey = validatorKey;
  }

  public int getValidatorReportingLevel() {
    return validatorReportingLevel;
  }

  public void setValidatorReportingLevel(int validatorReportingLevel) {
    this.validatorReportingLevel = validatorReportingLevel;
  }

  public Boolean isPDFA(byte[] data, String password, Integer level) throws InSideException {
    try {
      ByteArrayDataSource dataSource = new ByteArrayDataSource(data, "application/pdf");
      PreflightParser parser = new PreflightParser(dataSource);
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

  public int getPdfNumbersPages(File f) throws InSideException, IOException {
    FileInputStream fis = null;
    PdfReader r = null;
    int retorno;
    try {
      fis = new FileInputStream(f);
      r = new PdfReader(fis);

      retorno = r.getNumberOfPages();
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al leer el numero de paginas a convertir.", estadoInfo);
    } catch (IOException e) {
      logger.error(e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al leer el numero de paginas a convertir.", estadoInfo);
    } finally {
      FileUtil.close(fis);
      PdfUtils.close(r);
    }
    return retorno;
  }

  public boolean validateSimple(byte[] data) {
    try {
      boolean isCompliant = isCompliant = isPDFA(data, "none", 1);
      // Print result
      System.out.printf("Document is %scompliant with the PDF/A-2b standard.\n",
          isCompliant ? "" : "not ");
      return isCompliant;

    } catch (InSideException e) {
      logger.error("Comprobando validando PDF/A", e);
    }
    return false;
  }

}
