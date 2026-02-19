package es.mpt.dsic.inside.validacioneni.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.MDC;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.aapp.eeutil.eeutilrestigae.model.MDCWrapper;
import es.gob.aapp.eeutil.eeutilrestigae.model.SchemaEniModel;
import es.mpt.dsic.eeutil.configure.ConfigureRestInfo;
import es.mpt.dsic.eeutil.exception.EEUtilMiscException;
import es.mpt.dsic.eeutil.operFirma.consumer.impl.ConsumerEeutilOperFirmaImpl;
import es.mpt.dsic.eeutil.operFirma.consumer.model.DatosFirmados;
import es.mpt.dsic.eeutil.operFirma.consumer.model.ResultadoValidacionFirmaInfo;
import es.mpt.dsic.eeutil.operFirma.consumer.model.ResultadoValidacionInfo;
import es.mpt.dsic.eeutil.sia.consumer.impl.ConsumidorSIA;
import es.mpt.dsic.inside.dao.EeutilDao;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.rest.RestIgaeServiceC;
import es.mpt.dsic.inside.utils.xml.XMLUtil;
import es.mpt.dsic.inside.utils.xmlsecurity.XMLSeguridadFactoria;
import es.mpt.dsic.inside.validacioneni.EeutilEniValidationENIService;
import es.mpt.dsic.inside.validacioneni.FirmaDatos;
import es.mpt.dsic.inside.validacioneni.ResultadoValidacionDocENI;
import es.mpt.dsic.inside.ws.service.codigosrespuestavalidacion.DocumentoENICodigosRespuestaValidacion;
import es.mpt.dsic.inside.ws.service.codigosrespuestavalidacion.ExpedienteENICodigosRespuestaValidacion;
import es.mpt.dsic.inside.ws.service.codigosrespuestavalidacion.GlobalValidacionENICodigosRespuestaValidacion;
import es.mpt.dsic.inside.ws.service.model.Detalle;
import es.mpt.dsic.inside.ws.service.util.TipoFirmaException;
import es.mpt.dsic.loadTables.model.UnidadOrganica;

public class EeutilEniValidationENIServiceImpl implements EeutilEniValidationENIService {

  private static final String CUSTOM_SEPARATOR = " -- (";

  private static final String DETALLE_CONSTANT = " -- detalle: ";

  private static final String ESTADO_CONSTANT = " -- Estado: ";

  protected static final Log logger = LogFactory.getLog(EeutilEniValidationENIServiceImpl.class);

  @Autowired
  @Qualifier(value = "consumerEeutilOperFirma")
  private ConsumerEeutilOperFirmaImpl consumerEeutilOperFirmaImpl;

  @Autowired
  private ConsumidorSIA consumidorSIA;

  @Autowired
  private EeutilDao eeutilDao;

  @Autowired
  private RestIgaeServiceC restIgaeServiceC;

  private String schemasDir;
  private String versionENI;

  private Source[] schemasSourcesDefault;

  // nombre del identificador del documentoEni o del expedienteEni
  private String identificadorObjeto;



  // XPATH documentoEni
  private static final String XPATH_RUTA_IDENTIFICADOR_DOC = "documento/metadatos/Identificador";
  private static final String XPATH_RUTA_ORGANOS_DOC = "documento/metadatos/Organo";
  private static final String XPATH_RUTA_TIPO_FIRMA_DOC = "documento/firmas/firma/TipoFirma";
  private static final String XPATH_RUTA_REFFIRMA =
      "documento/firmas/firma[%d]/ContenidoFirma/FirmaConCertificado/ReferenciaFirma";
  private static final String XPATH_RUTA_MULTI =
      "documento/firmas/firma[%d]/ContenidoFirma/FirmaConCertificado/FirmaBase64";

  // Tag para coger datos de firma Documentos
  private static final String XPATH_RUTA_TF03_XADES_ENVELOPED = "documento/contenido/DatosXML";
  private static final String XPATH_RUTA_TF02_XADES_DETACHED = "documento/contenido/DatosXML";
  private static final String XPATH_RUTA_TF02_XADES_DETACHED_EN_FIRMABASE64 =
      "documento/firmas/firma/ContenidoFirma/FirmaConCertificado/FirmaBase64";

