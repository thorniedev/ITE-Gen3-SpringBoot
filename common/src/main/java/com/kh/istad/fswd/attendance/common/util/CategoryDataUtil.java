package com.kh.istad.fswd.attendance.common.util;

import java.util.UUID;
import java.util.function.Predicate;

public class ProductDataUtil
{
    private static final int MAX_ATTEMPTS = 50;

    private ProductDataUtil() {
        // Prevent instantiation
    }


    /**
     * Generates a unique product code by checking uniqueness against the provided condition.
     * * @param existsByCode A functional check (e.g., productRepository::existsByCode)
     * @return A guaranteed unique product code within attempt thresholds
     */
    public static String generateUniqueCode(Predicate<String> existsByCode) {
        String code;
        int attempts = 0;
        do {
            if (attempts >= MAX_ATTEMPTS) {
                throw new IllegalStateException("Failed to generate a unique product code. Maximum attempts reached.");
            }
            code = "ITE-3RD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            attempts++;
        } while (existsByCode.test(code));

        return code;
    }


    /**
     * Generates a unique slug based on a product name and checks uniqueness.
     * * @param name The raw name of the product
     * @param existsBySlug A functional check (e.g., productRepository::existsBySlug)
     * @return A unique slug string
     */
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
