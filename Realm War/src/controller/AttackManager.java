package controller;

import model.unit.Unit;
import model.structure.Structure;
import model.block.Block;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * مسئول مدیریت منطق مربوط به حملات بین واحدها و سازه‌ها در بازی.
 * این کلاس شامل متدهای استاتیک برای اجرای عملیات حمله است.
 */
public class AttackManager {

    private static final Logger LOGGER = Logger.getLogger(AttackManager.class.getName());

    /**
     * حمله‌ای را از یک واحد حمله‌کننده بر روی یک بلوک هدف انجام می‌دهد.
     * این متد بررسی‌های مختلف، اعمال آسیب، و مدیریت نابودی واحدها یا سازه‌ها را در بلوک هدف انجام می‌دهد.
     *
     * @param attacker واحد حمله‌کننده.
     * @param targetBlock بلوکی که هدف (واحد یا سازه) در آن قرار دارد.
     * @throws IllegalArgumentException اگر بلوک هدف خارج از برد حمله باشد،
     * یا اگر هیچ چیز معتبری برای حمله در بلوک وجود نداشته باشد.
     * @throws IllegalStateException    اگر حمله‌کننده قبلاً در این نوبت حمله کرده باشد،
     * یا اگر حمله‌کننده سعی کند به واحد/سازه‌ی خودی حمله کند.
     */
    public static void performAttack(Unit attacker, Block targetBlock)
            throws IllegalArgumentException, IllegalStateException {

        // 1. بررسی کند که attacker قبلاً حمله نکرده
        if (attacker.hasAttacked()) {
            LOGGER.log(Level.WARNING, "{0} {1} در ({2},{3}) تلاش به حمله کرد اما قبلاً در این نوبت حمله کرده است.",
                    new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                            attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY()});
            throw new IllegalStateException("حمله‌کننده قبلاً در این نوبت حمله کرده است.");
        }

        // اعتبارسنجی حمله‌کننده و بلوک هدف
        if (attacker == null) {
            throw new IllegalArgumentException("حمله‌کننده نمی‌تواند null باشد.");
        }
        if (targetBlock == null) {
            throw new IllegalArgumentException("بلوک هدف نمی‌تواند null باشد.");
        }

