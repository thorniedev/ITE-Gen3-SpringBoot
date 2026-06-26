package com.kh.istad.fswd.attendance.common.util;

public class SlugUtil
{

    public static String  generateSlug(String text)
    {
        return text
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
