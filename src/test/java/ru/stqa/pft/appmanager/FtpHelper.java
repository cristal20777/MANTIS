package ru.stqa.pft.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {
  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app;
    ftp = new FTPClient();
  }

  public void restore(String backup, String target) throws IOException {
    ftp.connect(app.getProperty("ftp.host"));
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile("/config/" + target);
    ftp.rename("/config/" + backup, "/config/" + target);
    ftp.disconnect();
  }

  public void upload(File file,String target, String backup) throws IOException {
    ftp.connect(app.getProperty("ftp.host"));
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile("/config/" + backup);
    ftp.rename("/config/" + target, "/config/" + backup);
    ftp.enterLocalPassiveMode();
    ftp.storeFile("/config/" + target, new FileInputStream(file));
    ftp.disconnect();

  }
}
