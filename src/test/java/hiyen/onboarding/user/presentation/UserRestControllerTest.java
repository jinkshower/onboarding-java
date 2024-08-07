package hiyen.onboarding.user.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import hiyen.onboarding.common.AcceptanceTest;
import hiyen.onboarding.user.presentation.request.UserSignRequest;
import hiyen.onboarding.user.presentation.request.UserSignupRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserRestControllerTest extends AcceptanceTest {

    @Nested
    @DisplayName("회원 가입")
    class Signup {

        final String username = "testuser123";
        final String password = "Password1";
        final String nickname = "jinkshower";

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final UserSignupRequest request = new UserSignupRequest(username, password, nickname);

            // when
            final ExtractableResponse<Response> response = signup(request);

            // then
            assertThat(response.statusCode()).isEqualTo(201);
            assertThat(response.header("Location")).contains("/api/users/");
        }

        @ParameterizedTest
        @DisplayName("4자 이상 20자 이하가 아닌 이름으로 가입시 실패한다.")
        @ValueSource(strings = {"", "ing", "oirifininwiqnijidasinginfdingi"})
        void fail_wrongUsername(final String wrongUsername) {
            // given
            final UserSignupRequest request = new UserSignupRequest(wrongUsername, password, nickname);

            // when
            final ExtractableResponse<Response> response = signup(request);

            // then
            assertThat(response.statusCode()).isEqualTo(400);
        }

        @ParameterizedTest
        @DisplayName("4자 이상 소문자, 대문자, 숫자 조합이 아닌 비밀번호로 가입시 실패한다.")
        @ValueSource(strings = {"", "p2P", "PASSWORD", "password!", "PASSWORD1", "password1", "PASSWORD1!"})
        void fail_wrongPassword(final String wrongPassword) {
            // given
            final UserSignupRequest request = new UserSignupRequest(username, wrongPassword, nickname);

            // when
            final ExtractableResponse<Response> response = signup(request);

            // then
            assertThat(response.statusCode()).isEqualTo(400);
        }

        @ParameterizedTest
        @DisplayName("2자 이상 10자 이하가 아닌 닉네임으로 가입시 실패한다.")
        @ValueSource(strings = {"", "s", "tignibimmg1"})
        void fail_wrongNickname(final String wrongNickname) {
            // given
            final UserSignupRequest request = new UserSignupRequest(username, password, wrongNickname);

            // when
            final ExtractableResponse<Response> response = signup(request);

            // then
            assertThat(response.statusCode()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        final String username = "testuser123";
        final String password = "Password1";
        final String nickname = "jinkshower";

        @BeforeEach
        void setUp() {
            final UserSignupRequest request = new UserSignupRequest(username, password, nickname);
            signup(request);
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final UserSignRequest request = new UserSignRequest(username, password);

            // when
            ExtractableResponse<Response> response = sign(request);

            // then
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.header("Authorization")).isNotNull();
        }
    }

    private ExtractableResponse<Response> signup(final UserSignupRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType("application/json")
                .when().post("/api/users/signup")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> sign(final UserSignRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType("application/json")
                .when().post("/api/users/sign")
                .then().log().all()
                .extract();
    }
}
