package es.mpt.dsic.inside.ws.service.impl;


import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditEntryPointAnnotation;
import es.mpt.dsic.inside.reflection.MapUtil;
import es.mpt.dsic.inside.reflection.UtilReflection;
import es.mpt.dsic.inside.security.model.ApplicationLogin;
import es.mpt.dsic.inside.utils.xml.XMLUtil;
import es.mpt.dsic.inside.ws.service.EeUtilValidacionENIService;
import es.mpt.dsic.inside.ws.service.model.RespuestaValidacionENI;
import es.mpt.dsic.inside.ws.service.model.Validaciones;


@Service("eeUtilValidacionENIService")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilValidacionENIService")
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.BARE, use = Use.LITERAL)
public class EeUtilValidacionENIServiceImpl implements EeUtilValidacionENIService {

  private static final String ENI_XSD_V_PATTERN = "ENI/XSD/v";

  private static final String VERSION_PARAM = "version";

  private static final String EXTRA_PARA_M = "ExtraParaM";

  private static final String G_999 = "G.999";

  protected static final Log logger = LogFactory.getLog(EeUtilValidacionENIServiceImpl.class);

  /**
   * Etiqueta para sacar la version del ENI
   */
  private static final String VERSION_NTI = "VersionNTI";

  @Autowired
  EeUtilValidacionENIServiceBusiness eeUtilValidacionENIServiceBusiness;



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarFirmaDocumentoENI(ApplicationLogin info,
      byte[] documentoENI) {

    RespuestaValidacionENI respuesta = null;

    try {

      // obtenemos la version del eni del nodo VersionNTI

      String version = XMLUtil.getNodeValue(documentoENI, VERSION_NTI);

      version = obtenerVersionDesdeNTI(version);

      respuesta = eeUtilValidacionENIServiceBusiness.validarFirmaDocumentoENI(
          info.getIdApplicacion(), info.getPassword(), documentoENI, version);

      if (respuesta.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarFirmaDocumentoENIMDC(documentoENI);
        logger.error(respuesta.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarFirmaDocumentoENIMDC(documentoENI);
      logger.error(e.getMessage(), e);
    }

    return respuesta;

  }



  /**
   * @param documentoENI
   */
  private void validarFirmaDocumentoENIMDC(byte[] documentoENI) {
    try {
      Object[] objs = new Object[1];
      String[] strP = new String[] {"documentoENI"};
      objs[0] = documentoENI;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarDocumentoENI(ApplicationLogin info, byte[] documentoENI,
      String version, Validaciones validaciones) {


    RespuestaValidacionENI respuesta = null;

    try {

      respuesta = eeUtilValidacionENIServiceBusiness.validarDocumentoENI(info.getIdApplicacion(),
          info.getPassword(), documentoENI, version, validaciones);

      if (respuesta.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarDocumentoENIMDC(documentoENI, version, validaciones);
        logger.error(respuesta.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarDocumentoENIMDC(documentoENI, version, validaciones);
      logger.error(e.getMessage(), e);
    }

    return respuesta;
  }


  /**
   * @param documentoENI
   * @param version
   * @param validaciones
   */
  private void validarDocumentoENIMDC(byte[] documentoENI, String version,
      Validaciones validaciones) {
    try {
      Object[] objs = new Object[3];
      String[] strP = new String[] {"documentoENI", VERSION_PARAM, "validaciones"};
      objs[0] = documentoENI;
      objs[1] = version;
      objs[2] = validaciones;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarExpedienteENI(ApplicationLogin info, byte[] expedienteENI,
      String version, Validaciones validaciones) {



    RespuestaValidacionENI respuesta = null;

    try {

      respuesta = eeUtilValidacionENIServiceBusiness.validarExpedienteENI(info.getIdApplicacion(),
          info.getPassword(), expedienteENI, version, validaciones);

      if (respuesta.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarExpedienteENIMDC(expedienteENI, version, validaciones);
        logger.error(respuesta.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarExpedienteENIMDC(expedienteENI, version, validaciones);
      logger.error(e.getMessage(), e);
    }

    return respuesta;
  }



  /**
   * @param expedienteENI
   * @param version
   * @param validaciones
   */
  private void validarExpedienteENIMDC(byte[] expedienteENI, String version,
      Validaciones validaciones) {
    try {
      Object[] objs = new Object[3];
      String[] strP = new String[] {"expedienteENI", VERSION_PARAM, "validaciones"};
      objs[0] = expedienteENI;
      objs[1] = version;
      objs[2] = validaciones;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarFirmaExpedienteENI(ApplicationLogin info,
      byte[] expedienteENI) {



    RespuestaValidacionENI respuesta = null;

    try {

      // obtenemos la version del eni del nodo VersionNTI

      String version = XMLUtil.getNodeValue(expedienteENI, VERSION_NTI);

      version = obtenerVersionDesdeNTI(version);

      respuesta = eeUtilValidacionENIServiceBusiness.validarFirmaExpedienteENI(
          info.getIdApplicacion(), info.getPassword(), expedienteENI, version);

      if (respuesta.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarFirmaExpedienteENIMDC(expedienteENI);
        logger.error(respuesta.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarFirmaExpedienteENIMDC(expedienteENI);
      logger.error(e.getMessage(), e);
    }

    return respuesta;

  }



  /**
   * @param expedienteENI
   */
  private void validarFirmaExpedienteENIMDC(byte[] expedienteENI) {
    try {
      Object[] objs = new Object[1];
      String[] strP = new String[] {"expedienteENI"};
      objs[0] = expedienteENI;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarExpedienteDocumentosENIZIP(ApplicationLogin info,
      byte[] docZipEntrada, String version, Validaciones validacionesExpediente,
      Validaciones validacionesDocumentos) {



    RespuestaValidacionENI respuesta = null;

    try {

      respuesta = eeUtilValidacionENIServiceBusiness.validarExpedienteDocumentosENIZIP(
          info.getIdApplicacion(), info.getPassword(), docZipEntrada, version,
          validacionesExpediente, validacionesDocumentos);

      if (respuesta.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarExpedienteDocumentosENIZIPMDC(docZipEntrada, version, validacionesExpediente,
            validacionesDocumentos);
        logger.error(respuesta.getDetalle());
      }


    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarExpedienteDocumentosENIZIPMDC(docZipEntrada, version, validacionesExpediente,
          validacionesDocumentos);
      logger.error(e.getMessage(), e);
    }

    return respuesta;


  }



  /**
   * @param docZipEntrada
   * @param version
   * @param validacionesExpediente
   * @param validacionesDocumentos
   */
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  private void validarExpedienteDocumentosENIZIPMDC(byte[] docZipEntrada, String version,
      Validaciones validacionesExpediente, Validaciones validacionesDocumentos) {
    try {
      Object[] objs = new Object[4];
      String[] strP = new String[] {"docZipEntrada", VERSION_PARAM, "validacionesExpediente",
          "validacionesDocumentos"};
      objs[0] = docZipEntrada;
      objs[1] = version;
      objs[2] = validacionesExpediente;
      objs[3] = validacionesDocumentos;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }

  private void ingresarMDCAppUUID(String idApp) {
    MDC.put("idApli", idApp);
    MDC.put("uUId", UUID.randomUUID().toString());
  }


  /**
   * Obtiene el valor numerico de la version NTI a partir del valor del nodo NTI
   * 
   * @param versionNTI
   * @return 1.0 o 2.0
   */
  private String obtenerVersionDesdeNTI(String versionNTI) {
    String version = null;

    try {

      if (versionNTI != null && versionNTI.length() > 0) {
        if (versionNTI.indexOf("documento-e") != -1) {
          version = versionNTI.substring(
              versionNTI.indexOf(ENI_XSD_V_PATTERN) + ENI_XSD_V_PATTERN.length(),
              versionNTI.indexOf("/documento-e"));
        } else if (versionNTI.indexOf("expediente-e") != -1) {
          version = versionNTI.substring(
              versionNTI.indexOf(ENI_XSD_V_PATTERN) + ENI_XSD_V_PATTERN.length(),
              versionNTI.indexOf("/expediente-e"));
        }

      }

    } catch (Exception e) {
      return version;
    }

    return version;
  }


}
