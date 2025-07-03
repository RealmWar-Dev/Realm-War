package view.screens;

import view.components.BaseBackgroundPanel;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class HelpScreen extends BaseBackgroundPanel {
    public static final String name = "HELP";

    public HelpScreen() {
        super(false, false);
        setName(name);
        initializeComponents();
    }

    /**
     * ایجاد یک بخش با متن فارسی و ایموجی
     */
    private JPanel createSection(String titleText, String bodyText) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setOpaque(false);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // عنوان بخش
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(getPersianFont(22, Font.BOLD));
        titleLabel.setForeground(new Color(255, 215, 0)); // رنگ طلایی
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        titleLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        titleLabel.setOpaque(false);

        // متن بدنه
        JTextPane bodyPane = createPersianTextPane(bodyText);
        bodyPane.setForeground(new Color(230, 230, 230)); // رنگ سفید روشن

        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        sectionPanel.add(bodyPane, BorderLayout.CENTER);

        return sectionPanel;
    }

    /**
     * ایجاد JTextPane با تنظیمات فارسی
     */
    private JTextPane createPersianTextPane(String text) {
        JTextPane textPane = new JTextPane();
        textPane.setEditorKit(new RightToLeftEditorKit());
        textPane.setText(text);
        textPane.setFont(getPersianFont(16, Font.PLAIN));
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setBackground(new Color(0, 0, 0, 0));
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        textPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return textPane;
    }

    /**
     * دریافت فونت مناسب برای نمایش فارسی و ایموجی
     */
    private Font getPersianFont(int size, int style) {
        String[] fontNames = {
                "Segoe UI Emoji",
                "B Nazanin",
                "Tahoma",
                "Arial Unicode MS"
        };

        String emoji = "📜";

        for (String fontName : fontNames) {
            Font font = new Font(fontName, style, size);
            if (font.canDisplay('آ') && font.canDisplayUpTo(emoji) == -1) {
                return font;
            }
        }

        return new Font(Font.SANS_SERIF, style, size);
    }

    @Override
    public void initializeComponents() {
        // پنل محتوای اصلی
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // افزودن بخش‌های مختلف
        contentPanel.add(createSection("📜 معرفی بازی",
                """
                        Realm War یک بازی استراتژیک جذاب و نوبتی است که در آن:
                        
                        • پادشاهی خود را می‌سازید
                        • ارتش قدرتمند تشکیل می‌دهید
                        • با دشمنان مبارزه می‌کنید
                        • قلمرو خود را گسترش می‌دهید"""));

        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createSection("⚔️ واحدهای نظامی",
                """
                        👑 شوالیه: واحد نزدیک‌زن قوی با زره بالا
                        🏹 کماندار: حمله از راه دور با دقت بالا
                        🧙‍♂️ جادوگر: حمله ناحیه‌ای و آسیب زیاد
                        ⚔️ سرباز: واحد پایه با هزینه کم
                        🛡 محافظ: دفاع عالی اما حرکت کند"""));

        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createSection("🏗 سازه‌های مهم",
                """
                        🏰 سربازخانه: آموزش نیروهای جدید
                        🧱 دیوار دفاعی: محافظت از قلمرو
                        ⛏ معدن طلا: تولید منابع مالی
                        🌾 مزرعه: تولید غذا برای نیروها
                        🔬 آزمایشگاه: تحقیقات و ارتقاء"""));

        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createSection("🎯 سیستم امتیازدهی",
                """
                        امتیاز شما بر اساس:
                        
                        ✅ پیروزی در نبردها
                        ✅ جمع‌آوری منابع
                        ✅ گسترش قلمرو
                        ❌ از دست دادن نیروها امتیاز کم می‌کند
                        
                        هدف نهایی: فتح تمام نقشه!"""));

        // اسکرول پنل
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // تنظیم اسکرول بار
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBlockIncrement(50);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * کلاس کمکی برای راست‌چین کردن کامل متن
     */
    private static class RightToLeftEditorKit extends StyledEditorKit {
        public ViewFactory getViewFactory() {
            return new RightToLeftViewFactory();
        }
    }

    private static class RightToLeftViewFactory implements ViewFactory {
        public View create(Element elem) {
            View view = null;
            String kind = elem.getName();

            if (kind != null) {
                view = switch (kind) {
                    case AbstractDocument.ParagraphElementName -> new ParagraphView(elem) {
                        {
                            setJustification(StyleConstants.ALIGN_RIGHT);
                        }
                    };
                    case AbstractDocument.SectionElementName -> new BoxView(elem, View.Y_AXIS);
                    default -> new LabelView(elem);
                };
            }
            return view;
        }
    }
}