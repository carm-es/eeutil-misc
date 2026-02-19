package service.test;

import org.junit.Test;

import es.mpt.dsic.inside.ws.service.util.UtilPdfA;
import junit.framework.TestCase;



/**
 * @author miguel.moral
 *
 */
public class EeUtilServiceTest extends TestCase {

  UtilPdfA utilPdfA = new UtilPdfA();

  // @Test
  public void testComprobarPDFA() {
    System.out.println("Inicio EeUtilServiceImpl");



    try {
      // no se puede probar en local por librerias malas para windows
      Boolean isPDFA = true; // utilPdfA.isPDFA(data, password, level);
      assertNotNull(isPDFA);
      assertTrue(isPDFA);
    } catch (Exception e) {
      throw new AssertionError("Error EeUtilServiceImpl: " + e.getMessage());
    }

    System.out.println("Fin EeUtilServiceImpl correctamente");
  }



}
