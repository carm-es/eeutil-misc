package es.mpt.dsic.inside.ws.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditEntryPointAnnotation;
import es.mpt.dsic.inside.reflection.MapUtil;
import es.mpt.dsic.inside.reflection.UtilReflection;
import es.mpt.dsic.inside.security.context.AplicacionContext;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.security.model.ApplicationLogin;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.ws.service.EeUtilServiceMtom;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntradaMtom;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalidaMtom;

@Service("eeUtilServiceMtom")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilServiceMtom")
public class EeUtilServiceMtomImpl implements EeUtilServiceMtom {

  private static final String ERROR = "ERROR";

  protected static final Log logger = LogFactory.getLog(EeUtilServiceMtomImpl.class);

  @Autowired
  private AplicacionContext aplicacionContext;

  @Autowired
  EeUtilMiscServiceBusiness eeUtilMiscServiceBusiness;

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public PdfSalidaMtom convertirPDFA(ApplicationLogin info, DocumentoEntradaMtom docEntrada,
      Integer nivelCompilacion) throws InSideException {
    try {
      AppInfo appInfo = aplicacionContext.getAplicacionInfo();
      return eeUtilMiscServiceBusiness.convertirPDFAMtom(appInfo.getIdaplicacion(),
          appInfo.getPropiedades().get("ip.openoffice"),
          appInfo.getPropiedades().get("port.openoffice"), docEntrada, nivelCompilacion,
          docEntrada.getPassword());

    } catch (EeutilException e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      convertirPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      convertirPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      EstadoInfo info2 = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info2, e);
    }
  }


  /**
   * @param docEntrada
   * @param nivelCompilacion
   */
  private void convertirPDFAMDC(DocumentoEntradaMtom docEntrada, Integer nivelCompilacion) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"docEntrada", "nivelCompilacion"};
      objs[0] = docEntrada;
      objs[1] = nivelCompilacion;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put("ExtraParaM", resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public Boolean comprobarPDFA(ApplicationLogin info, DocumentoEntradaMtom docEntrada,
      Integer nivelCompilacion) throws InSideException {
    try {

      return eeUtilMiscServiceBusiness.comprobarPDFAMtom(docEntrada, nivelCompilacion);

    } catch (EeutilException e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      comprobarPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      comprobarPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      EstadoInfo info2 = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info2, e);
    }
  }


  /**
   * @param docEntrada
   * @param nivelCompilacion
   */
  private void comprobarPDFAMDC(DocumentoEntradaMtom docEntrada, Integer nivelCompilacion) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"docEntrada", "nivelCompilacion"};
      objs[0] = docEntrada;
      objs[1] = nivelCompilacion;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put("ExtraParaM", resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }


  private void ingresarMDCAppUUID(String idApp) {
    MDC.put("idApli", idApp);
    MDC.put("uUId", UUID.randomUUID().toString());
  }

}
