import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class WordTest {
    public static void main(String[] a) {
      try {
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().open(new File(System.getProperty("user.dir")+ "/Argomenti/a.doc"));
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
}