        // 2. بررسی کند targetBlock در برد حمله‌ی attacker هست (با استفاده از متد calculateDistance).
        int distance = calculateDistance(attacker.getCurrentBlockLocation(), targetBlock);
        if (distance > attacker.getAttackRange()) {
            LOGGER.log(Level.WARNING, "{0} {1} در ({2},{3}) نمی‌تواند به هدف در ({4},{5}) حمله کند: خارج از برد. فاصله {6}، برد {7}.",
                    new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                            attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                            targetBlock.getPositionX(), targetBlock.getPositionY(), distance, attacker.getAttackRange()});
            throw new IllegalArgumentException("بلوک هدف خارج از برد حمله‌ی حمله‌کننده است.");
        }

        Unit targetUnit = targetBlock.getUnit();
        Structure targetStructure = targetBlock.getStructure();

        // 3. بررسی کند روی targetBlock چی هست:
        if (targetUnit != null) {
            // هدف یک واحد است (Unit)
            // الف. اگر مالک اون با attacker یکیه، استثنا بده (نمی‌تونه به خودی حمله کنه).
            if (targetUnit.getOwnerKingdom() == attacker.getOwnerKingdom()) {
                LOGGER.log(Level.WARNING, "{0} {1} در ({2},{3}) تلاش به حمله به واحد خودی {4} در ({5},{6}) کرد.",
                        new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                                attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                                targetUnit.getUnitRank(), targetBlock.getPositionX(), targetBlock.getPositionY()});
                throw new IllegalStateException("نمی‌توان به یک واحد خودی حمله کرد.");
            }

            // ب. در غیر این صورت، متد takeDamage رو روی اون اجرا کن.
            int damageDealt = attacker.attack(targetUnit); // متد حمله واحد، آسیب را اعمال کرده و hasAttacked را علامت‌گذاری می‌کند.
            LOGGER.log(Level.INFO, "{0} {1} در ({2},{3}) به {4} {5} در ({6},{7}) به میزان {8} آسیب حمله کرد. سلامتی هدف: {9}/{10}.",
                    new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                            attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                            targetUnit.getOwnerKingdom().getName(), targetUnit.getUnitRank(),
                            targetBlock.getPositionX(), targetBlock.getPositionY(),
                            damageDealt, targetUnit.getCurrentHealth(), targetUnit.getMaxHealth()});

            // اگر یونیت کشته شد (isDestroyed())، آن را از map و kingdom حذف کن.
            // (متد takeDamage خود Unit قبلاً این کار را انجام می‌دهد، بنابراین این خط اضافی نیست و نیازی به فراخوانی دستی removeUnit از AttackManager نیست.)

        } else if (targetStructure != null) {
            // هدف یک سازه است (Structure)
            // الف. باز هم اگر مالک هم‌تیمیه، استثنا پرتاب کنه.
            if (targetStructure.getOwnerKingdom() == attacker.getOwnerKingdom()) {
                LOGGER.log(Level.WARNING, "{0} {1} در ({2},{3}) تلاش به حمله به سازه خودی {4} در ({5},{6}) کرد.",
                        new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                                attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                                targetStructure.getStructureType(), targetBlock.getPositionX(), targetBlock.getPositionY()});
                throw new IllegalStateException("نمی‌توان به یک سازه خودی حمله کرد.");
            }

            // ب. در غیر این صورت، takeDamage اجرا کن.
            targetStructure.takeDamage(attacker.getAttackPower()); // سازه‌ها آسیب خام قدرت حمله را دریافت می‌کنند.
            LOGGER.log(Level.INFO, "{0} {1} در ({2},{3}) به {4} {5} در ({6},{7}) به میزان {8} آسیب حمله کرد. سلامتی هدف: {9}/{10}.",
                    new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                            attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                            targetStructure.getOwnerKingdom().getName(), targetStructure.getStructureType(),
                            targetBlock.getPositionX(), targetBlock.getPositionY(),
                            attacker.getAttackPower(), targetStructure.getCurrentHealth(), targetStructure.getMaxHealth()});

            // اگر ساختمون نابود شد، از map و kingdom حذفش کن.
            // (متد takeDamage خود Structure قبلاً این کار را انجام می‌دهد، بنابراین این خط اضافی نیست و نیازی به فراخوانی دستی removeStructure از AttackManager نیست.)

        } else {
            // نه یونیتی هست نه ساختمونی، استثنای IllegalArgumentException پرتاب کن.
            LOGGER.log(Level.WARNING, "{0} {1} در ({2},{3}) تلاش به حمله به بلوک خالی در ({4},{5}) کرد: چیزی برای حمله وجود ندارد.",
                    new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank(),
                            attacker.getCurrentBlockLocation().getPositionX(), attacker.getCurrentBlockLocation().getPositionY(),
                            targetBlock.getPositionX(), targetBlock.getPositionY()});
            throw new IllegalArgumentException("چیزی برای حمله وجود ندارد.");
        }

        // در پایان، attacker.markAttacked() رو صدا بزن.
        attacker.markAttacked();
        LOGGER.log(Level.INFO, "حمله توسط {0} {1} با موفقیت انجام شد.",
                new Object[]{attacker.getOwnerKingdom().getName(), attacker.getUnitRank()});
    }

    /**
     * متد کمکی برای محاسبه فاصله منهتن بین دو بلوک.
     * این متد برای حفظ خودبسندگی منطق AttackManager از Unit.java کپی شده است،
     * اما در صورت تمایل می‌توان آن را به عنوان یک ابزار مشترک نیز استفاده کرد.
     * این روش فاصله اقلیدسی را برای حرکت افقی و عمودی در یک شبکه محاسبه می کند.
     */
    private static int calculateDistance(Block block1, Block block2) {
        return Math.abs(block1.getPositionX() - block2.getPositionX()) +
                Math.abs(block1.getPositionY() - block2.getPositionY());
    }
}