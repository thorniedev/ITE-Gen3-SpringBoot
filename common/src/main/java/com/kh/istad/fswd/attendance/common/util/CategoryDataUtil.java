package com.kh.istad.fswd.attendance.common.util;

import java.util.UUID;
import java.util.function.Predicate;

public class CategoryDataUtil
{
    private static final int MAX_ATTEMPTS = 50;

    private CategoryDataUtil() {
        // Prevent instantiation
    }

    public static String generateUniqueCode(Predicate<String> existsByCode) {
        String code;
        int attempts = 0;
        do {
            if (attempts >= MAX_ATTEMPTS) {
                throw new IllegalStateException("Failed to generate a unique category code. Maximum attempts reached.");
            }
            code = "ITE3-CAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            attempts++;
        } while (existsByCode.test(code));

        return code;
    }

    public static String generateUniqueSlug(String name, Predicate<String> existsBySlug) {
        // Assuming SlugUtil is your existing slugifier
        String baseSlug = SlugUtil.generateSlug(name);
        String slug = baseSlug;
        int suffix = 1;
        int attempts = 0;

        while (existsBySlug.test(slug)) {
            if (attempts >= MAX_ATTEMPTS) {
                throw new IllegalStateException("Failed to generate a unique slug for name '" + name + "'. Maximum attempts reached.");
            }
            slug = baseSlug + "-" + suffix;
            suffix++;
            attempts++;
        }

        return slug;
    }


}
