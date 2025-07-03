package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * کلاس برای هش کردن رمزهای عبور با استفاده از الگوریتم SHA-256 و نمک (Salt)
 */
public class Hasher {
    // طول نمک (Salt) برای هش کردن
    private static final int SALT_LENGTH = 16;
    // الگوریتم هش کردن
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * تولید نمک تصادفی برای افزایش امنیت هش
     * @return نمک تولید شده به صورت بایت آرایه
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * هش کردن رمز عبور با نمک تصادفی
     * @param password رمز عبور ورودی
     * @return آرایه بایتی شامل نمک + هش رمز عبور
     */
    public static byte[] hashPassword(String password) {
        // تولید نمک تصادفی
        byte[] salt = generateSalt();
        return hashPasswordWithSalt(password, salt);
    }

    /**
     * هش کردن رمز عبور با نمک مشخص
     * @param password رمز عبور ورودی
     * @param salt نمک برای هش کردن
     * @return آرایه بایتی شامل نمک + هش رمز عبور
     */
    public static byte[] hashPasswordWithSalt(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            // اضافه کردن نمک به رمز عبور
            digest.update(salt);

            // هش کردن رمز عبور
            byte[] hashedPassword = digest.digest(password.getBytes());

            // ترکیب نمک و هش برای ذخیره سازی
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return combined;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("الگوریتم هش کردن پشتیبانی نمی‌شود", e);
        }
    }

    /**
     * بررسی تطابق رمز عبور با هش ذخیره شده
     * @param password رمز عبور ورودی برای بررسی
     * @param storedHash هش ذخیره شده در دیتابیس (شامل نمک + هش)
     * @return true اگر رمز عبور صحیح باشد، false در غیر این صورت
     */
    public static boolean verifyPassword(String password, byte[] storedHash) {
        try {
            // استخراج نمک از هش ذخیره شده
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(storedHash, 0, salt, 0, salt.length);

            // محاسبه هش رمز ورودی با نمک استخراج شده
            byte[] hashedPassword = hashPasswordWithSalt(password, salt);

            // مقایسه هش‌ها
            return MessageDigest.isEqual(storedHash, hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("خطا در بررسی رمز عبور", e);
        }
    }

    /**
     * تبدیل هش به رشته قابل نمایش (برای ذخیره سازی یا نمایش)
     * @param hash هش برای تبدیل
     * @return رشته Base64
     */
    public static String hashToString(byte[] hash) {
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * تبدیل رشته به هش (برای بازیابی از ذخیره سازی)
     * @param hashString رشته Base64
     * @return آرایه بایتی هش
     */
    public static byte[] stringToHash(String hashString) {
        return Base64.getDecoder().decode(hashString);
    }
}