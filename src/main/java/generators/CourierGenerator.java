package generators;

import io.qameta.allure.Step;
import models.courier.Courier;

import static utils.Utils.randomString;

public class CourierGenerator {
    @Step("Generate courier with random login, password and firstName")
    public static Courier randomCourier() {
        return new Courier()
                .setLogin(randomString())
                .setPassword(randomString())
                .setFirstName(randomString());
    }

}
