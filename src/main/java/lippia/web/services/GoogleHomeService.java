package lippia.web.services;

import com.crowdar.core.PropertyManager;
import com.crowdar.core.actions.ActionManager;
import com.crowdar.driver.DriverManager;
import lippia.web.constants.GoogleConstants;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static com.crowdar.core.actions.WebActionManager.navigateTo;

public class GoogleHomeService extends ActionManager {

    public static void navegarWeb(){
        navigateTo(PropertyManager.getProperty("web.base.url"));
    }

    public static void enterSearchCriteria(String text) {
        setInput(GoogleConstants.INPUT_SEARCH_XPATH, text);
    }

    public static void clickSearchButton() {
        click(GoogleConstants.SEARCH_BUTTON_NAME);
    }

    public static void validar() throws UnsupportedEncodingException {
        System.setProperty("webdriver.chrome.driver", "ruta/al/chromedriver");
        WebDriver driver = new ChromeDriver();

        // URL de la página web
        String url = "https://www.ejemplo.com";
        driver.get(url);

        // Locator que deseas validar
        By locator = By.xpath("/html/body/div[2]/div/div[1]/div/div/div/div[1]/div/div[7]/div/section/div/div[2]/div/div[4]/div/div/div[2]");

        while (true) {
            WebElement element = driver.findElement(locator);
            if (element.isDisplayed()) {
                System.out.println("Locator encontrado en la página.");
            } else {
                System.out.println("Locator no encontrado en la página. Notificando al webhook...");

                // Envia una solicitud HTTP al webhook
                notifyWebhook();

                break;
            }

            // Espera 1 hora (3600000 milisegundos) antes de la siguiente verificación
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void notifyWebhook() throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://chat.googleapis.com/v1/spaces/AAAARcBFhCU/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=faVnGPwDhEYWZUURf6zgdxbTio2RKxbbaoCM-1YT3uk");
        httpPost.setEntity(new StringEntity("hola nico"));
        // Puedes configurar el cuerpo de la solicitud y encabezados según tu webhook
        // Ejemplo: httpPost.setHeader("Authorization", "Bearer tu_token");
        //         httpPost.setEntity(new StringEntity("Mensaje de notificación"));

        try {
            HttpResponse response = (HttpResponse) httpClient.execute(httpPost);
            int statusCode = response.statusCode();
            // Puedes procesar la respuesta del webhook si es necesario
            // Ejemplo: int statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
