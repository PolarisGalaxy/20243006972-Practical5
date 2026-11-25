import java.lang.reflect.*;

public class TestTextProcessor {
    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("com.example.practicalfive.TextProcessor");
        Object tp = c.getDeclaredConstructor().newInstance();
        Method m = c.getMethod("processMunicipality", String.class);
        Method gi = c.getMethod("getMunicipalityIndex", String.class);

        String[][] cases = {
            {"beijing", "Beijing"}, {"BEIJING", "Beijing"}, {"Beijing", "Beijing"},
            {"SHANGHAI", "Shanghai"}, {"shanghai", "Shanghai"},
            {"tianjin", "Tianjin"}, {"Tianjin", "Tianjin"},
            {"ChOnGqInG", "Chongqing"}, {"chongqing", "Chongqing"},
            {"Guangzhou", null}, {"", null}, {"   ", null}, {null, null},
            {"  Beijing  ", "Beijing"}, {"Tianjin ", "Tianjin"}
        };

        int pass = 0, fail = 0;
        for (String[] tc : cases) {
            Object actual = m.invoke(tp, tc[0]);
            String exp = tc[1];
            boolean ok = (exp == null) ? actual == null : exp.equals(actual);
            if (ok) pass++; else fail++;
            System.out.printf("%-6s input=%-14s expected=%-10s actual=%-10s%n",
                ok ? "PASS" : "FAIL", "\"" + tc[0] + "\"", exp, actual);
        }

        // Index -> array order must be Beijing(0) Shanghai(1) Chongqing(2) Tianjin(3)
        System.out.println("--- indices ---");
        for (String name : new String[]{"Beijing","Shanghai","Chongqing","Tianjin"}) {
            System.out.println(name + " -> " + gi.invoke(tp, name));
        }

        System.out.println("--- summary word counts ---");
        String[] summaries = {
            "Beijing is the capital of the People's Republic of China and one of the largest cities in the world, home to more than twenty-one million residents. Situated in the north of the country, it has been the seat of Chinese power for centuries, first as the capital of the Ming dynasty and later of the People's Republic. The city is a vast living museum: the Forbidden City, Tiananmen Square, the Temple of Heaven and the Badaling section of the Great Wall all draw millions of visitors. At the same time Beijing has grown into a major cultural and educational centre, with around nine hundred universities, thriving technology districts and a skyline of striking modern towers set beside broad, tree-lined avenues.",
            "Shanghai is the most commercially important city in mainland China and one of the largest on Earth, standing on the eastern coast where the Yangtze River meets the East China Sea. It began as a handful of quiet fishing villages and was transformed during the nineteenth century by the foreign concessions, a past still visible in the art deco and colonial architecture along the Bund and the old French Concession. Today it is home to the Shanghai Stock Exchange and the world's busiest container port, and it remains one of the country's most international cities, famous for its distinctive skyline, its long-established shikumen lane houses, and a lively cultural calendar that includes the Shanghai International Film Festival.",
            "Chongqing, formerly romanised as Chungking, is the largest municipality in China by area and one of the largest cities on Earth, with roughly thirty million residents. It lies in the southwest at the meeting point of the Yangtze and Jialing rivers, and its name means double celebration. The city was elevated to direct-administered municipality status in 1997 as a deliberate move to accelerate development in the vast western region of the country. Historically it was the wartime capital of the Republic of China and is still famous for the teahouses of the Hongyadong district. Today Chongqing is a fast-growing river port and manufacturing hub, its dramatic hillsides crowned with towers and its harbour crossed by cable cars and bridges.",
            "Tianjin is one of the four direct-administered municipalities of China, a coastal city of about thirteen million people on the Bohai Sea roughly one hundred and fifty kilometres south-east of Beijing. Its name means heavenly ford and refers to an ancient crossing point over the Yong River, where a trading settlement grew into an important river port on the historic route from northern China towards the sea. Foreign powers established concessions along the Hai River in the nineteenth century, and the grand European architecture of that period survives today in districts such as the Five Great Avenues. Modern Tianjin combines heavy industry, one of the world's busiest container ports and a rapidly expanding skyline of glass towers."
        };
        String[] names = {"Beijing","Shanghai","Chongqing","Tianjin"};
        for (int i = 0; i < summaries.length; i++) {
            int words = summaries[i].trim().split("\\s+").length;
            boolean ok = words >= 80 && words <= 120;
            if (!ok) fail++; else pass++;
            System.out.printf("%-6s %-10s words=%d%n", ok ? "PASS" : "FAIL", names[i], words);
        }

        System.out.println("\nTOTAL pass=" + pass + " fail=" + fail);
    }
}
