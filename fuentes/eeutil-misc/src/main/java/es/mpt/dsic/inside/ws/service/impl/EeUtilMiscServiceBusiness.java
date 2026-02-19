package es.mpt.dsic.inside.ws.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.MDC;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.lowagie.text.exceptions.BadPasswordException;
import com.lowagie.text.pdf.PdfReader;

import es.mpt.dsic.eeutil.configure.ConfigureRestInfo;
import es.mpt.dsic.eeutil.model.PdfConformance;
import es.mpt.dsic.eeutil.operFirma.consumer.impl.ConsumerEeutilOperFirmaImpl;
import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.pdf.converter.PdfConverter;
import es.mpt.dsic.inside.reflection.MapUtil;
import es.mpt.dsic.inside.reflection.UtilReflection;
import es.mpt.dsic.inside.security.service.impl.EeutilAplicacionConversionService;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.file.FileUtil;
import es.mpt.dsic.inside.utils.io.IOUtil;
import es.mpt.dsic.inside.utils.rest.ClientSecurePdfaTools;
import es.mpt.dsic.inside.utils.rest.RestIgaeServiceC;
import es.mpt.dsic.inside.utils.string.StringUtil;
import es.mpt.dsic.inside.utils.xmlsecurity.XMLSeguridadFactoria;
import es.mpt.dsic.inside.ws.service.model.ContenidoInfo;
import es.mpt.dsic.inside.ws.service.model.DocumentoContenido;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import es.mpt.dsic.inside.ws.service.model.SalidaVisualizacion;
import es.mpt.dsic.inside.ws.service.model.TCNInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntrada;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntradaMtom;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalida;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalidaMtom;
import es.mpt.dsic.inside.ws.service.util.UtilFacturae;
import es.mpt.dsic.inside.ws.service.util.UtilPdfA;

@Component
public class EeUtilMiscServiceBusiness {

  private static final String ERROR_AL_CONVERTIR_A_PDF_A_MSG = "Error al convertir a PDF/A: ";

  private static final String ERROR_VISUALIZANDO_FACTURAE_MSG = "Error visualizando Facturae: ";

  protected static final Log logger = LogFactory.getLog(EeUtilMiscServiceBusiness.class);

  @Autowired
  @Qualifier(value = "consumerEeutilOperFirma")
  private ConsumerEeutilOperFirmaImpl consumerEeutilOperFirmaImpl;

  @Autowired
  private UtilFacturae utilFacturae;

  @Autowired
  private UtilPdfA utilPdfA;

  @Autowired
  PdfConverter pdfConverter;

  @Autowired
  private RestIgaeServiceC restIgaeServiceC;

  @Autowired
  private ClientSecurePdfaTools clientSecurePdfaTools;

  @Autowired
  private EeutilAplicacionConversionService aplicacionConversionService;

  public byte[] postProcesarFirma(String idApp, String passw, byte[] firma) throws EeutilException {
    try {
      return consumerEeutilOperFirmaImpl.postProcesarFirma(idApp, passw, firma);
    } catch (EeutilException e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }

  public ContenidoInfo convertirTCNAPdf(TCNInfo tcnInfo) throws EeutilException {
    ContenidoInfo contenidoInfo = new ContenidoInfo();

    try {
      // byte[] pdf =
      // tcnToPdfConverter.convertTCNToPdfBytes(tcnInfo.getContenido().getContenido());
      byte[] pdf = null;

      byte[] bContenido = tcnInfo.getContenido().getContenido();

      String json = Base64.encodeBase64String(bContenido);

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;

      try {
        response = restIgaeServiceC.getResponseClientIgae(httpClient,
            ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + "/api/convertirTCN", json,
            ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
            ConfigureRestInfo.getInstance().getTokenValueRestIgae());
        HttpEntity entityResponse = response.getEntity();
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new EeutilException("ERROR AL ACCEDER AL SERVICIO DE IGAE "
              + ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + " status:"
              + response.getStatusLine().getStatusCode());
        } else {
          pdf = Base64.decodeBase64(IOUtils.toByteArray(entityResponse.getContent()));
        }

      } catch (Exception e) {
        throw new EeutilException(e.getMessage(), e);
      } finally {
        restIgaeServiceC.cerrarRequestResponse(httpClient, response);
      }

      contenidoInfo.setContenido(pdf);
      contenidoInfo.setTipoMIME("application/pdf");
    } catch (EeutilException e) {
      // logger.error("Error en la conversi�n a PDF del TCN", e);
      // EstadoInfo estadoInfo = new EstadoInfo();
      throw new EeutilException("Error convirtiendo TCN a PDF: " + e.getMessage(), e);
    } catch (Exception t) {
      // logger.error("Error inesperado convirtiendo TCNaPDF", t);
      EstadoInfo estadoInfo = new EstadoInfo();
      throw new EeutilException("Error convirtiendo TCN a PDF: " + t.getMessage(), t);
    }

    return contenidoInfo;
  }