  private static final String XPATH_RUTA_TF05_CADES_ATTACHED =
      "documento/firmas/firma/ContenidoFirma/FirmaConCertificado/FirmaBase64";
  private static final String XPATH_RUTA_TF05_CADES_ATTACHED_REF_VALORBINARIO =
      "documento/contenido/ValorBinario";
  private static final String XPATH_RUTA_TF05_CADES_ATTACHED_IDFIRMA =
      "documento/firmas/firma[%d]/@Id";
  private static final String XPATH_RUTA_TF04_CADES_DETACHED =
      "documento/firmas/firma/ContenidoFirma/FirmaConCertificado/FirmaBase64";
  private static final String XPATH_RUTA_TF04_CADES_DETACHED_DOC =
      "documento/contenido/ValorBinario";
  private static final String XPATH_RUTA_TFO6_PADES = "documento/contenido/ValorBinario";
  private static final String XPATH_RUTA_TF07_XADES_MANIFEST =
      "documento/firmas/firma/ContenidoFirma/FirmaConCertificado/FirmaBase64";
  private static final String XPATH_RUTA_FIRMA = "/documento/firmas/firma";

  // XPATH expedienteEni
  private final String XPATH_RUTA_IDENTIFICADOR_EXP = "expediente/metadatosExp/Identificador";
  private final String XPATH_RUTA_ORGANOS_EXP = "expediente/metadatosExp/Organo";
  private final String XPATH_RUTA_CLASIFICACION_EXP = "expediente/metadatosExp/Clasificacion";
  private final String XPATH_RUTA_FECHA_APERTURA_EXP =
      "expediente/metadatosExp/FechaAperturaExpediente";
  private final String XPATH_RUTA_FIRMA_SIGNATURE_EXP =
      "expediente/indice/firmas/firma/ContenidoFirma/FirmaConCertificado/Signature";
  private final String XPATH_RUTA_FIRMA_BASE64_EXP =
      "expediente/indice/firmas/firma/ContenidoFirma/FirmaConCertificado/FirmaBase64";
  private final String XPATH_RUTA_TIPO_FIRMA_EXP = "expediente/indice/firmas/firma/TipoFirma";

  // Tipos de firma
  private final String TIPOFIRMA_TF_01 = "TF01";
  private final String TIPOFIRMA_TF_02 = "TF02";
  private final String TIPOFIRMA_TF_03 = "TF03";
  private final String TIPOFIRMA_TF_04 = "TF04";
  private final String TIPOFIRMA_TF_05 = "TF05";
  private final String TIPOFIRMA_TF_06 = "TF06";

  public ConsumidorSIA getConsumidorSIA() {
    return consumidorSIA;
  }

  public void setConsumidorSIA(ConsumidorSIA consumidorSIA) {
    this.consumidorSIA = consumidorSIA;
  }

  public String getVersionENI() {
    return versionENI;
  }

  public void setVersionENI(String versionENI) {
    this.versionENI = versionENI;
  }

  public String getSchemasDir() {
    return schemasDir;
  }

  @Required
  public void setSchemasDir(String schemasDir) {
    this.schemasDir = schemasDir;
  }

  @PostConstruct
  public void configureService() {
    logger.info(
        "Leyendo esquemas del directorio: " + schemasDir + " concatenando la version del ENI");

    schemasSourcesDefault = getSchemasByVesion(versionENI);

    if (schemasSourcesDefault.length == 0) {
      logger.error("No se han podido cargar los schemas del directorio: " + schemasDir);
    }
  }

  private Source[] getSchemasByVesion(String version) {
    return XMLUtil.getSchemasSources(schemasDir + File.separator + version);
  }

  @Override
  public void setIdentificadorExpedienteXML(String xml) throws EeutilException {
    try {
      identificadorObjeto = XMLUtil.getvalorNodoDatosXML(xml, this.XPATH_RUTA_IDENTIFICADOR_DOC);
    } catch (IOException | ParserConfigurationException | XPathExpressionException
        | SAXException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }

  /************************************************************************************************************************************
   * ************************************ VALIDACIONES DOCUMENTOENI
   * ******************************************************************
   * **********************************************************************************************************************************
   */

  /*
   * Validar shema de documentoeni (non-Javadoc)
   * 
   * @see es.mpt.dsic.inside.validacionENI.EeutilEniValidationENIService#
   * validarShemaDocumentoEniFile(byte[])
   */
  @Override
  public Detalle validarShemaDocumentoEniFile(String dataXml, String version) {

    try {
      identificadorObjeto =
          XMLUtil.getvalorNodoDatosXML(dataXml, this.XPATH_RUTA_IDENTIFICADOR_DOC);
      validarSchema(dataXml.getBytes(XMLUtil.UTF8_CHARSET), version); // por acentos

    } catch (SAXException e) {

      return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_E_999_CODIGO,
          DocumentoENICodigosRespuestaValidacion.D_E_999_DETALLE + ". " + e.getMessage());

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_EXX_CODIGO,
          new StringBuilder(GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE).append(" ")
              .append(e.getMessage()).toString());

    }

