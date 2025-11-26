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
      String flavourObjetivo = UtilsPdfA.mapearFlavourPDFBox(nivelCompilacion);

      if (!isPDFA) {
        logger.debug("El documento no es PDF/A, iniciando conversión...");

        try (PDDocument document = PDDocument.load(pdfOriginal, docEntrada.getPassword())) {

          // Obtener número de páginas para registro
          int numberPages = document.getNumberOfPages();
          aplicacionConversionService.saveAplicacionConversionInfo("EEUTILS-CARM", numberPages);

          // Determinar nivel de conformidad
          String part = "2";
          String conformance = "B";

          if (flavourObjetivo != null && flavourObjetivo.contains("1A")) {
            part = "1";
            conformance = "A";
          } else if (flavourObjetivo != null && flavourObjetivo.contains("1B")) {
            part = "1";
            conformance = "B";
          }

          // Crear esquema de metadatos XMP para PDF/A
          XMPMetadata xmp = XMPMetadata.createXMPMetadata();

          // Configurar información PDF/A
          PDFAIdentificationSchema pdfaid = new PDFAIdentificationSchema(xmp);
          pdfaid.setConformance(conformance);
          pdfaid.setPart(Integer.parseInt(part));
          xmp.addSchema(pdfaid);

          // Configurar Dublin Core
          DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
          dc.setTitle("Documento convertido a PDF/A");
          dc.addCreator("Sistema de Conversión");
          dc.addDate(Calendar.getInstance());

          // Serializar y establecer metadatos
          ByteArrayOutputStream xmpOut = new ByteArrayOutputStream();
          new XmpSerializer().serialize(xmp, xmpOut, true);

          PDMetadata metadata = new PDMetadata(document);
          metadata.importXMPMetadata(xmpOut.toByteArray());
          document.getDocumentCatalog().setMetadata(metadata);

          // Configurar OutputIntent para PDF/A (perfil de color sRGB)
          InputStream colorProfile = getClass().getResourceAsStream("/sRGB.icc");
          if (colorProfile == null) {
            // Alternativa: buscar en el classpath
            colorProfile =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("sRGB.icc");
          }

          if (colorProfile != null) {
            PDOutputIntent outputIntent = new PDOutputIntent(document, colorProfile);
            outputIntent.setInfo("sRGB IEC61966-2.1");
            outputIntent.setOutputCondition("sRGB IEC61966-2.1");
            outputIntent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
            outputIntent.setRegistryName("http://www.color.org");
            document.getDocumentCatalog().addOutputIntent(outputIntent);
          } else {
            logger.warn(
                "No se encontró el perfil de color sRGB.icc, la conversión puede no ser válida");
          }

          // Marcar para uso tagged (para PDF/A-1a, 2a, 3a)
          if ("A".equalsIgnoreCase(conformance)) {
            document.getDocumentCatalog().setMarkInfo(document.getDocumentCatalog().getMarkInfo());
          }

          // Guardar documento convertido en archivo temporal
          File pdfConvertido = File.createTempFile("pdfa_converted_", ".pdf");
          document.save(pdfConvertido);

          // Validar con PDFBox Preflight
          boolean conversionExitosa = UtilsPdfA.validarConPDFBox(pdfConvertido, flavourObjetivo);

          if (conversionExitosa) {
            logger.debug("Conversión a PDF/A exitosa y validada");
            retorno.setMime("application/pdf");
            DataSource dataSource = new ByteArrayDataSource(
                IOUtils.toByteArray(new FileInputStream(pdfConvertido)), "application/pdf");
            retorno.setContenido(new DataHandler(dataSource));

          } else {
            logger.error("La conversión no cumple con el estándar PDF/A requerido");
            // Obtener detalles de validación
            String detallesError =
                UtilsPdfA.obtenerDetallesValidacion(pdfConvertido, flavourObjetivo);
            EstadoInfo estadoInfo = new EstadoInfo();
            estadoInfo.setDescripcion(
                "La conversión a PDF/A no cumple con el estándar: " + detallesError);
            throw new InSideException("Error al convertir a PDF/A", estadoInfo);
          }

          // Limpiar archivo temporal
          pdfConvertido.delete();

        } catch (Exception e) {
          logger.error("Error procesando el PDF", e);
          EstadoInfo estadoInfo = new EstadoInfo();
          estadoInfo.setDescripcion("Error procesando el PDF: " + e.getMessage());
          throw new InSideException("Error al convertir a PDF/A", estadoInfo);
        }
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
