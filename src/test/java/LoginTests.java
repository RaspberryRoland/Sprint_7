import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.courier.Courier;
import models.courier.CourierCreds;
import models.courier.CourierId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static generators.CourierGenerator.randomCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static utils.Utils.randomString;

public class LoginTests {
    private final CourierClient courierClient = new CourierClient();
    private int id;
    private final String COURIER_NOT_FOUND = "Учетная запись не найдена";

    @Test
    @DisplayName("Login. Positive case. Login with correct login and password")
    public void loginWithCorrectLoginAndPassword() {
        Courier courier = randomCourier();
        courierClient.create(courier);
        Response response = courierClient.login(CourierCreds.credsFromCourier(courier));
        id = response.as(CourierId.class).getId();
        Assert.assertTrue(id > 0);
        Assert.assertEquals("Неверный статус код при авторизации с корректными данными",
                SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Login. Negative case. Login with incorrect login")
    public void loginWithIncorrectLogin() {
        CourierCreds courierCreds = new CourierCreds(randomString(),randomString());
        Response response = courierClient.login(courierCreds);
        id = response.as(CourierId.class).getId();
        response.then().assertThat().body("message", equalTo(COURIER_NOT_FOUND));
        Assert.assertEquals("Неверный статус код при авторизации с несуществующим логином",
                SC_NOT_FOUND, response.statusCode());
    }

    @Test
    @DisplayName("Login. Negative case. Login with incorrect password")
    public void loginWithIncorrectPassword() {
        Courier courier = randomCourier();
        courierClient.create(courier);
        courier.setPassword(randomString());
        Response response = courierClient.login(CourierCreds.credsFromCourier(courier));

        response.then().assertThat().body("message", equalTo(COURIER_NOT_FOUND));
        Assert.assertEquals("Неверный статус код при авторизации с неверным паролем",
                SC_NOT_FOUND, response.statusCode());
    }

    @Test
    @DisplayName("Login. Negative case. Login without login field")
    public void loginWithoutLoginFieldThatIsForbidden() {
        Courier courier = randomCourier();
        courierClient.create(courier);
        Response responseToGetIdForDeleteAfterTest = courierClient.login(CourierCreds.credsFromCourier(courier));
        id = responseToGetIdForDeleteAfterTest.as(CourierId.class).getId();

        Response response = courierClient.login("{\"password\":\"" + courier.getPassword() + "\"}");
        Assert.assertEquals("Неверный статус код при авторизации без обязательного поля login",
                SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Login. Negative case. Login without password field")
    public void loginWithoutPasswordFieldThatIsForbidden() {
        Courier courier = randomCourier();
        courierClient.create(courier);
        Response responseToGetIdForDeleteAfterTest = courierClient.login(CourierCreds.credsFromCourier(courier));
        id = responseToGetIdForDeleteAfterTest.as(CourierId.class).getId();

        Response response = courierClient.login("{\"login\":\"" + courier.getLogin() + "\"}");
        Assert.assertEquals("Неверный статус код при авторизации без обязательного поля password",
                SC_BAD_REQUEST, response.statusCode());
    }


    @After
    public void deleteCourier() {
        courierClient.delete(id);
    }
}
