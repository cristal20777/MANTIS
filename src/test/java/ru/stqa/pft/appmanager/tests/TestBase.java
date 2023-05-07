package ru.stqa.pft.appmanager.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import org.openqa.selenium.remote.BrowserType;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.appmanager.appmanager.ApplicationManager;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;


public class TestBase {



  protected static final ApplicationManager
          app = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

  @BeforeSuite
  public void setUp() throws Exception {
    app.init();
    app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php","config_inc.php.bak");
  }

  @AfterSuite
  public void tearDown() throws Exception {
    app.ftp().restore("config_inc.php.bak","config_inc.php");
    app.stop();

  }
  public void skipIfNotFixed(int issueId) throws MalformedURLException, ServiceException, RemoteException {
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }

  private boolean isIssueOpen(int issueId) throws RemoteException, MalformedURLException, ServiceException {
    MantisConnectPortType mc = app.soap().getMantisConnect();
    IssueData bug = mc.mc_issue_get
            ("administrator","root1", BigInteger.valueOf(issueId));
    String issueStatus = bug.getStatus().getName();
    if(issueStatus.equals("resolved")) {
      return false;
    } else {
      return true;
    }
  }
}
