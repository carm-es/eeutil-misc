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
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import es.mpt.dsic.eeutil.operFirma.consumer.impl.ConsumerEeutilOperFirmaImpl;
import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.pdf.converter.HtmlConverter;
import es.mpt.dsic.inside.pdf.converter.PdfConverter;
import es.mpt.dsic.inside.pdf.converter.TcnToPdfConverter;
import es.mpt.dsic.inside.pdf.exception.PdfConversionException;
import es.mpt.dsic.inside.security.context.AplicacionContext;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.security.service.impl.EeutilAplicacionConversionService;
import es.mpt.dsic.inside.security.wss4j.CredentialUtil;
import es.mpt.dsic.inside.utils.file.FileUtil;
import es.mpt.dsic.inside.utils.xml.XMLUtil;
import es.mpt.dsic.inside.ws.service.EeUtilMiscUserNameTokenService;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.ContenidoInfo;
import es.mpt.dsic.inside.ws.service.model.DocumentoContenido;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import es.mpt.dsic.inside.ws.service.model.SalidaVisualizacion;
import es.mpt.dsic.inside.ws.service.model.TCNInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntrada;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalida;
import es.mpt.dsic.inside.ws.service.util.UtilFacturae;
import es.mpt.dsic.inside.ws.service.util.UtilPdfA;

import org.verapdf.pdfa.flavours.PDFAFlavour;



@Service("eeUtilMiscUserNameTokenService")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilMiscUserNameTokenService")
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.BARE, use = Use.LITERAL)
public class EeUtilMiscUserNameTokenServiceImpl implements EeUtilMiscUserNameTokenService {

  protected final static Log logger = LogFactory.getLog(EeUtilMiscUserNameTokenServiceImpl.class);

  @Autowired
  private UtilFacturae utilFacturae;

  @Autowired
  private AplicacionContext aplicacionContext;

  @Autowired
  private UtilPdfA utilPdfA;

  @Autowired
  private EeutilAplicacionConversionService aplicacionConversionService;

  @Autowired
  @Qualifier(value = "consumerEeutilOperFirma")
  private ConsumerEeutilOperFirmaImpl consumerEeutilOperFirmaImpl;

  @Autowired
  TcnToPdfConverter tcnToPdfConverter;

  @Autowired
  PdfConverter pdfConverter;

  @Autowired
  HtmlConverter htmlConverter;

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  CredentialUtil credentialUtil;

  @Override
  public byte[] postProcesarFirma(byte[] firma) throws InSideException {
    try {
      return consumerEeutilOperFirmaImpl
          .postProcesarFirma(credentialUtil.getCredentialEeutilUserToken(wsContext), firma);
    } catch (es.mpt.dsic.eeutil.operFirma.consumer.model.InSideException e) {
      throw new InSideException(e.getMessage(), new EstadoInfo(), e);
    }
  }

  @Override
  public ContenidoInfo convertirTCNAPdf(TCNInfo tcnInfo) throws InSideException {
    ContenidoInfo contenidoInfo = new ContenidoInfo();

    try {
      byte[] pdf = tcnToPdfConverter.convertTCNToPdfBytes(tcnInfo.getContenido().getContenido());
      contenidoInfo.setContenido(pdf);
      contenidoInfo.setTipoMIME("application/pdf");
    } catch (PdfConversionException e) {
      logger.error("Error en la conversi�n a PDF del TCN", e);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error convirtiendo TCN a PDF: ", estadoInfo);
    } catch (Throwable t) {
      logger.error("Error inesperado convirtiendo TCNaPDF", t);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error convirtiendo TCN a PDF: ", estadoInfo);
    }

    return contenidoInfo;
  }

  @Override
  public SalidaVisualizacion visualizarFacturae(byte[] factura, String version)
      throws InSideException {
    logger.debug("Inicio visualizarFacturae");

    SalidaVisualizacion facturaPost = new SalidaVisualizacion();
    DocumentoContenido contenido = new DocumentoContenido();

    File cpr = null;

    try {
      if (StringUtils.isEmpty(version)) {
        cpr = new File(
            EeutilApplicationDataConfig.CONFIG_PATH + File.separator + utilFacturae.getTemplate());
      } else {
        cpr = new File(EeutilApplicationDataConfig.CONFIG_PATH + File.separator + version
            + File.separator + utilFacturae.getTemplate());

      }
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer(new StreamSource(cpr));

      ByteArrayInputStream bin = new ByteArrayInputStream(factura);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      StreamSource sourceFactura = new StreamSource(bin);
      StreamResult result = new StreamResult(bos);
      transformer.transform(sourceFactura, result);

      contenido.setBytesDocumento(bos.toByteArray());
      contenido.setMimeDocumento("text/html");

      facturaPost.setDocumentoContenido(contenido);
    } catch (TransformerConfigurationException e) {
      logger.error("Error inesperado visualizando Facturae", e);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error visualizando Facturae: ", estadoInfo);
    } catch (TransformerFactoryConfigurationError e) {
      logger.error("Error inesperado visualizando Facturae", e);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error visualizando Facturae: ", estadoInfo);
    } catch (TransformerException e) {
      logger.error("Error inesperado visualizando Facturae", e);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error visualizando Facturae: ", estadoInfo);
    }

    return facturaPost;
  }