    return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_E_000_CODIGO,
        DocumentoENICodigosRespuestaValidacion.D_E_000_DETALLE);

  }

  @Override
  public Detalle validarUnidadOrganicaDocumentoEniFile(String dataXml) {
    try {
      identificadorObjeto = XMLUtil.getvalorNodoDatosXML(dataXml, XPATH_RUTA_IDENTIFICADOR_DOC);

      // * String dataXml = XMLUtils.getDataExpedienteXML(xmlBytes); *//
      List<String> listaOrganos = XMLUtil.getContentNodeList(dataXml.getBytes(XMLUtil.UTF8_CHARSET),
          this.XPATH_RUTA_ORGANOS_DOC);
      String dir3Erroneo = validateOrganos(listaOrganos);
      if (!dir3Erroneo.equals("")) {
        return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_D_999_CODIGO,
            DocumentoENICodigosRespuestaValidacion.D_D_999_DETALLE + CUSTOM_SEPARATOR + dir3Erroneo
                + ")");
      } else {
        return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_D_000_CODIGO,
            DocumentoENICodigosRespuestaValidacion.D_D_000_DETALLE);
      }

    } catch (Exception e) {
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_DXX_CODIGO,
          GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE);
    }

  }

  /***********************************************************************************************************************************
   * **********************************************************************************************************************************
   * ************************************ VALIDACIONES EXPEDIENTEENI
   * ******************************************************************
   * **********************************************************************************************************************************
   * **********************************************************************************************************************************
   */

  /*
   * Validar shema de ExpedienteEni (non-Javadoc)
   * 
   * @see es.mpt.dsic.inside.validacionENI.EeutilEniValidationENIService#
   * validarShemaExpedienteEniFile(byte[])
   */
  @Override
  public Detalle validarShemaExpedienteEniFile(String dataXml, String version) {

    try {
      identificadorObjeto =
          XMLUtil.getvalorNodoDatosXML(dataXml, this.XPATH_RUTA_IDENTIFICADOR_EXP);
      validarSchema(dataXml.getBytes(XMLUtil.UTF8_CHARSET), version);

    } catch (SAXException e) {

      return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_E_999_CODIGO,
          ExpedienteENICodigosRespuestaValidacion.E_E_999_DETALLE + ". " + e.getMessage());

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_EXX_CODIGO,
          new StringBuilder(GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE).append(" ")
              .append(e.getMessage()).toString());

    }

    return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_E_000_CODIGO,
        ExpedienteENICodigosRespuestaValidacion.E_E_000_DETALLE);

  }

  @Override
  public Detalle validarUnidadOrganicaExpedienteEniFile(String dataXml) {
    try {
      identificadorObjeto =
          XMLUtil.getvalorNodoDatosXML(dataXml, this.XPATH_RUTA_IDENTIFICADOR_EXP);
      List<String> listaOrganos = XMLUtil.getContentNodeList(dataXml.getBytes(XMLUtil.UTF8_CHARSET),
          this.XPATH_RUTA_ORGANOS_EXP);
      String dir3Erroneo = validateOrganos(listaOrganos);
      if (!dir3Erroneo.equals("")) {
        return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_D_999_CODIGO,
            ExpedienteENICodigosRespuestaValidacion.E_D_999_DETALLE + CUSTOM_SEPARATOR + dir3Erroneo
                + ")");
      } else {
        return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_D_000_CODIGO,
            ExpedienteENICodigosRespuestaValidacion.E_D_000_DETALLE);
      }

    } catch (Exception e) {
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_DXX_CODIGO,
          GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE);
    }

  }

  @Override
  public Detalle validarCodigoSIAExpedienteEniFile(String dataXml) {
    try {
      identificadorObjeto =
          XMLUtil.getvalorNodoDatosXML(dataXml, this.XPATH_RUTA_IDENTIFICADOR_EXP);

      String clasificacionSIA = XMLUtil.getContentNode(dataXml.getBytes(XMLUtil.UTF8_CHARSET),
          XPATH_RUTA_CLASIFICACION_EXP);
      String fechaAperturaExpediente = XMLUtil
          .getContentNode(dataXml.getBytes(XMLUtil.UTF8_CHARSET), XPATH_RUTA_FECHA_APERTURA_EXP);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date feApExFormateada = sdf.parse(fechaAperturaExpediente);
      Calendar calendario = GregorianCalendar.getInstance();
      calendario.setTime(feApExFormateada);

      boolean correcto = validateClasificacionSIA(clasificacionSIA, calendario);
      if (!correcto) {
        return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_S_999_CODIGO,
            ExpedienteENICodigosRespuestaValidacion.E_S_999_DETALLE + CUSTOM_SEPARATOR
                + clasificacionSIA + ")");
      } else {
        return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_S_000_CODIGO,
            ExpedienteENICodigosRespuestaValidacion.E_S_000_DETALLE);
      }

    } catch (Exception e) {
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_SXX_CODIGO,
          GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE);
    }

  }

  public Detalle validarFirmaExpedienteEniFile(String idApp, String password, String xml,
      String xmlNoProcedenteInside, byte[] expedienteRecibidoOriginalSinTocar, String version) {

    ResultadoValidacionInfo resultado = null;
    try {

      resultado = consumerEeutilOperFirmaImpl.validacionFirma(idApp, password,
          expedienteRecibidoOriginalSinTocar, null, null);

    }

    catch (Exception e) {
      String detalle = GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE;
      detalle += ". " + e.getMessage();
      logger.error(detalle, e);
      // si resultado es nulo sacamos la excepcion
      if (resultado == null) {
        return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
            detalle);
      }
    }

    // si falla lo intenta de esta manera
    if (!resultado.isEstado()) {

      try {
        identificadorObjeto = XMLUtil.getvalorNodoDatosXML(xml, this.XPATH_RUTA_IDENTIFICADOR_EXP);
        byte[] firmaAValidar = getFirmaExpediente(xml);
        resultado =
            consumerEeutilOperFirmaImpl.validacionFirma(idApp, password, firmaAValidar, null, null);
      }

      catch (EeutilException e) {
        String detalle = GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE;
        detalle += ". " + e.getMessage();
        logger.error(detalle, e);
        return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
            detalle);
      } catch (Exception e) {
        String detalle = GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE;
        detalle += ". " + e.getMessage();
        logger.error(detalle, e);
        return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
            detalle);
      }
    }

    // Puede ser valida pero da error de referencia por meter unos namespaces en
    // addNameSpacesToExpedienteENIXML validos para
    // cuando el eni se hizo en inside, pero pueden mandar un eni no hecho desde
    // inside y hay que quitar esos namespaces aniadidos.
    // se a�ade la condicion resultado.getDetalle().contains("COD_103") porque en
    // des-afirma y afirma pro devolvian distinto error
    // y paara ambos conviene volver a realizar la llamada.
    if (resultado.getDetalle().contains("(VALIDATION) La referencia 2 contenida en la Firma")
        || resultado.getDetalle().contains("COD_103")) {
      try {
        // Elimina los namespaces que son 3 innecesarios para validar porque no se hizo
        // en inside el eni

        // String xmlNuevo = XMLUtil.obtenerExpedienteENIXMLDELETENAMESPACES(xml);
        byte[] firmaAValidarNOProvieneInside = getFirmaExpediente(xmlNoProcedenteInside);

        resultado = consumerEeutilOperFirmaImpl.validacionFirma(idApp, password,
            firmaAValidarNOProvieneInside, null, null);

      } catch (Exception e) {
        return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
            GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE);
      }
    }

    // expediente de sip que iria nos da que no valida... mete bien los namespace
    // pero no hay que meter los tres que se introduces insidews, ns8 y ns9
    if (!resultado.isEstado()) {
      try {
        // Elimina los namespaces que son 3 innecesarios para validar porque no se hizo
        // en inside el eni
        byte[] firmaAValidarNOProvieneInside =
            getFirmaExpediente(XMLUtil.deleteNameSpacesToExpedienteENIXML(
                XMLUtil.getNode(xml.getBytes(XMLUtil.UTF8_CHARSET), "*"), version));

        resultado = consumerEeutilOperFirmaImpl.validacionFirma(idApp, password,
            firmaAValidarNOProvieneInside, null, null);

      } catch (Exception e) {
        return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
            GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE);
      }
    }

    // si el estado es true -> firma valida
    if (resultado.isEstado())
      return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_F_000_CODIGO,
          ExpedienteENICodigosRespuestaValidacion.E_F_000_DETALLE + ESTADO_CONSTANT
              + resultado.isEstado() + DETALLE_CONSTANT + resultado.getDetalle());
    else
      return getDetalleValidacion(ExpedienteENICodigosRespuestaValidacion.E_F_999_CODIGO,
          ExpedienteENICodigosRespuestaValidacion.E_F_999_DETALLE + ESTADO_CONSTANT
              + resultado.isEstado() + DETALLE_CONSTANT + resultado.getDetalle());
  }

  /*
   * valida un xml frente a unos shemas
   * 
   */
  private void validarSchema(byte[] xmlBytes, String version) throws EeutilException, SAXException {

    EeutilException eeutilThrow = null;
    SchemaEniModel schemaEniModel = new SchemaEniModel();

    schemaEniModel.setEntrada(xmlBytes);
    schemaEniModel.setVersion(version);
    MDCWrapper mdcWrapper = new MDCWrapper((String) MDC.get("idApli"), (String) MDC.get("uUId"),
        (String) MDC.get("ipClient"), (String) MDC.get("clientHost"), (String) MDC.get("clientURI"),
        (String) MDC.get("contentLengh"), (String) MDC.get("extraParaM"));
    schemaEniModel.setMdcWrapper(mdcWrapper);

    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;

    try {

      response = restIgaeServiceC.getResponseClientIgae(httpClient,
          ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + "/api/validarSchemaENI",
          schemaEniModel, ConfigureRestInfo.getInstance().getTokenNameRestIgae(),
          ConfigureRestInfo.getInstance().getTokenValueRestIgae());
      HttpEntity entityResponse = response.getEntity();

      if (response.getStatusLine().getStatusCode() == 500) {
        String resultado = EntityUtils.toString(entityResponse);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultado);
        throw new SAXException("Error al validar el schema:" + jsonObject.get("message"));

      } else if (response.getStatusLine().getStatusCode() == 200) {
        String resultado = null;
        if (entityResponse != null) {
          resultado = EntityUtils.toString(entityResponse);
          JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultado);

          boolean success = (boolean) jsonObject.get("status");

          if (!success) {
            throw new SAXException("ESTRUCTURA INCORRECTA");
          }

        }



      } else {
        throw new EeutilException("ERROR AL ACCEDER AL SERVICIO DE IGAE "
            + (ConfigureRestInfo.getInstance().getBaseUrlRestIgae() + " status:"
                + response.getStatusLine().getStatusCode()));
      }

    } catch (SAXException e) {
      throw new SAXException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    } finally {
      try {
        restIgaeServiceC.cerrarRequestResponse(httpClient, response);
      } catch (IOException e) {
        eeutilThrow = new EeutilException(e.getMessage(), e);
      }

    }

    if (eeutilThrow != null) {
      throw eeutilThrow;
    }

    // try (ByteArrayInputStream bin = new ByteArrayInputStream(xmlBytes);) {
    //
    // XMLReader parser = null;
    //
    // if (StringUtils.isNotBlank(version)) {
    // parser = XMLUtil.createParserForValidation(getSchemasByVesion(version));
    // } else {
    // parser = XMLUtil.createParserForValidation(schemasSourcesDefault);
    // }
    // InputSource xmlSource = new InputSource(bin);
    // parser.parse(xmlSource);
    // } catch (IOException e) {
    // // logger.error(e.getMessage(),e);
    // throw new EeutilException(e.getMessage(), e);
    // }
    // catch (SAXException e1) {
    // throw new EeutilException(e1.getMessage(), e1);
    // }
  }

  /*
   * Construye detalle validacion
   * 
   */
  private Detalle getDetalleValidacion(String codigo, String detalle) {
    Detalle detalleValidacion = new Detalle();
    detalleValidacion.setIdObjeto(identificadorObjeto);
    detalleValidacion.setCodigoRespuesta(codigo);
    detalleValidacion.setDetalleRespuesta(detalle);
    return detalleValidacion;

  }

  /*
   * Devuelve los dir3 erroneos
   * 
   */
  private String validateOrganos(List<String> organos) {
    StringBuilder tmpBuff = new StringBuilder("");
    for (String organo : organos) {
      List<Criterion> criterias = new ArrayList<>();
      criterias.add(Restrictions.eq("codigoUnidadOrganica", organo));
      UnidadOrganica unidadOrganica =
          (UnidadOrganica) eeutilDao.findObjeto(UnidadOrganica.class, criterias);
      if (unidadOrganica == null) {
        if (!tmpBuff.toString().equals("")) {
          tmpBuff.append(",");
        }
        tmpBuff.append(organo);
      }
    }

    return tmpBuff.toString();
  }

  private boolean validateClasificacionSIA(String clasificacionSIA, Calendar fecha) {
    String clasificacionPattern = ".*_PRO_.*";
    boolean retorno = true;
    Integer anio = fecha.get(Calendar.YEAR);
    boolean exists = true;

    try {
      exists = consumidorSIA.existClasificacion(clasificacionSIA, anio.toString());
    } catch (Exception e) {
      retorno = false;
      logger.error("Error en la llamada al servicio de SIA para validar");
    }

    try {
      if (!exists) {
        logger.debug("Valor de clasificaci�n no corresponde en SIA");
        Pattern pattern = Pattern.compile(clasificacionPattern);
        Matcher matcher = pattern.matcher(clasificacionSIA);
        if (!matcher.matches()) {
          retorno = false;
        } else {

          List<Criterion> criterias = new ArrayList<>();

          StringTokenizer tmpToken = new StringTokenizer(clasificacionSIA, "_PRO_");
          String data = (String) tmpToken.nextElement();
          criterias.add(Restrictions.eq("codigoUnidadOrganica", data));

          UnidadOrganica unidadOrganica =
              (UnidadOrganica) eeutilDao.findObjeto(UnidadOrganica.class, criterias);

          if (unidadOrganica == null) {
            retorno = false;
          }
        }
      }
    } catch (Exception e) {
      retorno = false;
      logger.error("Error en la busqueda de SIA en tabla para validarlo");
    }

    return retorno;

  }

  private byte[] getFirmaExpediente(String xml) throws EeutilException {

    String firma = null;

    try {
      String tipoFirma = XMLUtil.getvalorNodoDatosXML(xml, this.XPATH_RUTA_TIPO_FIRMA_EXP);

      // CADES
      if (tipoFirma != null && tipoFirma.equalsIgnoreCase(this.TIPOFIRMA_TF_05)) {
        firma =
            XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET), XPATH_RUTA_FIRMA_BASE64_EXP);
      }

      // XADES INTERNAL DETACHED XADES ENVELOPED SIGNATURE
      if (tipoFirma != null && (tipoFirma.equalsIgnoreCase(this.TIPOFIRMA_TF_02)
          || tipoFirma.equalsIgnoreCase(this.TIPOFIRMA_TF_03))) {
        // ver si trae firma en nodo firmabase64 codificada en base64. si es asi
        // devolvemos eso.
        firma =
            XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET), XPATH_RUTA_FIRMA_BASE64_EXP);

        // Si firmanodobase64 es nulo
        if (firma == null || "".equals(firma.trim())) {
          firma = xml;
        }

      }

      if (StringUtils.isEmpty(firma) && StringUtils.isEmpty(tipoFirma)) {
        // lanzamos error no hemos detectado la firma del expediente
        throw new TipoFirmaException("No se ha detectado la firma del expediente");
      } else if (StringUtils.isEmpty(firma) && !StringUtils.isEmpty(tipoFirma)) {
        throw new TipoFirmaException("El tipo de firma (" + tipoFirma
            + ") no es permitida para el Expediente ENI.Firmas validas:" + TIPOFIRMA_TF_05
            + "(CADES)," + TIPOFIRMA_TF_02 + "(XADES INTERNAL DETACHED)," + TIPOFIRMA_TF_03
            + "(XADES ENVELOPED SIGNATURE)");
      }

    } catch (RuntimeException e) {
      // suponemos que es runtime y no qeremos capturarla, la dejamos asi
      throw e;
    } catch (TipoFirmaException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    if (Base64.isBase64(firma))
      return Base64.decodeBase64(firma);
    else
      return firma != null ? firma.getBytes(XMLUtil.UTF8_CHARSET) : null;

  }

  @Override
  public Detalle validarFirmaDocumentoEniFile(String idApp, String password, String xml) {

    boolean firmaValida = true;
    int numeroFirmasDocTotal = 0;
    int contadorFirmasContenidas = 0;
    List<String> agregadosErroresValidacionFirmaDocEni = null;

    try {
      identificadorObjeto = XMLUtil.getvalorNodoDatosXML(xml, this.XPATH_RUTA_IDENTIFICADOR_DOC);

      List<FirmaDatos> listaFirmas = getListaFirmaDocumento(xml);

      List<ResultadoValidacionDocENI> listaResultados = new ArrayList<>();

      List<ResultadoValidacionDocENI> listaResultadosRefFirma = new ArrayList<>();

      agregadosErroresValidacionFirmaDocEni = new ArrayList<>();

      if (listaFirmas != null && !listaFirmas.isEmpty()) {
        for (FirmaDatos firma : listaFirmas) {
          if (StringUtils.isBlank(firma.getRefFirma())) {
            ResultadoValidacionFirmaInfo resultadoValidacion = consumerEeutilOperFirmaImpl
                .validacionFirmaInfo(idApp, password, (byte[]) firma.getContenidoFirma(), null,
                    (DatosFirmados) firma.getDatosFirmados(), false);
            listaResultados.add(new ResultadoValidacionDocENI(
                resultadoValidacion.getNumeroFirmantes(), firma.isFirmaMultiple(),
                resultadoValidacion.isEstado(), firma.getIdFirma(), null));
            // si la validacion da estado falso, sacamos el detalle.
            if (resultadoValidacion != null && !resultadoValidacion.isEstado()
                && resultadoValidacion.getDetalle() != null
                && !"".equals(resultadoValidacion.getDetalle())) {
              // a�adimos el error
              agregadosErroresValidacionFirmaDocEni.add(resultadoValidacion.getDetalle());
            }

          } else {
            ResultadoValidacionDocENI result = new ResultadoValidacionDocENI();
            result.setRefFirmaValor(firma.getRefFirma());
            result.setFirmaMultiple(firma.isFirmaMultiple());
            listaResultadosRefFirma.add(result);
          }
        }

      }

      List<ResultadoValidacionDocENI> listaResultadosAnadir = new ArrayList<>();

      if (!listaResultadosRefFirma.isEmpty()) {

        for (ResultadoValidacionDocENI r : listaResultadosRefFirma) {
          String refFirmaValor = r.getRefFirmaValor();

          for (ResultadoValidacionDocENI resultValDocEni : listaResultados) {
            if (refFirmaValor.equals(resultValDocEni.getIdFirma())) {
              ResultadoValidacionDocENI resultado = new ResultadoValidacionDocENI();
              resultado.setValida(resultValDocEni.isValida());
              resultado.setFirmaMultiple(r.isFirmaMultiple());
              resultado.setNumeroFirmas(resultValDocEni.getNumeroFirmas());
              listaResultadosAnadir.add(resultado);
              resultValDocEni.setFirmaMultiple(true);
            }
          }
        }

        listaResultados.addAll(listaResultadosAnadir);
      }

      if (!listaResultados.isEmpty()) {
        for (ResultadoValidacionDocENI r : listaResultados) {
          firmaValida = firmaValida && r.isValida();
          if (r.isFirmaMultiple()) {
            if (numeroFirmasDocTotal == 0) {
              numeroFirmasDocTotal = r.getNumeroFirmas();
            }
            contadorFirmasContenidas++;
          }
        }
      }

    } catch (Exception e) {
      return getDetalleValidacion(GlobalValidacionENICodigosRespuestaValidacion.G_FXX_CODIGO,
          GlobalValidacionENICodigosRespuestaValidacion.G_XXX_DETALLE + "." + e.getMessage());
    }

    if (firmaValida) {
      // firmas validas
      // RESULTADO FINAL VALIDO
      return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_F_000_CODIGO,
          DocumentoENICodigosRespuestaValidacion.D_F_000_DETALLE + ESTADO_CONSTANT + true
              + DETALLE_CONSTANT + "La firma es v�lida");
    } else {
      // firmas invalidas
      return getDetalleValidacion(DocumentoENICodigosRespuestaValidacion.D_F_999_CODIGO,
          DocumentoENICodigosRespuestaValidacion.D_F_999_DETALLE + ESTADO_CONSTANT + false
              + " -- detalle: Error en la valicacion de la firma"
              + generarMsgAPartirAgregadoErrores(agregadosErroresValidacionFirmaDocEni));
    }
  }

  private String generarMsgAPartirAgregadoErrores(List<String> agregados) {
    StringBuilder str = new StringBuilder("");

    if (agregados != null && !agregados.isEmpty()) {
      for (String aux : agregados) {
        str.append(aux).append(System.lineSeparator());
      }
    }

    return str.toString();

  }

  private List<FirmaDatos> getListaFirmaDocumento(String xml) throws EeutilException {

    List<FirmaDatos> datos = null;
    int contadorTagFirma = 0;

    try (ByteArrayInputStream bInputStream =
        new ByteArrayInputStream(xml.getBytes(XMLUtil.UTF8_CHARSET))) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      XMLSeguridadFactoria.getInstance().setPreventAttackDocumentBuilderFactoryExternal(dbf);
      // dbf.setFeature(XMLSeguridadFactoria.SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
      // dbf.setFeature(XMLSeguridadFactoria.SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
      Document node = dbf.newDocumentBuilder().parse(bInputStream);

      NodeList nodeList = XMLUtil.getNodeListByXpathExpression(node, XPATH_RUTA_FIRMA);

      datos = new ArrayList<>();

      for (int i = 0; i < nodeList.getLength(); i++) {
        Node item = nodeList.item(i);
        contadorTagFirma++;

        if (item.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) item;
          String tipoFirmaText = "";
          if (eElement.getChildNodes().item(0).getNodeType() == Node.ELEMENT_NODE
              && eElement.getChildNodes().item(0).getNodeName().contains("TipoFirma")) {
            tipoFirmaText = eElement.getChildNodes().item(0).getTextContent();
          } else if (eElement.getChildNodes().item(1).getNodeType() == Node.ELEMENT_NODE
              && eElement.getChildNodes().item(1).getNodeName().contains("TipoFirma")) {
            tipoFirmaText = eElement.getChildNodes().item(1).getTextContent();
          }
          if (Integer.parseInt("" + tipoFirmaText.charAt(3)) != 1) {
            // No se considera la validacion de las firmas de tipo TF01 (CSV)
            datos.add(this.obtenerDatosInfoFirma(tipoFirmaText, xml, contadorTagFirma));
          }
        }
      }

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return datos;

  }

  private FirmaDatos obtenerDatosInfoFirma(String tipoFirma, String xml, int contadorTagFirma)
      throws EeutilException {
    // Partimos de la base, que cuando la firma est� contenido en el elemento XML
    // FirmaBase64, se trata de una �nica firma
    // Cuando la firma est� contenida dentro del elemento XML DatosXML o
    // ValorBinario, ese documento puede tener de varias firmas

    FirmaDatos firmaDatos = new FirmaDatos();
    Object contenidoFirmaObj = null;
    String contenidoFirma = null;
    Object datosFirmadosObj = null;

    try {

      String idFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
          String.format(XPATH_RUTA_TF05_CADES_ATTACHED_IDFIRMA, contadorTagFirma));

      firmaDatos.setIdFirma(idFirma);

      switch (Integer.parseInt("" + tipoFirma.charAt(3))) {
        case 2:
          contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              XPATH_RUTA_TF02_XADES_DETACHED);
          firmaDatos.setFirmaMultiple(true);
          if ("".equals(contenidoFirma)) {
            contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_MULTI, contadorTagFirma));
            firmaDatos.setFirmaMultiple(false);
          }
          if ("".equals(contenidoFirma)) {
            String refFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_REFFIRMA, contadorTagFirma));
            firmaDatos.setRefFirma(refFirma.substring(1, refFirma.length()));
            firmaDatos.setFirmaMultiple(true);
          }
          break;
        case 3:
          contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              XPATH_RUTA_TF03_XADES_ENVELOPED);
          firmaDatos.setFirmaMultiple(true);
          if ("".equals(contenidoFirma)) {
            contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_MULTI, contadorTagFirma));
            firmaDatos.setFirmaMultiple(false);
          }
          if ("".equals(contenidoFirma)) {
            String refFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_REFFIRMA, contadorTagFirma));
            firmaDatos.setRefFirma(refFirma.substring(1, refFirma.length()));
            firmaDatos.setFirmaMultiple(true);
          }
          break;
        case 4:
          contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              String.format(XPATH_RUTA_MULTI, contadorTagFirma));
          if ("".equals(contenidoFirma)) {
            String refFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_REFFIRMA, contadorTagFirma));
            firmaDatos.setRefFirma(refFirma.substring(1, refFirma.length()));
            firmaDatos.setFirmaMultiple(true);
          }
          String datosFirmados = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              XPATH_RUTA_TF04_CADES_DETACHED_DOC);
          DatosFirmados df = new DatosFirmados();
          df.setDocumento(Base64.decodeBase64(datosFirmados.getBytes(XMLUtil.UTF8_CHARSET)));
          datosFirmadosObj = df;
          firmaDatos.setFirmaMultiple(false);
          break;
        case 5:
          contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              String.format(XPATH_RUTA_MULTI, contadorTagFirma));
          firmaDatos.setFirmaMultiple(false);
          if ("".equals(contenidoFirma)) {
            contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                XPATH_RUTA_TF05_CADES_ATTACHED_REF_VALORBINARIO);
            firmaDatos.setFirmaMultiple(true);
          }
          if ("".equals(contenidoFirma)) {
            String refFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_REFFIRMA, contadorTagFirma));
            firmaDatos.setRefFirma(refFirma.substring(1, refFirma.length()));
            firmaDatos.setFirmaMultiple(true);
          }
          break;
        case 6:
          contenidoFirma =
              XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET), XPATH_RUTA_TFO6_PADES);
          firmaDatos.setFirmaMultiple(true);
          if ("".equals(contenidoFirma)) {
            contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_MULTI, contadorTagFirma));
            firmaDatos.setFirmaMultiple(false);
          }
          if ("".equals(contenidoFirma)) {
            String refFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
                String.format(XPATH_RUTA_REFFIRMA, contadorTagFirma));
            firmaDatos.setRefFirma(refFirma.substring(1, refFirma.length()));
            firmaDatos.setFirmaMultiple(true);
          }
          break;
        case 7:
          contenidoFirma = XMLUtil.getContentNode(xml.getBytes(XMLUtil.UTF8_CHARSET),
              String.format(XPATH_RUTA_MULTI, contadorTagFirma));
          firmaDatos.setFirmaMultiple(false);
          break;

        default:
          throw new EEUtilMiscException("El tipo de firma " + tipoFirma + " no esta registrada");

      }

      if (Base64.isBase64(contenidoFirma.getBytes(XMLUtil.UTF8_CHARSET))) {
        contenidoFirmaObj = Base64.decodeBase64(contenidoFirma.getBytes(StandardCharsets.UTF_8));
      } else {
        contenidoFirmaObj = contenidoFirma.getBytes(StandardCharsets.UTF_8);
      }

      firmaDatos.setContenidoFirma(contenidoFirmaObj);
      firmaDatos.setDatosFirmados(datosFirmadosObj);

      return firmaDatos;

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

  }

}
