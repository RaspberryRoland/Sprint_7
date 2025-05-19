package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.courier.Courier;
import models.courier.CourierCreds;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private final String API_V1_COURIER = "/api/v1/courier";
    private final String API_V1_COURIER_LOGIN = "/api/v1/courier/login";

    public CourierClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(API_V1_COURIER);
    }

    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(API_V1_COURIER + id);
    }

    public Response login(CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(API_V1_COURIER_LOGIN);
    }

    public Response login(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(API_V1_COURIER_LOGIN);
    }
}
