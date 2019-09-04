/* WebDriver TEST using Selenium Webdriver
 ** Author: Aaditya Raj
 ** packaged: 04/09/19
 */
package edu.udacity.java.nano.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.PostConstruct;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WebSocketChatServerTest {

    @Autowired
    private Environment env;

    private static String BASE_URL = "http://localhost:8080";
    private static String USERNAME = "testUser";
    private static String CHAT_URL = "http://localhost:8080/index?username=" + USERNAME;

    private WebDriver webDriver;



    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @Test
    public void chatRoomLoginPage() {
        webDriver.get(BASE_URL);
        Assert.assertEquals(webDriver.getTitle(), "Chat Room Login");
        webDriver.close();
    }

    @Test
    public void chatRoomJoinedPage() {
        webDriver.get(BASE_URL);
        WebElement inputUsername = webDriver.findElement(By.id("username"));
        assertNotNull(inputUsername);
        inputUsername.sendKeys(USERNAME);
        WebElement loginButton = webDriver.findElement(By.className("submit"));
        loginButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String getCurrentUrl = webDriver.getCurrentUrl();
        Assert.assertEquals(getCurrentUrl, String.format(CHAT_URL, USERNAME));

        WebElement onlineUsers =  webDriver.findElement(By.className("chat-num"));
        Assert.assertEquals(onlineUsers.getText(), "1");
        webDriver.close();
    }

    @Test
    public void chatRoomSendMessage() {
        String message = "Some Dummy Text";
        webDriver.get(String.format(CHAT_URL, USERNAME));
        WebElement messageContainer = webDriver.findElement(By.className("message-container"));
        int msgContainerCount = messageContainer.findElements(By.className("mdui-card")).size();
        WebElement msgInputField = webDriver.findElement(By.className("mdui-textfield-input"));
        msgInputField.sendKeys(message);
        webDriver.findElement(By.id("send-msg")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expectedMessage = String.format("%s：%s", USERNAME, message);
//        String expectedMessage = USERNAME + "：" + message;
        List<WebElement> messagesList = messageContainer.findElements(By.className("message-content"));
        WebElement lastMessage = messagesList.get(messagesList.size() - 1);
        Assert.assertEquals(expectedMessage, lastMessage.getText());
        webDriver.close();
    }

    @Test
    public void chatRoomExit() {
        webDriver.get(String.format(CHAT_URL, USERNAME));
        WebElement exitApp = webDriver.findElement(By.id("exit-app"));
        exitApp.click();
        Assert.assertEquals(webDriver.getTitle(), "Chat Room Login");
        webDriver.close();
    }
}
