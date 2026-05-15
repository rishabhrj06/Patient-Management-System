import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientIntegrationTest {

    private static String token;
    private static String patientId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4010";

        String loginData = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """;

        token = given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    @Order(1)
    public void shouldCreatePatientSuccessfully() {

        String patientRequest = """
                {
                    "name": "Shakalakaka Prasad Singh",
                    "email": "shakalakaka@gmail.com",
                    "address": "Jodhpur Rajasthan",
                    "dateOfBirth": "2003-10-15"
                }
                """;

        patientId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(patientRequest)
                .when()
                .post("/api/patients")
                .then()
                .log()
                .all()
                .statusCode(201)
                .body("name", equalTo("Shakalakaka Prasad Singh"))
                .body("email", equalTo("shakalakaka@gmail.com"))
                .body("address", equalTo("Jodhpur Rajasthan"))
                .body("id", notNullValue())
                .extract()
                .jsonPath()
                .getString("id");
    }

    @Test
    @Order(2)
    public void shouldReturnAllPatientsWhenValidTokenIsProvided() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patients")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    @Order(3)
    public void shouldReturnUnauthorizedWhenTokenIsMissing() {

        given()
                .when()
                .get("/api/patients")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(4)
    public void shouldReturnUnauthorizedWhenTokenIsInvalid() {

        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/patients")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(5)
    public void shouldReturnBadRequestWhenPatientDataIsInvalid() {

        String invalidPatientRequest = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "address": "",
                    "dateOfBirth": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(invalidPatientRequest)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(6)
    public void shouldUpdatePatientSuccessfully() {

        String updatedPatientRequest = """
                {
                    "name": "Updated Rishabh",
                    "email": "updated@test.com",
                    "address": "Updated Address",
                    "dateOfBirth": "2003-10-15"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(updatedPatientRequest)
                .when()
                .put("/api/patients/" + patientId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Rishabh"))
                .body("email", equalTo("updated@test.com"));
    }

    @Test
    @Order(7)
    public void shouldReturnBadRequestWhenUpdatingPatientWithInvalidUuid() {

        String updatedPatientRequest = """
                {
                    "name": "Updated Rishabh",
                    "email": "updated@test.com",
                    "address": "Updated Address",
                    "dateOfBirth": "2003-10-15"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(updatedPatientRequest)
                .when()
                .put("/api/patients/invalid-uuid")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(8)
    public void shouldDeletePatientSuccessfully() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/patients/" + patientId)
                .then()
                .statusCode(200)
                .body(containsString("deleted successfully"));
    }

    @Test
    @Order(9)
    public void shouldReturnBadRequestWhenDeletingPatientWithInvalidUuid() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/patients/invalid-uuid")
                .then()
                .statusCode(400);
    }
}