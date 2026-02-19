package runtime.xmlsec;

import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.xml.security.utils.I18n;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class XMLSecTest {

  // java.lang.NoSuchMethodError:
  // org.apache.xml.security.utils.I18n.init(Ljava&#47;util&#47;ResourceBundle;)V
  @Test
  public void test(final @Mocked ResourceBundle mockResBundle) {
    Locale locale = new Locale("es", "ES");
    // ResourceBundle mockResBundle = ResourceBundle.getBundle("mock.mock", locale);
    I18n.init(mockResBundle);

    assertTrue(true);
  }
}
