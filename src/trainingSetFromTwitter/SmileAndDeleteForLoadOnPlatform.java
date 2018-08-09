package trainingSetFromTwitter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SmileAndDeleteForLoadOnPlatform {




   public static String prepareText(String text) throws Exception {
        String preparedText = text;
        preparedText.replaceAll("@[\\w:_-]*","").replaceAll("#[а-яА-Яa-zA-Z0-9:_-]*","").replaceAll("http[./а-яА-Яa-zA-Z0-9/\\:_-]*","").replaceAll(" amp;*"," ").replaceAll("amp;|amp;;|htt…"," ");
        String[] numbers = {"Ноль", "Один", "Два", "Три", "Четыре", "Пять", "Шесть", "Семь", "Восемь", "Девять"};
        String[] smilePositiveArray = {"\uD83D\uDE42", "\uD83D\uDC4D", "\uD83D\uDC93", "\uD83D\uDD90", "\uD83D\uDE0A", "\uD83D\uDE09", "\uD83D\uDE0D",
                "^_^", "D", "DD", "DDD", "DDDD", "DDDDD", "))", ")))", "))))", ")))))", "))))))", "^__^", "^___^", "n_n", "=:)", ":-)", ":-D", ";)",
                ";-)", "xD", "xDD", ":-*", "<3", ";-]", ":-]", ";]", ":]", "8-)", "8-]",
                ":D", ":^D", "=^*", "o_o", "0_0", "o_O", "*_*", "=)", ":,-)", ":)", ":-x", ":3"};
        String[] smileNegativeArray = {"\uD83D\uDC4E", "\uD83D\uDD96", "<_>", "((", "(((", "((((", "((((((", ":(", ":-(", ":[", ":-[", "=(", "v_v", "-_-", ">_<",
                ">__<", "-_-+", "(>>)", "(>_>)", "(<_<)", "(;_;)", "(T_T)", "($_$)", ":((", ":_(", "D;",
                ":-||", ":<", ":/"};
        String[] smileNeutralArray = {"...", "=X=", "(?_?)", "=__=", "*^_^*", "*:|", "..."};
        Map<String, String> hashMap = new HashMap<String, String>();
        Map<Integer, String> hashMapNumbers = new HashMap<Integer, String>();

        for (int i=0; i<numbers.length;i++) {
            hashMapNumbers.put(i, numbers[i]);
        }

        String s;
        for (int i=0; i<smilePositiveArray.length;i++) {
            hashMap.put(" ПозитивныйСмайлНомер" + theWordsOfNumbers(String.valueOf(i),hashMapNumbers)+" ", smilePositiveArray[i]);
        }

        for (int i=0; i<smileNegativeArray.length;i++) {
            hashMap.put(" НегативныйСмайлНомер" + theWordsOfNumbers(String.valueOf(i),hashMapNumbers)+" ", smileNegativeArray[i]);
        }
        for (int i=0; i<smileNeutralArray.length;i++) {
            hashMap.put(" НейтральныйСмайлНомер" + theWordsOfNumbers(String.valueOf(i),hashMapNumbers)+" ", smileNeutralArray[i]);
        }

        for (Map.Entry n : hashMap.entrySet()) {
            if (text.contains(n.getValue().toString()))
                text=text.replaceAll(Pattern.quote(n.getValue().toString()),n.getKey().toString());
        }
       preparedText = text.replaceAll("[^Ёёa-zA-Zа-яА-Я]"," ");
        return preparedText;
    }


    public static String theWordsOfNumbers(String s, Map<Integer, String> hashMapNumbers) {
        String nameOfNum = "";
        if (s.length() == 1) {
            for (Map.Entry n : hashMapNumbers.entrySet()) {
                if (s.contains(n.getKey().toString()))
                    nameOfNum = n.getValue().toString();
            }
        } else if (s.length() == 2) {
            for (Map.Entry n : hashMapNumbers.entrySet()) {
                if (String.valueOf(s.charAt(s.length() - 2)).contains(n.getKey().toString()))
                    nameOfNum = n.getValue().toString();
            }
            for (Map.Entry n : hashMapNumbers.entrySet()) {
                if (String.valueOf(s.charAt(s.length() - 1)).contains(n.getKey().toString()))
                    nameOfNum = nameOfNum + n.getValue().toString();
            }
        }
        return nameOfNum;
    }
}
