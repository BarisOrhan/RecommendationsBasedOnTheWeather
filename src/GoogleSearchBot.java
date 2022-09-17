import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author borhan
 */
public class GoogleSearchBot {
    /* Googledan kullanıcının girmiş olduğu Location ile hava durumu sorgusu aratılacak.X
     * StatusWheather çıktısı olarak kaydedilecek çıktı.
     * GoogleSearchBot sınıfının içerisine SearchBot Methodu olarak yazılacak.X
     * Kullanıcıdan String olarak Location inputu alınacak.X
     * GoogleSearchBot classının içerisine method olarak yazılacak.X
     * */
    ArrayList<String> RainList = new ArrayList<String>();
    ArrayList<String> HumidityList = new ArrayList<String>();

    public String Location() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Write Your City: ");
        String InputLocation = sc.nextLine();
        sc.close();
        return InputLocation;
    }

    public void SearchBot(String Location) throws IOException { //Must be added Jsoup jar file

        final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
        String searchURL = GOOGLE_SEARCH_URL + "?q=" + Location + "+hava&num=" + 1;


        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        Elements elements = doc.select("a");
        for (Element e : elements) {
            String WeatherUrl = e.attr("abs:href");
            if (WeatherUrl.contains("www.weather")) {
                System.out.println("Weather URL:" + WeatherUrl);
                searchURL = WeatherUrl;
            }

        }
        /*
         * weather.com... new searchurl
         */
        Document weather = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        //Temperature
        Elements temps = weather.select("div.TodayDetailsCard--feelsLikeTemp--3fwAJ > span.TodayDetailsCard--feelsLikeTempValue--Cf9Sl");
        for (Element e : temps) {
            String TempFahrenheit = e.text();
            System.out.println("Fahrenheit: " + TempFahrenheit);
            TempFahrenheit = TempFahrenheit.replace("°", "");
            double TempCelsius = Double.valueOf(TempFahrenheit);
            TempCelsius = (TempCelsius - 32) * 0.5556;
            System.out.println("Celsius: " + TempCelsius);
        }

        //Rains
        Elements Rains = weather.select("div.Column--precip--2ck8J > span.Column--precip--2ck8J");
        for (Element e : Rains) {
            String RainRate = e.text();
            RainRate=RainRate.replace("Chance of Rain","");
            RainList.add(RainRate);// Fifth rain rate is today's rate
        }
        System.out.println("Chance of Rain: "+ RainList.get(4));

        //Humidity
        Elements Humidity = weather.select("div.WeatherDetailsListItem--wxData--2s6HT");
        for (Element e : Humidity) {
            String HumidityPercentage = e.text();
            HumidityList.add(HumidityPercentage);// 3th rate is today's rate
        }
        System.out.println("Percentage of Humidity: "+ HumidityList.get(2));

        //Wind
        String WindPower= HumidityList.get(1);
        WindPower=WindPower.replace("Wind Direction ","");
        System.out.println("Wind Power: "+WindPower);

    }

}