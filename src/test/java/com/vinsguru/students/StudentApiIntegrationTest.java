package com.vinsguru.students;

import static org.assertj.core.api.Assertions.assertThat;

import com.vinsguru.students.dto.StudentRequest;
import com.vinsguru.students.dto.StudentResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApiIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient client() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/students")
                .defaultStatusHandler(status -> true, (request, response) -> { })
                .build();
    }

    private StudentResponse create(String name, int age, String className) {
        return client().post()
                .body(new StudentRequest(name, age, className))
                .retrieve()
                .body(StudentResponse.class);
    }

    @Test
    @DisplayName("Should create a student and return 201")
    void shouldCreateStudent() {
        var response = client().post()
                .body(new StudentRequest("Alice", 20, "A1"))
                .retrieve()
                .toEntity(StudentResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Alice");
        assertThat(response.getBody().age()).isEqualTo(20);
        assertThat(response.getBody().className()).isEqualTo("A1");
    }

    @Test
    @DisplayName("Should return all students")
    void shouldReturnAllStudents() {
        StudentResponse created = create("Bob", 21, "B2");

        List<StudentResponse> all = client().get()
                .retrieve()
                .body(new ParameterizedTypeReference<List<StudentResponse>>() { });

        assertThat(all).contains(created);
    }

    @Test
    @DisplayName("Should return a student by id")
    void shouldReturnStudentById() {
        StudentResponse created = create("Carol", 22, "C3");

        StudentResponse found = client().get().uri("/{id}", created.id())
                .retrieve()
                .body(StudentResponse.class);

        assertThat(found).isEqualTo(created);
    }

    @Test
    @DisplayName("Should update a student")
    void shouldUpdateStudent() {
        StudentResponse created = create("Dave", 23, "D4");

        StudentResponse updated = client().put().uri("/{id}", created.id())
                .body(new StudentRequest("David", 24, "D5"))
                .retrieve()
                .body(StudentResponse.class);

        assertThat(updated).isEqualTo(new StudentResponse(created.id(), "David", 24, "D5"));
    }

    @Test
    @DisplayName("Should delete a student and return 204")
    void shouldDeleteStudent() {
        StudentResponse created = create("Eve", 25, "E5");

        var deleted = client().delete().uri("/{id}", created.id()).retrieve().toBodilessEntity();
        var after = client().get().uri("/{id}", created.id()).retrieve().toEntity(ProblemDetail.class);

        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(after.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should return 404 ProblemDetail when student id does not exist")
    void shouldReturn404WhenNotFound() {
        var response = client().get().uri("/{id}", 999999).retrieve().toEntity(ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail when request is invalid")
    void shouldReturn400WhenInvalid() {
        var response = client().post()
                .body(new StudentRequest(" ", 0, ""))
                .retrieve()
                .toEntity(ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }
}
