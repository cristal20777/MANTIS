package ru.stqa.pft.appmanager.tests;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.appmanager.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class ChangePassword extends TestBase {
  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void changePassword() throws MessagingException, IOException {
    app.login().loginAdmin();
    app.login().openSidebar();
    app.login().managementUsers();
    String username = String.format(app.getDriver().findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText());
    String email2 = String.format(app.getDriver().
            findElement(By.xpath("//tbody/tr[2]/td[3]")).getText());
    String password2 = "root2";
    app.login().choiceUser(username);
    app.login().resetPassword();
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    String confirmationLink = findConfirmationLink(mailMessages, email2);
    app.registration().finish(confirmationLink, password2, username);
    assertTrue(app.newSession().login(username, password2));
  }

  private String findConfirmationLink (List < MailMessage > mailMessages, String email){
      MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
      VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
    }
  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop();
  }
  }
