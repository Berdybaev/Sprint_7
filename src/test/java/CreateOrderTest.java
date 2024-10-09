import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

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
        trackId = orderSteps.createOrder(color);
    }

    @After
    public void tearDown() {
        orderSteps.tearDown(trackId);
    }
}
