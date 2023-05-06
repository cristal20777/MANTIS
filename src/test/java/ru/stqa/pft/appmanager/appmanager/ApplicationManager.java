package ru.stqa.pft.appmanager.appmanager;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
  private final Properties properties;
  private WebDriver wd;
  private String browser;
  private RegistrationHelper registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private JamesHelper jamesHelper;
  private LoginHelper loginHelper;


  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }
public FtpHelper ftp() {
  if (ftp == null) {
    ftp = new FtpHelper(this);
  }
  return ftp;
}

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  public void stop() {

    if (wd != null) {
      wd.quit();
    }
  }

  public HttpSession newSession() {
    return new HttpSession(this);

  }
  public MailHelper  mail() {
    if (mailHelper == null) {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }
  public JamesHelper james() {
    if(jamesHelper == null) {
      jamesHelper = new JamesHelper(this);
    }return jamesHelper;
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public RegistrationHelper registration() {
    if (registrationHelper == null) {
      registrationHelper = new RegistrationHelper(this);
    }
    return registrationHelper;
  }

  public WebDriver getDriver() {
    if (wd == null) {
      if (browser.equals(BrowserType.CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.FIREFOX)) {
        wd = new FirefoxDriver();
      }
      wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseURL"));
    }
    return wd;
  }
  public LoginHelper login(){
    if (loginHelper == null) {
      loginHelper = new LoginHelper(this);
    }
    return loginHelper;
  }

  public String getUsername() {
    String user = String.format(getDriver().findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText());
    return user;
  }

  public String getEmail() {
    String email = String.format(getDriver().
            findElement(By.xpath("//tbody/tr[2]/td[3]")).getText());
    return email;
  }
}
