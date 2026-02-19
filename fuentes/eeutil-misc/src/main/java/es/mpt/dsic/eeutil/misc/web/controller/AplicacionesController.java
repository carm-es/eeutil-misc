package es.mpt.dsic.eeutil.misc.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.mpt.dsic.eeutil.misc.web.util.MessageObject;
import es.mpt.dsic.eeutil.misc.web.util.WebConstants;
import es.mpt.dsic.inside.dao.EeutilDao;
import es.mpt.dsic.inside.model.EeutilAplicacion;
import es.mpt.dsic.inside.model.aplicacion.AplicacionObject;
import es.mpt.dsic.inside.service.aplicacion.EeutilAplicacionService;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.loadTables.model.UnidadOrganica;

@Controller
public class AplicacionesController extends BaseController {

  private static final String CODIGO_UNIDAD_ORGANICA_PARAM = "codigoUnidadOrganica";

  private static final String MENSAJE_USUARIO_PARAM = "mensajeUsuario";

  private static final String IDENTIFICADOR_PARAM = "identificador";

  private static final String VERSIONMAVEN_PARAM = "versionmaven";

  protected static final Log logger = LogFactory.getLog(AplicacionesController.class);

  @Autowired
  private EeutilAplicacionService eeutilAplicacionService;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private EeutilDao eeutilDao;

  @RequestMapping(value = "/aplicacionesLista", method = RequestMethod.GET)
  public ModelAndView aplicacionesLista(@RequestParam(value = "app", required = true) String app,
      HttpServletRequest request, Locale locale) {
    ModelAndView retorno = null;


    if (app.equalsIgnoreCase("Eeutil")) {
      retorno = new ModelAndView("aplicaciones/lista");
      try {
        retorno.addObject("data", eeutilAplicacionService.getAplicaciones(app));
        retorno.addObject("app", app);

        retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

        // para ordenar por key
        Map<String, String> mapa = eeutilAplicacionService.getInfAdicional(app);
        Map<String, String> treeMap = new TreeMap<>(mapa);

        retorno.addObject("aditionalData", treeMap);

        String[] option = new String[1];
        option[0] = app;
        retorno.addObject("titulo",
            messageSource.getMessage("listaAplicaciones.title", option, locale));

      } catch (EeutilException e) {
        logger.error("Error al obtener aplicaciones");
      }
    }


    return retorno;
  }

  @RequestMapping(value = "/bajaAplicacion", method = RequestMethod.POST)
  public ModelAndView bajaAplicacion(HttpServletRequest request, Locale locale) {
    ModelAndView retorno = null;
    try {
      String app = request.getParameter("app");

      // damos de baja la aplicacion
      AplicacionObject aplicacion = new AplicacionObject();
      aplicacion.setIdentificador(request.getParameter(IDENTIFICADOR_PARAM));

      EeutilAplicacion apli = (EeutilAplicacion) eeutilDao.getObjeto(EeutilAplicacion.class,
          aplicacion.getIdentificador());
      if (apli.isActiva()) {
        eeutilAplicacionService.desactivar(app, aplicacion);
      } else {
        eeutilAplicacionService.activar(app, aplicacion);
      }

      retorno = aplicacionesLista(app, request, locale);

      String[] option = new String[1];
      option[0] = app;
      retorno.addObject("titulo",
          messageSource.getMessage("listaAplicaciones.title", option, locale));

      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    } catch (EeutilException e) {
      logger.error("Error al eliminar aplicacion");
    }
    return retorno;
  }

