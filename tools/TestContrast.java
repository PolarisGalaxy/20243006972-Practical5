import java.awt.Color;

public class TestContrast {
    static double lum(int rgb) {
        int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        return 0.2126 * lin(r) + 0.7152 * lin(g) + 0.0722 * lin(b);
    }

    static double lin(int c) {
        double v = c / 255.0;
        return v <= 0.03928 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
    }

    static double ratio(int a, int b) {
        double la = lum(a), lb = lum(b);
        if (la < lb) { double t = la; la = lb; lb = t; }
        return (la + 0.05) / (lb + 0.05);
    }

    public static void main(String[] args) {
        int white = 0xFFFFFF;
        int bg = 0xF4F4F6;
        String[] names = {"Beijing", "Shanghai", "Tianjin", "Chongqing"};
        int[] colours = {0xD32F2F, 0xD81B60, 0x37474F, 0xC2410C};

        System.out.println("== white text on the colour (name bar + button) ==");
        for (int i = 0; i < colours.length; i++) {
            double r = ratio(colours[i], white);
            System.out.printf("%-10s #%06X  vs white = %.2f:1  %s%n",
                names[i], colours[i], r, r >= 4.5 ? "PASS AA" : (r >= 3.0 ? "PASS AA-large" : "FAIL"));
        }

        System.out.println("\n== colour as body text on the light app background ==");
        for (int i = 0; i < colours.length; i++) {
            double r = ratio(colours[i], bg);
            System.out.printf("%-10s #%06X  vs #F4F4F6 = %.2f:1  %s%n",
                names[i], colours[i], r, r >= 4.5 ? "PASS AA" : (r >= 3.0 ? "PASS AA-large" : "FAIL"));
        }
    }
}
