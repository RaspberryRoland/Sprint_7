import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.courier.Courier;
import models.courier.CourierId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static generators.CourierGenerator.randomCourier;
import static models.courier.CourierCreds.credsFromCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class CreateCourierTests {

    private final CourierClient courierClient = new CourierClient();
    private final String NOT_ENOUGH_DATA_TO_CREATE_COURIER = "Недостаточно данных для создания учетной записи";
    private int id;

    @Test
    @DisplayName("Create Courier. Positive case. Check creation of courier")
    public void createCourier() {
        Courier courier = randomCourier();

        Response response = courierClient.create(courier);
        Assert.assertEquals("Неверный статус-код при создании курьера", SC_CREATED, response.statusCode());

        response.then().assertThat().body("ok", equalTo(true));

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        id = loginResponse.as(CourierId.class).getId();

        Assert.assertEquals("Неверный статус-код при логине", SC_OK, loginResponse.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with the same login")
    public void createCouriersWithTheSameLoginThatIsForbidden() {
        Courier courier = randomCourier();
        String courierLogin = courier.getLogin();
        Response response = courierClient.create(courier);
        id = response.as(CourierId.class).getId();

        Courier courierWithTheSameLogin = randomCourier();
        courierWithTheSameLogin.setLogin(courierLogin);
        Response errorResponse = courierClient.create(courierWithTheSameLogin);
        Assert.assertEquals("Неверный статус-код при создании курьера с существующим логином",
                SC_CONFLICT, errorResponse.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with only login field")
    public void createCourierWithOnlyLoginFieldThatIsForbidden() {
        Courier courierWithOnlyLoginField = new Courier()
                .setLogin(randomString());
        Response response = courierClient.create(courierWithOnlyLoginField);
        response.then().assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_COURIER));
        Assert.assertEquals("Неверный статус-код при создании курьера без обязательных полей password и firstName",
                SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with only password field")
    public void createCourierWithOnlyPasswordFieldThatIsForbidden() {
        Courier courierWithOnlyPasswordField = new Courier()
                .setPassword(randomString());
        Response response = courierClient.create(courierWithOnlyPasswordField);
        response.then().assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_COURIER));
        Assert.assertEquals("Неверный статус-код при создании курьера без обязательных полей login и firstName",
                SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with only firstName field")
    public void createCourierWithOnlyFirstNameFieldThatIsForbidden() {
        Courier courierWithOnlyFirstNameField = new Courier()
                .setFirstName(randomString());
        Response response = courierClient.create(courierWithOnlyFirstNameField);
        response.then().assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_COURIER));
        Assert.assertEquals("Неверный статус-код при создании курьера без обязательных полей login и password",
                SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with only login and firstName fields")
    public void createCourierWithOnlyLoginAndFirstNameFieldsThatIsForbidden() {
        Courier courierWithOnlyLoginAndFirstNameFields = new Courier()
                .setLogin(randomString())
                .setFirstName(randomString());
        Response response = courierClient.create(courierWithOnlyLoginAndFirstNameFields);
        response.then().assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_COURIER));
        Assert.assertEquals("Неверный статус-код при создании курьера без обязательного поля password", SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of courier with only password and firstName fields")
    public void createCourierWithOnlyPasswordAndFirstNameFieldsThatIsForbidden() {
        Courier courierWithOnlyPasswordAndFirstNameFields = new Courier()
                .setLogin(randomString())
                .setFirstName(randomString());
        Response response = courierClient.create(courierWithOnlyPasswordAndFirstNameFields);
        response.then().assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_COURIER));
        Assert.assertEquals("Неверный статус-код при создании курьера без обязательного поля login", SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Create Courier. Negative case. Check creation of same courier")
    public void createSameCouriersThatIsForbidden() {
        Courier courier = randomCourier();
        courierClient.create(courier);
        Response response = courierClient.create(courier);
        Assert.assertEquals("Неверный статус-код при создании одинаковых курьеров", SC_CONFLICT, response.statusCode());

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        id = loginResponse.as(CourierId.class).getId();
    }

    @After
    public void deleteCourier() {
        courierClient.delete(id);
    }
}
