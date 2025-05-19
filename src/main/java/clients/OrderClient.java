package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.order.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final String API_V1_ORDER = "/api/v1/orders";
    private final String API_V1_CANCEL_ORDER = "/api/v1/orders/cancel";


    public OrderClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(API_V1_ORDER);
    }

    public Response cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(track)
                .when()
                .put(API_V1_CANCEL_ORDER);
    }

    public Response getOrderList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(API_V1_ORDER);
    }

}
