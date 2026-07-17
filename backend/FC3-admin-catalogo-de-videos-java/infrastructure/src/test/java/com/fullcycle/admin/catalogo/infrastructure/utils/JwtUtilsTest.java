package com.fullcycle.admin.catalogo.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

public class JwtUtilsTest extends UnitTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String SAMPLE_TOKEN =
            "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDX085M3pGbGMxempqZmNzRnoyc1BSemRseVZYbWRjbF8tY1dLdlp1LUtjIn0"
                    + ".eyJleHAiOjE3ODQxNTQxNDMsImlhdCI6MTc4NDE1Mzg0MywianRpIjoiZTA4MjhhNzEtMDllYS00NDcxLTg0N2QtM2Y2YjYxMWViYzdhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL3JlYWxtcy9mYzMtY29kZWZsaXgiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzU2YWRjNDQtYTYwNC00YzlhLTk2M2MtOGE5MDQwZWI5MGQxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZmMzLWFkbWluLWNhdGFsb2dvIiwic2Vzc2lvbl9zdGF0ZSI6IjRmOWUwODIwLWQ3YmEtNDY4ZS05M2JkLWI0NzI4MjZlOTc3MCIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImNhdGFsb2dvX2FkbWluIiwiZGVmYXVsdC1yb2xlcy1mYzMtY29kZWZsaXgiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI0ZjllMDgyMC1kN2JhLTQ2OGUtOTNiZC1iNDcyODI2ZTk3NzAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IldhZ25lciBTaWx2YSIsInByZWZlcnJlZF91c2VybmFtZSI6ImRlcGxveV9zZXh0YSIsImdpdmVuX25hbWUiOiJXYWduZXIiLCJmYW1pbHlfbmFtZSI6IlNpbHZhIiwiZW1haWwiOiJ2dmFnbmVyLnNpbHZhQGdtYWlsLmNvbSJ9"
                    + ".A8oQTBQKxvz5h2aguODtSiMUjnkQESHHKo-_-xtCECT75OmmZcweO-qWq3mHF-EFVIMd5u2BRl_t5TxV0evCoN_jbsqTkvKz-NIoIwcrbahzzP5nAZS9h95blf1pb5xCAMIvMeuHUJjbyVbpKp6vy9KFd2ibr1_-dmRi3IDEWwA2rzUoT3et7_oUuFoh6sfCV2_bE_5LToVBL7Pg4b75c5sBiI7XPbGcoEl589KVoXHBqeJkd1l3FcTYYJDa-0jfLqLJXxfwo8P40ZJvPa4InfIoT71XgOVMclaqtG8ia-CrKQbHKFOdWLwyA7S7FupssPCAqCQZneltp5mlO5Nj1g";

    @Test
    public void givenValidToken_whenCallDecode_shouldReturnHeaderAndPayloadClaims() {
        final var expectedAlg = "RS256";
        final var expectedKid = "C_O93zFlc1zjjfcsFz2sPRzdlyVXmdcl_-cWKvZu-Kc";
        final var expectedIssuer = "http://localhost:8443/realms/fc3-codeflix";
        final var expectedUsername = "deploy_sexta";
        final var expectedEmail = "vvagner.silva@gmail.com";

        final var actualDecoded = JwtUtils.decode(SAMPLE_TOKEN);

        Assertions.assertEquals(expectedAlg, actualDecoded.header().get("alg"));
        Assertions.assertEquals(expectedKid, actualDecoded.header().get("kid"));
        Assertions.assertEquals(expectedIssuer, actualDecoded.payload().get("iss"));
        Assertions.assertEquals(expectedUsername, actualDecoded.payload().get("preferred_username"));
        Assertions.assertEquals(expectedEmail, actualDecoded.payload().get("email"));
        Assertions.assertNotNull(actualDecoded.signature());
        Assertions.assertFalse(actualDecoded.signature().isBlank());
    }

    @Test
    public void givenTokenWithFutureExpiration_whenCallIsValid_shouldReturnTrue() {
        final var expectedExp = Instant.now().plusSeconds(3600).getEpochSecond();
        final var actualToken = newToken(Map.of("exp", expectedExp));

        final var actualIsValid = JwtUtils.isValid(actualToken);

        Assertions.assertTrue(actualIsValid);
    }

    @Test
    public void givenExpiredToken_whenCallIsValid_shouldReturnFalse() {
        final var expectedExp = Instant.now().minusSeconds(3600).getEpochSecond();
        final var actualToken = newToken(Map.of("exp", expectedExp));

        final var actualIsValid = JwtUtils.isValid(actualToken);

        Assertions.assertFalse(actualIsValid);
    }

    @Test
    public void givenTokenNotYetValid_whenCallIsValid_shouldReturnFalse() {
        final var expectedExp = Instant.now().plusSeconds(7200).getEpochSecond();
        final var expectedNbf = Instant.now().plusSeconds(3600).getEpochSecond();
        final var actualToken = newToken(Map.of("exp", expectedExp, "nbf", expectedNbf));

        final var actualIsValid = JwtUtils.isValid(actualToken);

        Assertions.assertFalse(actualIsValid);
    }

    @Test
    public void givenTokenWithoutThreeParts_whenCallIsValid_shouldReturnFalse() {
        final var actualToken = "header.payload";

        final var actualIsValid = JwtUtils.isValid(actualToken);

        Assertions.assertFalse(actualIsValid);
    }

    @Test
    public void givenTokenWithInvalidBase64Segment_whenCallIsValid_shouldReturnFalse() {
        final var actualToken = "###.###.###";

        final var actualIsValid = JwtUtils.isValid(actualToken);

        Assertions.assertFalse(actualIsValid);
    }

    @Test
    public void givenNullToken_whenCallDecode_shouldThrowsIllegalArgumentException() {
        final String expectedToken = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> JwtUtils.decode(expectedToken));
    }

    @Test
    public void givenTokenWithMalformedJsonSegment_whenCallDecode_shouldThrowsIllegalArgumentException() {
        final var actualToken = "###.###.###";

        Assertions.assertThrows(IllegalArgumentException.class, () -> JwtUtils.decode(actualToken));
    }

    private String newToken(final Map<String, Object> payloadClaims) {
        final Map<String, Object> header = Map.of("alg", "none", "typ", "JWT");
        return encode(header) + "." + encode(payloadClaims) + ".signature";
    }

    private String encode(final Map<String, Object> claims) {
        try {
            final var json = MAPPER.writeValueAsBytes(claims);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
