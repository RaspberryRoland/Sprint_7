import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.order.Order;
import models.order.OrderTrack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrderTests {
    private final OrderClient orderClient = new OrderClient();
    private int track;

    //Имя заказчика, записывается в поле firstName таблицы Orders
    private final String firstName;

    //Фамилия заказчика, записывается в поле lastName таблицы Orders
    private final String lastName;

    //Адрес заказчика, записывается в поле adress таблицы Orders
    private final String address;

    //Ближайшая к заказчику станция метро, записывается в поле metroStation таблицы Orders
    private final String metroStation;

    //Телефон заказчика, записывается в поле phone таблицы Orders
    private final String phone;

    //Количество дней аренды, записывается в поле rentTime таблицы Orders
    private final int rentTime;

    //Дата доставки, записывается в поле deliveryDate таблицы Orders
    private final String deliveryDate;

    //Комментарий от заказчика, записывается в поле comment таблицы Orders
    private final String comment;

    //Предпочитаемые цвета, записываются в поле color таблицы Orders
    private final String[] color;

    public CreateOrderTests(String firstName,
                            String lastName,
                            String address,
                            String metroStation,
                            String phone,
                            int rentTime,
                            String deliveryDate,
                            String comment,
                            String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {"Том", "Рэдл", "ул. Слизерина 1", "Станция Запретный лес", "+7 927 547 69 62", 1, "2025-08-09", "Авада Кедабра", new String[]{"BLACK"}},
                {"Драко", "Малфой", "ул. Слизерина 13", "Станция Азкабан", "89275476923", 2, "2025-11-12", "Круцио", new String[]{"GREY"}},
                {"Гарри", "Поттер", "ул. Гриффиндора 1", "Станция Достоевского", "89098547692", 3, "2025-06-07", "Экспекто Патронум", new String[]{"BLACK, GREY"}},
                {"Рон", "Уизли", "ул. Гриффиндора 7", "Станция Левиоса", "+79098547692", 4, "2025-06-06", "Левиоса", new String[]{"GREY, BLACK"}},
                {"Гермиона", "Грейнджер", "ул. Гриффиндора 22", "Станция Отличников", "83472122334", 5, "2025-11-12", "Алохомора", new String[]{""}}
        };
    }

    @Test
    @DisplayName("Create Order. Check creation of orders with dif params")
    public void createOrder() {
        Order order = new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
        Response response = orderClient.createOrder(order);

        track = response.as(OrderTrack.class).getTrack();
        Assert.assertTrue(track > 0);
        Assert.assertEquals("Неверный статус код при создании заказа", SC_CREATED, response.statusCode());

    }


    @After
    public void cancelOrder() {
        orderClient.cancelOrder(track);
    }
}
