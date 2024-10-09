import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

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
        listofOrders.getOrderList(courierId, nearestStation, limit, page);
    }
}