  @RequestMapping(value = "/eliminarAplicacion", method = RequestMethod.POST)
  public ModelAndView eliminarAplicacion(HttpServletRequest request, Locale locale) {
    ModelAndView retorno = null;
    String app = request.getParameter("app");
    try {

      // damos de baja la aplicacion
      AplicacionObject aplicacion = new AplicacionObject();
      aplicacion.setIdentificador(request.getParameter(IDENTIFICADOR_PARAM));
      eeutilAplicacionService.eliminarAplicacion(app, aplicacion, locale);

      retorno = aplicacionesLista(app, request, locale);

      MessageObject mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
          messageSource.getMessage("listaAplicaciones.eliminar.ok", null, locale));
      retorno.addObject(MENSAJE_USUARIO_PARAM, mensaje);
      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    } catch (EeutilException e) {
      logger.error("Error al eliminar aplicacion");
      retorno = aplicacionesLista(app, request, locale);
      MessageObject mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_ERROR, e.getMessage());
      retorno.addObject(MENSAJE_USUARIO_PARAM, mensaje);
    }

    return retorno;
  }

  @RequestMapping(value = "/altaAplicacion", method = RequestMethod.POST)
  public ModelAndView altaAplicacion(HttpServletRequest request, Locale locale) {
    ModelAndView retorno = null;
    try {
      String app = request.getParameter("app");

      String modoEscritura = request.getParameter("modoEscritura");

      MessageObject mensaje = null;

      // damos de baja la aplicacion
      AplicacionObject aplicacion = convertToObject(request);

      if (modoEscritura != null && "alta".equals(modoEscritura)) {
        // Si estamos en un modo alta, validamos que no exista ya la aplicacion de eeutils.
        EeutilAplicacion appEeutils = eeutilAplicacionService
            .getAplicacionEEUTIL(aplicacion != null ? aplicacion.getIdentificador() : null);

        if (appEeutils != null) {
          mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_WARNING,
              messageSource.getMessage("error.msg.datosNoValidos", null, locale));
          retorno = aplicacionesLista(app, request, locale);
          retorno.addObject(MENSAJE_USUARIO_PARAM, mensaje);
        }

      }
      if (retorno == null) {
        if (aplicacion != null) {
          eeutilAplicacionService.altaAplicacion(app, aplicacion);
          mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_SUCCESS,
              messageSource.getMessage("listaAplicaciones.alta.guardar.ok", null, locale));
        } else {
          mensaje = new MessageObject(WebConstants.MESSAGE_LEVEL_WARNING,
              messageSource.getMessage("error.msg.datosNoValidos", null, locale));
        }

      }

      retorno = aplicacionesLista(app, request, locale);
      retorno.addObject(MENSAJE_USUARIO_PARAM, mensaje);
      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    } catch (EeutilException e) {
      logger.error("Error crear aplicacion");
    }
    return retorno;
  }

  private AplicacionObject convertToObject(HttpServletRequest request) {
    if (StringUtils.isEmpty(request.getParameter(IDENTIFICADOR_PARAM))
        || StringUtils.isEmpty(request.getParameter("email"))
        || StringUtils.isEmpty(request.getParameter("responsable"))
        || StringUtils.isEmpty(request.getParameter("telefono"))
        || StringUtils.isEmpty(request.getParameter(CODIGO_UNIDAD_ORGANICA_PARAM))) {
      return null;
    } else {
      AplicacionObject retorno = new AplicacionObject();
      retorno.setIdentificador(request.getParameter(IDENTIFICADOR_PARAM));
      retorno.setPassword(request.getParameter("password"));
      retorno.setEmail(request.getParameter("email"));
      retorno.setResponsable(request.getParameter("responsable"));
      retorno.setTelefono(request.getParameter("telefono"));
      if (StringUtils.isNotEmpty(request.getParameter("serialNumberCertificado"))) {
        retorno.setSerialNumberCertificado(request.getParameter("serialNumberCertificado"));
      }
      if (StringUtils.isNotEmpty(request.getParameter("numeroProcedimiento"))) {
        retorno.setNumeroProcedimiento(request.getParameter("numeroProcedimiento"));
      }

      if (StringUtils.isNotEmpty(request.getParameter("aditional"))) {
        StringTokenizer tmpToken = new StringTokenizer(request.getParameter("aditional"), ";;");
        Map<String, String> aditionalData = new HashMap<>();
        while (tmpToken.hasMoreTokens()) {
          String[] datos = tmpToken.nextToken().split("=");
          if (datos.length > 1)
            aditionalData.put(datos[0], datos[1]);
          else
            aditionalData.put(datos[0], "");
        }
        retorno.setAditionalData(aditionalData);
      }
      if (StringUtils.isNotEmpty(request.getParameter(CODIGO_UNIDAD_ORGANICA_PARAM))
          && validateOrgano(request.getParameter(CODIGO_UNIDAD_ORGANICA_PARAM))) {
        retorno.setCodigoUnidadOrganica(request.getParameter(CODIGO_UNIDAD_ORGANICA_PARAM));
      }

      return retorno;
    }
  }

  private boolean validateOrgano(String organo) {
    boolean idValid = false;
    List<Criterion> criterias = new ArrayList<>();
    criterias.add(Restrictions.eq(CODIGO_UNIDAD_ORGANICA_PARAM, organo));
    UnidadOrganica unidadOrganica =
        (UnidadOrganica) eeutilDao.findObjeto(UnidadOrganica.class, criterias);
    if (unidadOrganica != null) {
      idValid = true;
    }
    return idValid;
  }


}
