package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

    private static final int RESTAURANTE_ID_INEXISTENTE = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    public RestauranteRepository restauranteRepository;

    @Autowired
    public CozinhaRepository cozinhaRepository;

    int quantidadeRestaurantesCadastrados;
    private String bodyJsonRestauranteInputCorreto;

    private String bodyJsonRestauranteComCozinhaInexistente;

    private String bodyJsonRestauranteSemNome;

    private String bodyJsonRestauranteTaxaFreteInvalido;

    private Restaurante restauranteZen;

    @BeforeEach
    public void setUp() {
        enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        basePath = "/restaurantes";

        bodyJsonRestauranteInputCorreto = ResourceUtils.getContentFromResource("/json/restauranteInputCorreto.json");
        bodyJsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource("/json/restauranteComCozinhaInexistente.json");
        bodyJsonRestauranteSemNome = ResourceUtils.getContentFromResource("/json/restauranteSemNome.json");
        bodyJsonRestauranteTaxaFreteInvalido = ResourceUtils.getContentFromResource("/json/restauranteTaxaFreteInvalido.json");

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    public void deveRetornarStatus200_QuandoListarRestaurantes() {

        given()
                .accept(ContentType.JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoBuscarRestauranteExistente() {

        given()
                .accept(ContentType.JSON)
                .pathParam("restauranteId", restauranteZen.getId())
        .when()
                .get("/{restauranteId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(restauranteZen.getNome()));

    }

    @Test
    public void deveRetornarStatus404_QuandoBuscarRestauranteInexistente() {

        given()
                .accept(ContentType.JSON)
                .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
        .when()
                .get("/{restauranteId}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void deveRetornarCreated_QuandoIncluirRestauranteComDadosCorretos() {

        given()
                .accept(ContentType.JSON)
                .body(bodyJsonRestauranteInputCorreto)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void deveRetornarBadRequest_QuandoCadastrarRestauranteComCozinhaInexistente() {

        given()
                .accept(ContentType.JSON)
                .body(bodyJsonRestauranteComCozinhaInexistente)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void deveRetornarBadRequest_QuandoCadastrarRestauranteSemNome() {

        given()
                .accept(ContentType.JSON)
                .body(bodyJsonRestauranteSemNome)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void deveRetornarBadRequest_QuandoCadastrarRestauranteTaxaFreteInvalido() {

        given()
                .accept(ContentType.JSON)
                .body(bodyJsonRestauranteTaxaFreteInvalido)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    private void prepararDados() {

        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Japonesa");

        cozinha = cozinhaRepository.save(cozinha);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Zen");
        restaurante.setTaxaFrete(BigDecimal.valueOf(10));
        restaurante.setCozinha(cozinha);

        restauranteZen = restauranteRepository.save(restaurante);
    }


}
