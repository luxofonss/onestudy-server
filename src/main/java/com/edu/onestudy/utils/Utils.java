package com.edu.onestudy.utils;

import io.micrometer.common.util.StringUtils;
import org.apache.commons.codec.digest.MurmurHash3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    private static final String REGEX_FILTER_KEY =
            "[ : ]+((?=\\[)\\[[^]]*\\]|(?=\\{)\\{[^\\}]*\\}|\\\"[^\"]*\\\"|(\\d+(\\.\\d+)?))";
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    static List<String> redactKeys = Collections.unmodifiableList(Arrays.asList(
            "api_key", "apiKey", "api_secret", "apiSecret", "otp", "pin", "access_token", "accessToken", "full_name",
            "fullName", "phone_number", "phoneNumber", "email", "mobile_number", "mobileNumber", "email_address", "emailAddress",
            "email_preference", "emailReference", "authorization", "verified_token", "verifiedToken", "x-api-secret", "xApiSecret",
            "x-api-key", "xApiKey", "Authorization", "partner", "client_id", "clientId", "public_key", "publicKey",
            "private_key", "privateKey", "x-public-key", "xPublicKey", "x-private-key", "xPrivateKey", "newrelic",
            "refresh_token", "refreshToken", "password", "passcode"));

    public static long genAutoId(String key) {
        if (StringUtils.isBlank(key)) {
            key = UUID.randomUUID().toString();
        }
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        long h = MurmurHash3.hash64(bytes);
        return Math.abs(h);
    }


    public static String redact(@NonNull String string) {
        try {
            for (String key : redactKeys) {
                Matcher matcher = Pattern.compile(String.format("\"%s\"%s", key, REGEX_FILTER_KEY)).matcher(string);
                if (matcher.find() && matcher.group(1) != null) {
                    String group = matcher.group(1);
                    if (!ObjectUtils.isEmpty(group.trim()) && !"\"\"".equals(group)) {
                        string = string.replace(group, "\"**********\"");
                    }
                }
            }
            return string;
        } catch (Exception e) {
            return string;
        }
    }

    public static <T> Map<String, T> convertListToMap(List<T> list, Function<T, String> keyExtractor) {
        return list.stream().collect(Collectors.toMap(keyExtractor, Function.identity(), (oldVal, newVal) -> newVal));
    }

    public static String convertDateToyyyyMMdd(Long input){
        String pattern = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(input);
        return simpleDateFormat.format(date);
    }

    public static boolean isMatchRegex(String str, String regex) {
        try {
            if (str == null || regex == null) {
                return false;
            }

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);

            return matcher.matches();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }


    public static String[] getBlankProperties(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(obj);
                        return value == null || (value instanceof String && StringUtils.isBlank((String) value));
                    } catch (IllegalAccessException e) {
                        return true;
                    }
                })
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public static String[] getExistedProperties(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(obj);
                        if (value instanceof String) {
                            return !StringUtils.isBlank((String) value);
                        }
                        return value != null;
                    } catch (IllegalAccessException e) {
                        return true;
                    }
                })
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public static <T extends Enum<T>> T enumOf(Class<T> enumType, String value) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    public static LocalDateTime stringToLocalDateTime(String dateOfBirth) {
        if (StringUtils.isBlank(dateOfBirth)) {
            return null;
        }
        return LocalDateTime.parse(dateOfBirth);
    }
}