  @Override
  public SalidaVisualizacion visualizarFacturaePDF(byte[] factura, String version)
      throws InSideException {

    logger.debug("Inicio visualizarFacturaePDF");

    SalidaVisualizacion facturaPDFPost = new SalidaVisualizacion();

    DocumentoContenido contenido = new DocumentoContenido();

    try {
      // llamada al visualizarFacturae para obtener el html
      SalidaVisualizacion facturaHTML = visualizarFacturae(factura, version);

      // metodo que pasa de html a xhtml y desde xhtml a pdf
      byte[] facturaEPDF =
          htmlConverter.convertHTMLtoPDF(facturaHTML.getDocumentoContenido().getBytesDocumento());

      contenido.setBytesDocumento(facturaEPDF);
      contenido.setMimeDocumento("application/pdf");
      facturaPDFPost.setDocumentoContenido(contenido);


    } catch (TransformerFactoryConfigurationError e) {
      logger.error("Error inesperado visualizando Facturae", e);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error visualizando Facturae: ", estadoInfo);
    }

    return facturaPDFPost;
  }


  @Override
  public PdfSalida convertirPDFA(DocumentoEntrada docEntrada, Integer nivelCompilacion)
      throws InSideException {
    File pdfOriginal = null;
    try {
      logger.debug("convertirPDFA");
      logger.debug("mime:" + docEntrada.getMime());
      PdfSalida retorno = new PdfSalida();

      AppInfo appInfo = aplicacionContext.getAplicacionInfo();
      String ipOpenOffice = appInfo.getPropiedades().get("ip.openoffice");
      String portOpenOffice = appInfo.getPropiedades().get("port.openoffice");

      String filePathIn = FileUtil.createFilePath("PDFA_", docEntrada.getContenido());

      pdfOriginal = pdfConverter.convertir(ipOpenOffice, portOpenOffice, new File(filePathIn),
          docEntrada.getMime());


      // Determinar el flavour de PDF/A objetivo
      PDFAFlavour flavourObjetivo = UtilsPdfA.mapearFlavourVeraPDF(nivelCompilacion);

      // Verificar si ya es PDF/A usando veraPDF
      boolean isPDFA = UtilsPdfA.validarConVeraPDF(pdfOriginal, flavourObjetivo);

      if (!isPDFA) {
        logger.debug("El documento no es PDF/A, iniciando conversión...");

        try (PDDocument document = PDDocument.load(pdfOriginal, docEntrada.getPassword())) {

          // Obtener número de páginas para registro
          int numberPages = document.getNumberOfPages();
          aplicacionConversionService.saveAplicacionConversionInfo("EEUTILS-CARM", numberPages);

          // Determinar nivel de conformidad
          String part = "2";
          String conformance = "B";

          if (flavourObjetivo != null) {
            part = String.valueOf(flavourObjetivo.getPart().getPartNumber());
            conformance = flavourObjetivo.getLevel().getCode();
          }

          // Crear esquema de metadatos XMP para PDF/A
          XMPMetadata xmp = XMPMetadata.createXMPMetadata();

          // Configurar información PDF/A
          PDFAIdentificationSchema pdfaid = xmp.createAndAddPDFAIdentificationSchema();
          pdfaid.setConformance(conformance);
          pdfaid.setPart(Integer.parseInt(part));

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

          // Validar con veraPDF
          boolean conversionExitosa = UtilsPdfA.validarConVeraPDF(pdfConvertido, flavourObjetivo);

          if (conversionExitosa) {
            logger.debug("Conversión a PDF/A exitosa y validada");
            retorno.setMime("application/pdf");
            retorno.setContenido(IOUtils.toByteArray(new FileInputStream(pdfConvertido)));

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
        retorno.setContenido(IOUtils.toByteArray(new FileInputStream(pdfOriginal)));
      }

      return retorno;
    } catch (NumberFormatException e) {
      logger.error("Error al convertir a PDF/A:" + e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (FileNotFoundException e) {
      logger.error("Error al convertir a PDF/A:" + e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (IOException e) {
      logger.error("Error al convertir a PDF/A:" + e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } catch (PdfConversionException e) {
      logger.error("Error al convertir a PDF/A:" + e.getMessage());
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new InSideException("Error al convertir a PDF/A: ", estadoInfo);
    } finally {
      try {
        FileUtils.forceDelete(pdfOriginal);
      } catch (IOException e) {
        logger.warn("Error al eliminar fichero temporal:" + pdfOriginal.getAbsolutePath());
      }
    }
  }

  @Override
  public Boolean comprobarPDFA(DocumentoEntrada docEntrada, Integer nivelCompilacion)
      throws InSideException {
    return utilPdfA.isPDFA(docEntrada.getContenido(), docEntrada.getPassword(), nivelCompilacion);
  }


}
