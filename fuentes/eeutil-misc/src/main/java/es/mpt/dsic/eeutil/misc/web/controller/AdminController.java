package es.mpt.dsic.eeutil.misc.web.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.mpt.dsic.eeutil.misc.web.controller.model.InfoCertModel;
import es.mpt.dsic.eeutil.misc.web.util.MessageObject;
import es.mpt.dsic.eeutil.misc.web.util.WebConstants;
import es.mpt.dsic.eeutil.operFirma.consumer.impl.ConsumerEeutilOperFirmaImpl;
import es.mpt.dsic.inside.dao.EeutilDao;
import es.mpt.dsic.inside.schredule.CertificadoValidadorTask;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.string.StringUtil;
import es.mpt.dsic.inside.utils.test.IPruebaTraza;
import es.mpt.dsic.inside.utils.test.UtilidadesTestAfirmawsStub;
import es.mpt.dsic.inside.utils.test.UtilidadesTestComunes;
import es.mpt.dsic.inside.utils.test.UtilidadesTestEeutilMisc;
import es.mpt.dsic.inside.utils.test.UtilidadesTestLoadTables;
import es.mpt.dsic.inside.utils.test.UtilidadesTestModel;
import es.mpt.dsic.inside.utils.test.UtilidadesTestPdfConversionIgae;
import es.mpt.dsic.inside.utils.test.UtilidadesTestSchredule;
import es.mpt.dsic.inside.utils.test.UtilidadesTestServices;
import es.mpt.dsic.inside.utils.test.UtilidadesTestUtil;
import es.mpt.dsic.loadTables.schredule.UnidadOrganicaTask;

@Controller
@RequestMapping("administracion")
public class AdminController extends BaseController {

  private static final String DATOS_PARAM = "datos";

  private static final String ddMMyyyy_MASK = "ddMMyyyy";

  private static final String VERSIONMAVEN_PARAM = "versionmaven";

  private static final String MENSAJE_USUARIO = "mensajeUsuario";

  private static final String ES_ES = "es_ES";

  private static final String ADMINISTRACION_ACTUALIZAR_DIR3_MSG = "administracion.actualizarDIR3";

  private static final String FECHA_INICIO_01012010 = "01012010";

  private static final String PRINCIPAL = "principal";

  protected static final Log logger = LogFactory.getLog(AdminController.class);

  @Autowired
  @Qualifier(value = "consumerEeutilOperFirma")
  private ConsumerEeutilOperFirmaImpl consumerEeutilOperFirmaImpl;

  @Autowired
  private EeutilDao eeutilDao;

  @Autowired
  @Qualifier("entityManagerFactory")
  private EntityManagerFactory entityManagerFactory;

  // @Autowired
  // AdministrationService administrationService;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  UnidadOrganicaTask unidadOrganicaTask;

  @Autowired
  CertificadoValidadorTask certificadoValidadorTask;

  private static final String strDateFormat = ddMMyyyy_MASK;

