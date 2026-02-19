package es.mpt.dsic.eeutil.misc.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.mpt.dsic.eeutil.misc.web.util.WebConstants;
import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.security.service.LoginBusinessService;
import es.mpt.dsic.inside.security.util.ConstantsClave;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.engine.xml.opensaml.SecureRandomXmlIdGenerator;

@Controller
@PropertySource("file:${eeutil-misc.config.path}/clave.properties")
public class LoginController extends BaseController {

  private static final String VERSIONMAVEN_PARAM = "versionmaven";

  @Autowired
  private Environment env;

  @Autowired
  private LoginBusinessService loginBusinessService;

  protected static final Log logger = LogFactory.getLog(LoginController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView init(HttpSession session) {
    ModelAndView retorno = new ModelAndView("login");
    if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
      retorno = new ModelAndView("principal");
    }

    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

    return retorno;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView loginGet(@RequestParam(value = "error", required = false) String error,
      HttpServletRequest request) {

    ModelAndView retorno = new ModelAndView("login");
    if (error != null) {
      ResourceBundle bundle = ResourceBundle.getBundle("messages", request.getLocale());
      retorno.addObject("error", bundle.getString(WebConstants.KEY_ERROR_LOGIN));
    }
    retorno.addObject(VERSIONMAVEN_PARAM, getVersion());
    return retorno;
  }

  @RequestMapping(value = "/loginRedirectClave", method = RequestMethod.POST)
  public ModelAndView loginClave(Model model, HttpSession session, HttpServletRequest request,
      RedirectAttributes redirectAttributes, Locale locale) {
    logger.debug("loginRedirectClave");

    ModelAndView retorno = new ModelAndView("login-redirect");


    try (FileInputStream fin = new FileInputStream(
        EeutilApplicationDataConfig.CONFIG_PATH + File.separator + ConstantsClave.SP_PROPERTIES)) {
      String claveServiceUrl = env.getProperty(ConstantsClave.SERVICE_URL);
      // String excludedIdPList = env.getProperty(Configuration.PROPERTY_EXCLUDED_IDPLIST);
      String forcedIdP = env.getProperty(ConstantsClave.PROPERTY_FORCED_IDP);

      // session.setAttribute(VERSION, versionValue);

      byte[] token;
      Properties props = new Properties();

      props.load(fin);
      token = loginBusinessService.generaTokenClave(claveServiceUrl, props);


      String SAMLRequest = EidasStringUtil.encodeToBase64(token);
      String samlRequestXML = new String(token);
      String relayState = SecureRandomXmlIdGenerator.INSTANCE.generateIdentifier(8);

      if (logger.isInfoEnabled()) {
        logger.debug(samlRequestXML);
      }

      model.addAttribute("claveServiceUrl", claveServiceUrl);
      // model.addAttribute(Configuration.ATRIBUTO_EXCLUDED_IDPLIST, excludedIdPList);
      model.addAttribute(ConstantsClave.ATRIBUTO_FORCED_IDP, forcedIdP);
      model.addAttribute(ConstantsClave.ATRIBUTO_SAML_REQUEST, SAMLRequest);
      model.addAttribute(ConstantsClave.ATRIBUTO_RELAY_STATE, relayState);

      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());

      return retorno;

    } catch (EeutilException e) {
      logger.error("Error: " + e.toString());
      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());
      retorno.setViewName("login?error=Error acceso a cl@ve");
      return retorno;

    } catch (Exception e) {
      logger.error("Error: " + e.toString());
      retorno.addObject(VERSIONMAVEN_PARAM, getVersion());
      retorno.setViewName("login?error=Error acceso a cl@ve");
      return retorno;
    }


  }


}
