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

package es.mpt.dsic.inside.ws.service.impl;

import java.io.*;
import java.util.Calendar;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;

import es.mpt.dsic.inside.ws.service.util.PdfCompliance;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.mpt.dsic.inside.pdf.converter.PdfConverter;
import es.mpt.dsic.inside.pdf.exception.PdfConversionException;
import es.mpt.dsic.inside.security.context.AplicacionContext;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.security.model.ApplicationLogin;
import es.mpt.dsic.inside.security.service.impl.EeutilAplicacionConversionService;
import es.mpt.dsic.inside.utils.file.FileUtil;
import es.mpt.dsic.inside.utils.xml.XMLUtil;
import es.mpt.dsic.inside.ws.service.EeUtilServiceMtom;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntradaMtom;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalidaMtom;
import es.mpt.dsic.inside.ws.service.util.UtilPdfA;


@Service("eeUtilServiceMtom")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilServiceMtom")
public class EeUtilServiceMtomImpl implements EeUtilServiceMtom {

  protected final static Log logger = LogFactory.getLog(EeUtilServiceMtomImpl.class);

  @Autowired
  private AplicacionContext aplicacionContext;

  @Autowired
  private UtilPdfA utilPdfA;

  @Autowired
  private EeutilAplicacionConversionService aplicacionConversionService;

  @Autowired
  PdfConverter pdfConverter;

  @Override
  public PdfSalidaMtom convertirPDFA(ApplicationLogin info, DocumentoEntradaMtom docEntrada,
      Integer nivelCompilacion) throws InSideException {
    try {
      logger.debug("convertirPDFA");
      logger.debug("mime:" + docEntrada.getMime());
      logger.debug("contenido:" + docEntrada.getContenido());
      PdfSalidaMtom retorno = new PdfSalidaMtom();

      AppInfo appInfo = aplicacionContext.getAplicacionInfo();
      String ipOpenOffice = appInfo.getPropiedades().get("ip.openoffice");
      String portOpenOffice = appInfo.getPropiedades().get("port.openoffice");

      String filePathIn = FileUtil.createFilePath("PDF_",
          IOUtils.toByteArray(docEntrada.getContenido().getInputStream()));

      File pdfOriginal = pdfConverter.convertir(ipOpenOffice, portOpenOffice, new File(filePathIn),
          docEntrada.getMime());

      Boolean isPDFA = utilPdfA.isPDFA(IOUtils.toByteArray(new FileInputStream(pdfOriginal)),
          docEntrada.getPassword(), nivelCompilacion);


      // Determinar el flavour de PDF/A objetivo
      PdfCompliance flavourObjetivo = PdfCompliance.getCompliance(nivelCompilacion);

      if (!isPDFA) {
        logger.debug("El documento no es PDF/A, iniciando conversión...");

        retorno.setMime("application/pdf");
        byte[] pdfConverted = UtilsPdfA
            .convertToPDFA(IOUtils.toByteArray(new FileInputStream(pdfOriginal)), flavourObjetivo);

        DataSource dataSource = new ByteArrayDataSource(pdfConverted, "application/pdf");

        retorno.setContenido(new DataHandler(dataSource));

        int numberPages = utilPdfA.getPdfNumbersPages(pdfOriginal);
        aplicacionConversionService.saveAplicacionConversionInfo(info.getIdApplicacion(),
            numberPages);
      } else {
        retorno.setMime("application/pdf");
        DataSource dataSource = new ByteArrayDataSource(
            IOUtils.toByteArray(new FileInputStream(pdfOriginal)), "application/pdf");
        retorno.setContenido(new DataHandler(dataSource));
      }

      return retorno;
    } catch (NumberFormatException e) {
      logger.error("Error al convertir a PDF/A");
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (FileNotFoundException e) {
      logger.error("Error al convertir a PDF/A");
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (IOException e) {
      logger.error("Error al convertir a PDF/A");
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (PdfConversionException e) {
      logger.error("Error al convertir a PDF/A");
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    }
  }

  @Override
  public Boolean comprobarPDFA(ApplicationLogin info, DocumentoEntradaMtom docEntrada,
      Integer nivelCompilacion) throws InSideException {
    try {
      Boolean isPDFA =
          utilPdfA.isPDFA(IOUtils.toByteArray(docEntrada.getContenido().getInputStream()),
              docEntrada.getPassword(), nivelCompilacion);

      return isPDFA;
    } catch (IOException e) {
      logger.error("Error al comprobar a PDF/A");
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al comprobar a PDF/A: ", estadoInfo);
    }
  }
}