  /**
   * Metodo para actualizar dir3 con fechaInicio y fechaFin (parametros obligatorios) Ej:
   * http://host:port/eeutil-misc/administracion/actualizarDIR3F?fechaInicio=10022021&fechaFin=
   * 
   * @param fechaInicio la fecha debe ir en formato ddMMyyyy ej: 24122020
   * @param fechaFin la fecha debe ir en formato ddMMyyyy ej: 24122020 o vacio
   * @return
   */
  @RequestMapping(value = "/actualizarDIR3F", method = RequestMethod.GET)
  public ModelAndView actualizarDIR3Fechas(@RequestParam String fechaInicio,
      @RequestParam String fechaFin) {
    ModelAndView retorno = new ModelAndView(PRINCIPAL);
    MessageObject mensaje = null;
    try {

      Date dInicial = null;
      Date dFin = null;
      SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

      // si es vacia la fecha de inicio ponemos una inicial
      if (StringUtil.esVacioOrNull(fechaInicio)) {
        dInicial = objSDF.parse(FECHA_INICIO_01012010);
      }
      // si no es vacia ponemos la parametrizada en formato ddMMyyyy
      else {
        dInicial = objSDF.parse(fechaInicio);
      }

      // si es vacia la fecha de fin no ponemos ninguna, si no es la parametrizada en
      // formato ddMMyyyy
      if (!StringUtil.esVacioOrNull(fechaFin)) {
        dFin = objSDF.parse(fechaFin);
      }

      unidadOrganicaTask.loadUnidadOrganica(dInicial, dFin);
      unidadOrganicaTask.loadOficinas(dInicial, dFin);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, mensaje);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_ERROR,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, e.getMessage());
    }

    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }



  /**
   * Metodo para actualizar dir3 (unidades organicas) con fechaInicio y fechaFin (parametros
   * obligatorios) Ej:
   * http://host:port/eeutil-misc/administracion/actualizarDIR3UOF?fechaInicio=10022021&fechaFin=
   * 
   * @param fechaInicio la fecha debe ir en formato ddMMyyyy ej: 24122020
   * @param fechaFin la fecha debe ir en formato ddMMyyyy ej: 24122020 o vacio
   * @return
   */
  @RequestMapping(value = "/actualizarDIR3UOF", method = RequestMethod.GET)
  public ModelAndView actualizarDIR3UnidadesOrganicasFechas(@RequestParam String fechaInicio,
      @RequestParam String fechaFin) {
    ModelAndView retorno = new ModelAndView(PRINCIPAL);
    MessageObject mensaje = null;
    try {

      Date dInicial = null;
      Date dFin = null;
      SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

      // si es vacia la fecha de inicio ponemos una inicial
      if (StringUtil.esVacioOrNull(fechaInicio)) {
        dInicial = objSDF.parse(FECHA_INICIO_01012010);
      }
      // si no es vacia ponemos la parametrizada en formato ddMMyyyy
      else {
        dInicial = objSDF.parse(fechaInicio);
      }

      // si es vacia la fecha de fin no ponemos ninguna, si no es la parametrizada en
      // formato ddMMyyyy
      if (!StringUtil.esVacioOrNull(fechaFin)) {
        dFin = objSDF.parse(fechaFin);
      }

      unidadOrganicaTask.loadUnidadOrganica(dInicial, dFin);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, mensaje);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_ERROR,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, e.getMessage());
    }

    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }


  /**
   * Metodo para actualizar dir3 (oficinas) con fechaInicio y fechaFin (parametros obligatorios) Ej:
   * http://host:port/eeutil-misc/administracion/actualizarDIR3OF?fechaInicio=10022021&fechaFin=
   * 
   * @param fechaInicio la fecha debe ir en formato ddMMyyyy ej: 24122020
   * @param fechaFin la fecha debe ir en formato ddMMyyyy ej: 24122020 o vacio
   * @return
   */
  @RequestMapping(value = "/actualizarDIR3OF", method = RequestMethod.GET)
  public ModelAndView actualizarDIR3OficinasFechas(@RequestParam String fechaInicio,
      @RequestParam String fechaFin) {
    ModelAndView retorno = new ModelAndView(PRINCIPAL);
    MessageObject mensaje = null;
    try {

      Date dInicial = null;
      Date dFin = null;
      SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

      // si es vacia la fecha de inicio ponemos una inicial
      if (StringUtil.esVacioOrNull(fechaInicio)) {
        dInicial = objSDF.parse(FECHA_INICIO_01012010);
      }
      // si no es vacia ponemos la parametrizada en formato ddMMyyyy
      else {
        dInicial = objSDF.parse(fechaInicio);
      }

      // si es vacia la fecha de fin no ponemos ninguna, si no es la parametrizada en
      // formato ddMMyyyy
      if (!StringUtil.esVacioOrNull(fechaFin)) {
        dFin = objSDF.parse(fechaFin);
      }

      unidadOrganicaTask.loadOficinas(dInicial, dFin);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, mensaje);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_ERROR,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, e.getMessage());
    }

    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }

  /**
   * Metodo para actualizar dir3 con codigo (parametros obligatorios) Ej:
   * http://host:port/eeutil-misc/administracion/actualizarDIR3C?codigoUnidadOrganica=XXXXXXX
   * 
   * @param codigoUnidadOrganica codigo de la unidad organica
   * @return
   */
  @RequestMapping(value = "/actualizarDIR3C", method = RequestMethod.GET)
  public ModelAndView actualizarDIR3CodigioUnidadOrganica(
      @RequestParam String codigoUnidadOrganica) {
    ModelAndView retorno = new ModelAndView(PRINCIPAL);
    MessageObject mensaje = null;
    try {

      // si es vacio lanzamos excepcion
      if (StringUtil.esVacioOrNull(codigoUnidadOrganica)) {
        throw new EeutilException(
            "Parametro codigoUnidadOrganica requerido en la peticion: Formato codigoUnidadOrganica=valor");
      }

      unidadOrganicaTask.loadUnidadOrganica(codigoUnidadOrganica);
      unidadOrganicaTask.loadOficinas(codigoUnidadOrganica);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, mensaje);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_ERROR,
          messageSource.getMessage(ADMINISTRACION_ACTUALIZAR_DIR3_MSG, null, new Locale(ES_ES)));
      retorno.addObject(MENSAJE_USUARIO, e.getMessage());
    }

    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }

  /**
   * Tenemos una licencia para generar 360.000 paginas al a�o
   * 
   * @throws ParseException
   * 
   *         formato: /administracion/contarPDFA?fecha=01052019
   */
  @RequestMapping(value = "/contarPDFA", method = RequestMethod.GET)
  public ModelAndView contarPDFAGeneradosUnAno(@RequestParam String fecha) throws ParseException {
    ModelAndView retorno = new ModelAndView("administracion/contarPDFA");
    String resultado = null;

    // Parseamos la fecha para evitar ataques
    SimpleDateFormat format = new SimpleDateFormat(ddMMyyyy_MASK);
    Date parsed = format.parse(fecha);
    java.sql.Date dFecha = new java.sql.Date(parsed.getTime());

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parsed);
    calendar.add(Calendar.YEAR, 1);
    java.sql.Date dFecha1ano = new java.sql.Date(calendar.getTimeInMillis());

    EntityManager em = null;

    try {

      em = eeutilDao.getEntityManager();

      Query q = em.createNativeQuery(
          "select sum(petp.numero_paginas) from peticiones_pdfa petp where petp.fecha_peticion between ?1 and ?2");
      q.setParameter(1, dFecha);
      q.setParameter(2, dFecha1ano);

      BigDecimal res = (BigDecimal) q.getSingleResult();

      resultado = "El numero de paginas generadas por PDFTools desde " + dFecha.toLocalDate()
          + " hasta " + dFecha1ano.toLocalDate() + " es: " + res.toString() + " en este entorno";

    } catch (Exception e) {
      retorno.addObject(DATOS_PARAM, e.getMessage());
    } finally {
      if (em != null)
        em.close();
    }

    retorno.addObject(DATOS_PARAM, resultado);
    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }

  @RequestMapping(value = "/testtrazas", method = RequestMethod.GET)
  public ModelAndView testTrazas() throws ParseException {
    ModelAndView retorno = new ModelAndView("administracion/testtrazas");

    String resultado = "";
    String saltoLinea = "<br>";
    // eeutil-util
    IPruebaTraza iPT = new UtilidadesTestUtil();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-afirma-stub
    iPT = new UtilidadesTestAfirmawsStub();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-comunes
    iPT = new UtilidadesTestComunes();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-misc
    iPT = new UtilidadesTestEeutilMisc();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-model
    UtilidadesTestModel umodel = new UtilidadesTestModel();
    resultado += umodel.testModel() + saltoLinea;
    // eeutil-pdf-conversion-igae
    iPT = new UtilidadesTestPdfConversionIgae();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-schredule
    iPT = new UtilidadesTestSchredule();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // eeutil-services
    iPT = new UtilidadesTestServices();
    resultado += iPT.testTrazaImp() + saltoLinea;
    // load-tables
    iPT = new UtilidadesTestLoadTables();
    resultado += iPT.testTrazaImp() + saltoLinea;

    retorno.addObject(DATOS_PARAM, resultado);
    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }

  @RequestMapping(value = "/borrarCache", method = RequestMethod.GET)
  @Transactional
  public ModelAndView borrarCache() {
    ModelAndView retorno = new ModelAndView("administracion/borrarcache");
    String resultado = null;

    EntityManager em = null;

    try {

      em = entityManagerFactory.createEntityManager();

      em.getTransaction().begin();

      // marcamos la bbdd para limpiar la cache
      Query queryUpdate = em.createNativeQuery("UPDATE EEUTIL_CACHE set A_CACHEAR = ?1");
      queryUpdate.setParameter(1, 1);
      queryUpdate.executeUpdate();

      em.flush();
      em.getTransaction().commit();

      resultado =
          "Cache marcada para borrar correctamente<br>. Se borrara aproximadamente en 5 minutos.";
    } catch (Exception e) {
      resultado = "Problemas al marcar el borrado de cache<br>";
      resultado += e.getMessage();
      logger.error(e.getMessage(), e);
    } finally {
      if (em != null)
        em.close();
    }

    retorno.addObject(DATOS_PARAM, resultado);
    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;

  }

  @RequestMapping(value = "/healthycertificados", method = RequestMethod.GET)
  public ModelAndView healtycertificados() throws Exception {
    ModelAndView retorno = new ModelAndView("administracion/healthycertificados");

    Map<String, InfoCertModel> mapResultados =
        certificadoValidadorTask.healthyCertificadosBusiness();

    retorno.addObject(DATOS_PARAM, mapResultados);
    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());
    return retorno;
  }



}