  public SalidaVisualizacion visualizarFacturae(byte[] factura, String version)
      throws EeutilException {
    logger.debug("Inicio visualizarFacturae");

    SalidaVisualizacion facturaPost = new SalidaVisualizacion();
    DocumentoContenido contenido = new DocumentoContenido();
    File cpr = null;
    try (ByteArrayInputStream bin = new ByteArrayInputStream(factura);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
      if (StringUtils.isEmpty(version)) {
        cpr = new File(EeutilApplicationDataConfig.CONFIG_PATH + File.separator
            + utilFacturae.getTemplateDefault());
        if (!cpr.exists()) {
          throw new FileNotFoundException(
              "No se encuentra el fichero " + EeutilApplicationDataConfig.CONFIG_PATH
                  + File.separator + utilFacturae.getTemplateDefault());
        }
      } else {
        cpr = new File(
            EeutilApplicationDataConfig.CONFIG_PATH + File.separator + utilFacturae.getDirectorio()
                + File.separator + version + File.separator + utilFacturae.getTemplate());
        if (!cpr.exists()) {
          throw new FileNotFoundException("No se encuentra el fichero para la version " + version
              + ":" + EeutilApplicationDataConfig.CONFIG_PATH + File.separator + version
              + File.separator + utilFacturae.getTemplate());
        }
      }

      TransformerFactory tFactory = TransformerFactory.newInstance();
      // habilitamos seguridad para evitar problemas de hijacking.
      // deshabilitamos para evitar validaciones dtd
      // tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      // to be compliant, prohibit the use of all protocols by external entities:
      // tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      // tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      XMLSeguridadFactoria.getInstance().setPreventAttackExternalTransformer(tFactory);
      Transformer transformer = tFactory.newTransformer(new StreamSource(cpr));

      StreamSource sourceFactura = new StreamSource(bin);
      StreamResult result = new StreamResult(bos);
      transformer.transform(sourceFactura, result);

      contenido.setBytesDocumento(bos.toByteArray());
      contenido.setMimeDocumento("text/html");

      if (bos.toByteArray().length == 0) {
        visualizarFacturaePDFMDC(factura, version);
        logger.error(
            "No se ha podido realizar la transformacion de la FacturaE. El tama�o de la respuesta es 0");
      }

      facturaPost.setDocumentoContenido(contenido);
    } catch (TransformerException | IOException e) {
      // logger.error("Error inesperado visualizando Facturae", e);
      throw new EeutilException(ERROR_VISUALIZANDO_FACTURAE_MSG + e.getMessage(), e);
    }

    return facturaPost;
  }

  public SalidaVisualizacion visualizarFacturaePDF(byte[] factura, String version)
      throws EeutilException {

    logger.debug("Inicio visualizarFacturaePDF");

    SalidaVisualizacion facturaPDFPost = new SalidaVisualizacion();

    DocumentoContenido contenido = new DocumentoContenido();

    try {
      // llamada al visualizarFacturae para obtener el html
      SalidaVisualizacion facturaHTML = visualizarFacturae(factura, version);

      // metodo que pasa de html a xhtml y desde xhtml a pdf
      // byte[] facturaEPDF = htmlConverter
      // .convertHTMLtoPDF(facturaHTML.getDocumentoContenido().getBytesDocumento());

      byte[] facturaEPDF = null;

      byte[] bContenido =
          IOUtil.getBytesFromObject(facturaHTML.getDocumentoContenido().getBytesDocumento());

      String json = Base64.encodeBase64String(bContenido);

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;

      try {

        response = restIgaeServiceC.getResponseClientIgae(httpClient,
            ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + "/api/convertirPDFFacturae",
            json, ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
            ConfigureRestInfo.getInstance().getTokenValueRestIgae());
        HttpEntity entityResponse = response.getEntity();
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new EeutilException("ERROR AL ACCEDER AL SERVICIO DE IGAE "
              + ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + " status:"
              + response.getStatusLine().getStatusCode());
        } else {
          facturaEPDF = Base64.decodeBase64(IOUtils.toByteArray(entityResponse.getContent()));
        }

      } catch (Exception e) {
        throw new EeutilException(e.getMessage(), e);
      } finally {
        restIgaeServiceC.cerrarRequestResponse(httpClient, response);
      }

      contenido.setBytesDocumento(facturaEPDF);
      contenido.setMimeDocumento("application/pdf");
      facturaPDFPost.setDocumentoContenido(contenido);

    } catch (EeutilException e) {
      // logger.error("Error inesperado visualizando Facturae", e);
      throw new EeutilException(ERROR_VISUALIZANDO_FACTURAE_MSG + e.getMessage(), e);
    } catch (Exception e) {
      // logger.error("Error inesperado visualizando Facturae", e);

      throw new EeutilException(ERROR_VISUALIZANDO_FACTURAE_MSG + e.getMessage(), e);
    }

