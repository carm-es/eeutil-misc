package es.mpt.dsic.eeutil.misc.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalController extends BaseController {

  protected static final Log logger = LogFactory.getLog(PrincipalController.class);

  @RequestMapping(value = "/principal", method = RequestMethod.GET)
  public ModelAndView principal() {
    logger.debug("Iniciado GInsidePrincipalController.principal");
    ModelAndView modelAndView = new ModelAndView("principal");

    modelAndView.addObject("versionmaven", getVersion());

    return modelAndView;
  }

}
