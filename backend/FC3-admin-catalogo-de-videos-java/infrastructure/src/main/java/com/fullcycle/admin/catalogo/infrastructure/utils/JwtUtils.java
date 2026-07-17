package com.fullcycle.admin.catalogo.infrastructure.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public final class JwtUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> CLAIMS_TYPE = new TypeReference<>() {
    };

    private JwtUtils() {
    }

    public record DecodedJwt(Map<String, Object> header, Map<String, Object> payload, String signature) {
    }

    public static DecodedJwt decode(final String token) {
        final var parts = split(token);
        final var header = readClaims(parts[0]);
        final var payload = readClaims(parts[1]);
        return new DecodedJwt(header, payload, parts[2]);
    }

    public static boolean isValid(final String token) {
        try {
            final var decoded = decode(token);
            return !isExpired(decoded) && !isNotYetValid(decoded);
        } catch (final RuntimeException e) {
            return false;
        }
    }

    public static boolean isExpired(final DecodedJwt jwt) {
        return getInstantClaim(jwt.payload(), "exp")
                .map(exp -> exp.isBefore(Instant.now()))
                .orElse(false);
    }

    public static boolean isNotYetValid(final DecodedJwt jwt) {
        return getInstantClaim(jwt.payload(), "nbf")
                .map(nbf -> nbf.isAfter(Instant.now()))
                .orElse(false);
    }

    private static String[] split(final String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token não pode ser nulo ou vazio");
        }

        final var parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "token JWT inválido: esperado 3 partes separadas por '.', encontrado " + parts.length);
        }

        return parts;
    }

    private static Map<String, Object> readClaims(final String base64UrlSegment) {
        final var json = new String(Base64.getUrlDecoder().decode(base64UrlSegment), StandardCharsets.UTF_8);
        try {
            return MAPPER.readValue(json, CLAIMS_TYPE);
        } catch (final Exception e) {
            throw new IllegalArgumentException("não foi possível interpretar o segmento do token como JSON", e);
        }
    }

    private static Optional<Instant> getInstantClaim(final Map<String, Object> claims, final String name) {
        final var value = claims.get(name);
        if (value instanceof Number number) {
            return Optional.of(Instant.ofEpochSecond(number.longValue()));
        }
        return Optional.empty();
    }
}
