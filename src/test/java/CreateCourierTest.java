import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;

public class CreateCourierTest {

    public static final String COURIER_PATH = "/api/v1/courier";
    private String login;
    private String password;
    private String firstName;
    private int courierId; // переменная для хранения ID созданного курьера
    CreateCourierSteps courierSteps = new CreateCourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        login = "Lio";
        password = "122333";
        firstName = "Tapolski";
    }

    @After
    public void deleteCourier() {
        courierSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("1. Курьера можно создать")
    @Description("Создание курьера")
    public void createCourierTest() {
        Response response = courierSteps.createCourier(login, password, firstName);
        courierSteps.compareResponseCode(response, 201);
        courierSteps.checkSuccessResponse(response, true);
        courierId = courierSteps.loginCourierAndGetId(login, password); // Получаем ID курьера для последующего удаления
    }

    @Test
    @DisplayName("2. Нельзя создать двух одинаковых курьеров")
    @Description("Отправка тех же данных при котором создался курьер для проверки дубляжа")
    public void DuplicateAccountTest() {
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.loginCourierAndGetId(login, password);

        Response duplicateCourier = courierSteps.createCourier(login, password, firstName);
        courierSteps.compareResponseCode(duplicateCourier, 409);
        courierSteps.checkErrorMessage(duplicateCourier, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("3.Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    @Description("Попытка создания аккаунта без логина затем без пароля")
    public void requiredFieldsTest() {
        // Попытка создания курьера без логина
        Response withoutLogin = courierSteps.createCourierWithoutLogin(password, firstName);
        courierSteps.compareResponseCode(withoutLogin, 400);
        courierSteps.checkErrorMessage(withoutLogin, "Недостаточно данных для создания учетной записи");

        Response withoutPassword = courierSteps.createCourierWithoutPassword(login, firstName);
        courierSteps.compareResponseCode(withoutPassword, 400);
        courierSteps.checkErrorMessage(withoutPassword, "Недостаточно данных для создания учетной записи");
    }

    // Попытка создания курьера без пароля
    @Test
    @DisplayName("4. Если создать пользователя с логином, который уже есть, возвращается ошибка.")
    @Description("Создание курьера c тем же логином")
    public void createCourierDuplicateLoginTest() {
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.loginCourierAndGetId(login, password); // Получаем ID курьера для последующего удаления
        // Попытка создания курьера c тем же логином
        Response duplicateLogin = courierSteps.createCourier(login, "54321", "Forest Gump");
        courierSteps.compareResponseCode(duplicateLogin, 409);
        courierSteps.checkErrorMessage(duplicateLogin, "Этот логин уже используется. Попробуйте другой.");
    }
}