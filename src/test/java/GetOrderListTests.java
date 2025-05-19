import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.order.Order;
import models.order.OrderTrack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTests {

    private final OrderClient orderClient = new OrderClient();
    private int track;

    @Test
    @DisplayName("Get order. Check status code and body ")
    public void getOrdersList() {
        Order order = new Order(
                "Ян",
                "Декс",
                "ул. Яндекса 14",
                "Станция Яндекса",
                "8 913 33 10 203",
                10,
                "2025-10-10",
                "comment",
                new String[]{"BLACK"});
        Response response = orderClient.createOrder(order);
        track = response.as(OrderTrack.class).getTrack();

        response = orderClient.getOrderList();

        response.then().assertThat().body("orders", notNullValue());
        Assert.assertEquals("Неверный статус код при получении списка заказов", SC_OK, response.statusCode());
    }

    @After
    public void deleteCourier() {
        orderClient.cancelOrder(track);
    }
}
