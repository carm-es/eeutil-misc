package es.mpt.dsic.inside.utils.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SLF4JLocationAwareLog;

public class UtilidadesTestEeutilMisc implements IPruebaTraza {
  protected static final Log logger = LogFactory.getLog(UtilidadesTestEeutilMisc.class);


  public String testTrazaImp() {
    boolean bImpSLF4JLocationAwareLog = false;

    if (logger instanceof SLF4JLocationAwareLog) {
      bImpSLF4JLocationAwareLog = true;
    }
    logger.error("Is implementation SLF4JLocationAwareLog " + bImpSLF4JLocationAwareLog
        + " Is error enabled: " + logger.isErrorEnabled()
        + " Verificacion de que la traza en eeutil-misc es correcta");

    return "Is implementation SLF4JLocationAwareLog " + bImpSLF4JLocationAwareLog
        + " Is error enabled: " + logger.isErrorEnabled()
        + " Verificacion de que la traza en eeutil-misc es correcta";
  }
}