    return facturaPDFPost;
  }

  public PdfSalida convertirPDFA(String idApp, String ipOpenOffice, String portOpenOffice,
      DocumentoEntrada docEntrada, Integer nivelCompilacion, String password)
      throws EeutilException {

    // si no esta encriptado seteamos el campo a null
    if (!isEncrypted(docEntrada.getContenido(), docEntrada.getPassword())) {
      docEntrada.setPassword(null);
    }


    File pdfOriginal = null;
    File fileIn = null;
    PdfSalida retorno = null;
    Path pathIn = null;
    try {
      logger.debug("convertirPDFA");
      logger.debug("mime:" + docEntrada.getMime());

      String filePathIn = FileUtil.createFilePath("PDFA_", docEntrada.getContenido());
      pathIn = Paths.get(filePathIn);

      fileIn = new File(pathIn.toString());

      pdfOriginal = pdfConverter.convertir(ipOpenOffice, portOpenOffice, fileIn,
          docEntrada.getMime(), idApp, ConfigureRestInfo.getInstance().getBaseUrlRestIgae(),
          ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
          ConfigureRestInfo.getInstance().getTokenValueRestIgae());
      int numberPages = utilPdfA.getPdfNumbersPages(pdfOriginal, password);
      // insercion en bbdd
      aplicacionConversionService.saveAplicacionConversionInfo(idApp, numberPages);

      try/* (FileInputStream fIOPdfOriginal1 = new FileInputStream(pdfOriginal)) */
      {
        byte[] bytePDF = Files.readAllBytes(pdfOriginal.toPath());
        // retorno =
        // utilPdfA.convertPdfa(IOUtils.toByteArray(fIOPdfOriginal1),password,nivelCompilacion);

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        try {
          PdfConformance pdfConformance = utilPdfA.convertCodeToPartLevel(nivelCompilacion);

          // si la version es una de estas
          if (pdfConformance != null && ("1.0".equals(pdfConformance.getVersion())
              || "1.1".equals(pdfConformance.getVersion())
              || "1.2".equals(pdfConformance.getVersion())
              || "1.3".equals(pdfConformance.getVersion())
              || "1.4".equals(pdfConformance.getVersion())
              || "1.5".equals(pdfConformance.getVersion())
              || "1.6".equals(pdfConformance.getVersion())
              || "1.7".equals(pdfConformance.getVersion()))) {
            throw new EeutilException(
                "Error, el parametro de conversion no se corresponde con un nivel de conversion estandar PDFA.");
          }

          String jsonConvertirPDFA = clientSecurePdfaTools
              .toJsonConvertirPDFA(pdfConformance.getLevel(), bytePDF, password);
          String uuidTraza = UUID.randomUUID().toString();
          response = clientSecurePdfaTools.getResponseClientPdfatools(httpClient,
              ConfigureRestInfo.getInstance().getBaseUrlPdfatools() + "/api/convertirPDFA",
              ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
              ConfigureRestInfo.getInstance().getTokenValueRestIgae(), jsonConvertirPDFA,
              bytePDF != null ? bytePDF.length : 0, uuidTraza);
          HttpEntity entityResponse = response.getEntity();
          if (response.getStatusLine().getStatusCode() == 500) {
            throw new EeutilException("UUID traza:" + uuidTraza
                + ".Error, no se ha podido realizar la conversion del documento pdfaTools PDFA. status:"
                + response.getStatusLine().getStatusCode() + " "
                + EntityUtils.toString(entityResponse));
          } else if (response.getStatusLine().getStatusCode() != 200) {
            throw new EeutilException("ERROR AL ACCEDER AL SERVICIO PDFTOOLS "
                + ConfigureRestInfo.getInstance().getBaseUrlPdfatools() + "/api/convertirPDFA "
                + " status:" + response.getStatusLine().getStatusCode());
          } else {

            if (entityResponse != null) {
              String resultado = EntityUtils.toString(entityResponse);
              JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultado);

              String sResultado = (String) jsonObject.get("contenido");
              retorno = new PdfSalida();
              retorno.setContenido(Base64.decodeBase64(sResultado));
              retorno.setMime("application/pdf");

              if (sResultado == null) {
                throw new SAXException("Error al devolver el resultado de conversion a PDFA");
              }

            } else {
              throw new SAXException("Error al devolver el resultado de conversion a PDFA");
            }

          }

        } catch (Exception e) {
          throw new EeutilException(e.getMessage(), e);
        } finally {
          clientSecurePdfaTools.cerrarRequestResponse(httpClient, response);
        }

      } catch (FileNotFoundException e) {
        throw new EeutilException(e.getMessage(), e);
      } catch (IOException e) {
        throw new EeutilException(e.getMessage(), e);
      }
    } catch (NumberFormatException | IOException | EeutilException e) {
      // logger.error("Error al convertir a PDF/A:" + e.getMessage());
      throw new EeutilException(ERROR_AL_CONVERTIR_A_PDF_A_MSG + e.getMessage(), e);
    } catch (Exception e) {
      // logger.error("Error al convertir a PDF/A:" + e.getMessage());
      throw new EeutilException(ERROR_AL_CONVERTIR_A_PDF_A_MSG + e.getMessage(), e);
    }

    finally {
      if (Files.exists(pathIn)) {
        try {
          Files.delete(pathIn);
        } catch (IOException e) {
          throw new EeutilException(ERROR_AL_CONVERTIR_A_PDF_A_MSG + e.getMessage(), e);
        }
      }
    }

    return retorno;
  }

  public PdfSalidaMtom convertirPDFAMtom(String idApp, String ipOpenOffice, String portOpenOffice,
      DocumentoEntradaMtom docEntrada, Integer nivelCompilacion, String password)
      throws EeutilException {

    File pdfOriginal = null;
    File fileIn = null;
    PdfSalidaMtom retorno = null;
    Path pathIn = null;
    try (InputStream isIn = docEntrada.getContenido().getInputStream()) {
      logger.debug("convertirPDFAMtom");
      logger.debug("mime:" + docEntrada.getMime());

      String filePathIn = FileUtil.createFilePath("PDFA_", IOUtils.toByteArray(isIn));
      pathIn = Paths.get(filePathIn);

      fileIn = new File(pathIn.toString());

      pdfOriginal = pdfConverter.convertir(ipOpenOffice, portOpenOffice, fileIn,
          docEntrada.getMime(), idApp, ConfigureRestInfo.getInstance().getBaseUrlRestIgae(),
          ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
          ConfigureRestInfo.getInstance().getTokenValueRestIgae());

      int numberPages = utilPdfA.getPdfNumbersPages(pdfOriginal, password);
      // insercion en bbdd
      aplicacionConversionService.saveAplicacionConversionInfo(idApp, numberPages);

      try/* (FileInputStream fIOPdfOriginal1 = new FileInputStream(pdfOriginal)) */
      {

        // retorno =
        // utilPdfA.convertPdfa(IOUtils.toByteArray(fIOPdfOriginal1),password,nivelCompilacion);

        byte[] bytePDF = Files.readAllBytes(pdfOriginal.toPath());


        // si no esta encriptado seteamos el campo a null
        if (!isEncrypted(bytePDF, docEntrada.getPassword())) {
          docEntrada.setPassword(null);
        }


        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        try {
          PdfConformance pdfConformance = utilPdfA.convertCodeToPartLevel(nivelCompilacion);
          // si la version es una de estas
          if (pdfConformance != null && ("1.0".equals(pdfConformance.getVersion())
              || "1.1".equals(pdfConformance.getVersion())
              || "1.2".equals(pdfConformance.getVersion())
              || "1.3".equals(pdfConformance.getVersion())
              || "1.4".equals(pdfConformance.getVersion())
              || "1.5".equals(pdfConformance.getVersion())
              || "1.6".equals(pdfConformance.getVersion())
              || "1.7".equals(pdfConformance.getVersion()))) {
            throw new EeutilException(
                "Error, el parametro de conversion no se corresponde con un nivel de conversion estandar PDFA.");
          }
          String jsonConvertirPDFA = clientSecurePdfaTools
              .toJsonConvertirPDFA(pdfConformance.getLevel(), bytePDF, password);
          String uuidTraza = UUID.randomUUID().toString();
          response = clientSecurePdfaTools.getResponseClientPdfatools(httpClient,
              ConfigureRestInfo.getInstance().getBaseUrlPdfatools() + "/api/convertirPDFA",
              ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
              ConfigureRestInfo.getInstance().getTokenValueRestIgae(), jsonConvertirPDFA,
              bytePDF != null ? bytePDF.length : 0, uuidTraza);
          HttpEntity entityResponse = response.getEntity();
          if (response.getStatusLine().getStatusCode() == 500) {
            throw new EeutilException("UUID traza:" + uuidTraza
                + ".Error, no se ha podido realizar la conversion del documento pdfaTools PDFA. status:"
                + response.getStatusLine().getStatusCode() + " "
                + EntityUtils.toString(entityResponse));
          } else if (response.getStatusLine().getStatusCode() != 200) {
            throw new EeutilException("ERROR AL ACCEDER AL SERVICIO PDFTOOLS "
                + ConfigureRestInfo.getInstance().getBaseUrlPdfatools() + "/api/convertirPDFA "
                + " status:" + response.getStatusLine().getStatusCode());
          } else {
            if (entityResponse != null) {
              String resultado = EntityUtils.toString(entityResponse);
              JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultado);

              String sResultado = (String) jsonObject.get("contenido");
              retorno = new PdfSalidaMtom();
              retorno.setContenido(Base64.decodeBase64(sResultado));
              retorno.setMime("application/pdf");

              if (sResultado == null) {
                throw new SAXException("Error al devolver el resultado de conversion a PDFA");
              }

            } else {
              throw new SAXException("Error al devolver el resultado de conversion a PDFA");
            }
          }

        } catch (Exception e) {
          throw new EeutilException(e.getMessage(), e);
        } finally {
          clientSecurePdfaTools.cerrarRequestResponse(httpClient, response);
        }

      } catch (FileNotFoundException e) {
        throw new EeutilException(e.getMessage(), e);
      } catch (IOException e) {
        throw new EeutilException(e.getMessage(), e);
      }
    } catch (IOException e) {
      throw new EeutilException(ERROR_AL_CONVERTIR_A_PDF_A_MSG + e.getMessage(), e);
    } finally {
      if (Files.exists(pathIn)) {
        try {
          Files.delete(pathIn);
        } catch (IOException e) {
          throw new EeutilException(ERROR_AL_CONVERTIR_A_PDF_A_MSG + e.getMessage(), e);
        }
      }
    }

    return retorno;

  }

  private Boolean comprobarPDFA(byte[] contenido, String password, Integer nivelCompilacion)
      throws EeutilException {
    Boolean resultado = false;

    try {
      // return utilPdfA.isPDFA(docEntrada.getContenido(), docEntrada.getPassword(),
      // nivelCompilacion);

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;
      try {
        PdfConformance pdfConformance = utilPdfA.convertCodeToPartLevel(nivelCompilacion);

        // si la version es una de estas
        if (pdfConformance != null && ("1.0".equals(pdfConformance.getVersion())
            || "1.1".equals(pdfConformance.getVersion())
            || "1.2".equals(pdfConformance.getVersion())
            || "1.3".equals(pdfConformance.getVersion())
            || "1.4".equals(pdfConformance.getVersion())
            || "1.5".equals(pdfConformance.getVersion())
            || "1.6".equals(pdfConformance.getVersion())
            || "1.7".equals(pdfConformance.getVersion()))) {
          resultado = new Boolean(false);
          // devolvemos inmediatamente false puesto que esos niveles de compilacion no se
          // corresponden
          // a niveles pdfa
          return resultado;
        }


        String jsonValidarConformanceLevel = clientSecurePdfaTools.toJsonValidarPdfaCompilanceLevel(
            contenido, pdfConformance.getLevel(), pdfConformance.getCompilance(), password);
        String uuidTraza = UUID.randomUUID().toString();
        response = clientSecurePdfaTools.getResponseClientPdfatools(httpClient,
            ConfigureRestInfo.getInstance().getBaseUrlPdfatools()
                + "/api/validarPDFACompilanceLevel",
            ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
            ConfigureRestInfo.getInstance().getTokenValueRestIgae(), jsonValidarConformanceLevel,
            contenido != null ? contenido.length : 0, uuidTraza);
        HttpEntity entityResponse = response.getEntity();
        if (response.getStatusLine().getStatusCode() == 500) {
          throw new EeutilException("UUID traza:" + uuidTraza
              + ".Error, no se ha podido realizar la validacion del documento pdfaTools PDFA. status:"
              + response.getStatusLine().getStatusCode() + " "
              + EntityUtils.toString(entityResponse));
        } else if (response.getStatusLine().getStatusCode() != 200) {
          throw new EeutilException("ERROR AL ACCEDER AL SERVICIO PDFTOOLS "
              + ConfigureRestInfo.getInstance().getBaseUrlPdfatools()
              + "/api/validarPDFACompilanceLevel " + " status:"
              + response.getStatusLine().getStatusCode());
        } else {
          if (entityResponse != null) {
            String resEntity = EntityUtils.toString(entityResponse);
            resultado = Boolean.parseBoolean(resEntity);
            // JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultado);

            // String sResultado = (String) jsonObject.get("contenido");
            // retorno = new PdfSalidaMtom();
            // retorno.setContenido(Base64.decodeBase64(sResultado));
            // retorno.setMime("application/pdf");

            if (resEntity == null) {
              throw new SAXException("Error al devolver el resultado de conversion a PDFA");
            }

          } else {
            throw new SAXException("Error al devolver el resultado de conversion a PDFA");
          }
        }

      } catch (Exception e) {
        throw new EeutilException(e.getMessage(), e);
      } finally {
        clientSecurePdfaTools.cerrarRequestResponse(httpClient, response);
      }

    } catch (EeutilException | IOException e) {
      // logger.error("Error al comprobar a PDF/A");
      throw new EeutilException("Error al comprobar a PDF/A: " + e.getMessage(), e);
    }

    return resultado;
  }

  public Boolean comprobarPDFA(DocumentoEntrada docEntrada, Integer nivelCompilacion)
      throws EeutilException {

    // si no esta encriptado seteamos el campo a null
    if (!isEncrypted(docEntrada.getContenido(), docEntrada.getPassword())) {
      docEntrada.setPassword(null);
    }

    return comprobarPDFA(docEntrada.getContenido(), docEntrada.getPassword(), nivelCompilacion);
  }

  public Boolean comprobarPDFAMtom(DocumentoEntradaMtom docEntrada, Integer nivelCompilacion)
      throws EeutilException {

    byte[] byteContenido = null;
    try {

      byteContenido = IOUtils.toByteArray(docEntrada.getContenido().getInputStream());

    } catch (IOException e) {
      throw new EeutilException(e.getMessage(), e);
    }

    // si no esta encriptado seteamos el campo a null
    if (!isEncrypted(byteContenido, docEntrada.getPassword())) {
      docEntrada.setPassword(null);
    }

    return comprobarPDFA(byteContenido, docEntrada.getPassword(), nivelCompilacion);
  }

  /**
   * @param factura
   * @param version
   */
  private void visualizarFacturaePDFMDC(byte[] factura, String version) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"factura", "version"};
      objs[0] = factura;
      objs[1] = version;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put("ExtraParaM", resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  public boolean isEncrypted(byte[] contenidoPDF, String password) throws EeutilException {
    boolean isEncrypted = false;

    if (!StringUtil.esVacioOrNull(password)) {

      try (PdfReader reader = new PdfReader(contenidoPDF, password.getBytes());) {
        isEncrypted = reader.isEncrypted();

      } catch (BadPasswordException e) {
        throw new EeutilException(e.getMessage(), e);
      } catch (IOException e) {
        throw new EeutilException(e.getMessage(), e);
      }

    } else {
      isEncrypted = false;
    }

    return isEncrypted;
  }

}
