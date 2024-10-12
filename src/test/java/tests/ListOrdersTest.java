package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.ListOrdersSteps;

import static org.apache.http.HttpStatus.*;

public class ListOrdersTest {

    private int courierId; // ID курьера для теста
    private int nearestStation; // ID ближайшей станции
    private int limit; // Максимальное количество заказов для получения
    private int page; // Номер страницы для пагинации
    ListOrdersSteps listofOrders = new ListOrdersSteps();

    @Before
    public void setUp() {
        // Инициализация перед запуском тестов
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierId = 387417;  // Задаем ID курьера
        nearestStation = 3; // Задаем ID ближайшей станции
        limit = 30;  // Устанавливаем лимит на количество заказов
        page = 0; // Устанавливаем номер страницы для запроса
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void listOrdersTest() {
        Response responseOrders = listofOrders.getOrderList(courierId, nearestStation, limit, page);
        listofOrders.compareResponseCode(responseOrders, SC_OK);
        listofOrders.checkSuccessGetOrders(responseOrders);
        listofOrders.checkOrdersSize(responseOrders);
    }
}
