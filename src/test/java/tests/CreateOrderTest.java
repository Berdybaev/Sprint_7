package tests;

import Data.OrderJson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;


@RunWith(Parameterized.class) // Указываем, что тест будет параметризован
public class CreateOrderTest {

    private int trackId;
    OrderSteps orderSteps = new OrderSteps();

    @Parameterized.Parameter(0)
    public ArrayList<String> color;

    @Parameterized.Parameters(name = "Testing case #{index}: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {new ArrayList<>(List.of("BLACK"))},
                {new ArrayList<>(List.of("GREY"))},
                {new ArrayList<>(List.of("BLACK", "GREY"))},
                {new ArrayList<String>(List.of())},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Создание заказа с параметреизацией")
    public void createOrderTest() {
        OrderJson order = new OrderJson(color);
        Response orderResponse = orderSteps.createOrder(order);
        trackId = orderResponse.jsonPath().getInt("track"); // Извлекаем трек заказа
        orderSteps.compareResponseCode(orderResponse, SC_CREATED); // Проверка, что статус ответа 201 (создано)
        orderSteps.checkSuccessGetTrack(orderResponse); // Проверка, что трек заказа не null
    }

    @After
    public void tearDown() {
        orderSteps.tearDown(trackId);
    }
}
