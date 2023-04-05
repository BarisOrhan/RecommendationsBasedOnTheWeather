import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    public String Location() {
        Scanner sc = new Scanner(System.in);


        System.out.println("Please Write Your City: ");
        String inputLocation = sc.nextLine();
        sc.close();
        return inputLocation;
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
        Document weather = Jsoup.connect(searchURL).userAgent("Chrome").get();

        //Temperature
        getTemperature(weather);

        //Rains
        getChanceOfRain(weather);

        //Humidity
        getPercentageOfHumidity(weather);

        //Wind
        getWindPower(weather);
    }

    public void getTemperature(Document weather){
        Element todayDetails = weather.getElementById("todayDetails");
        Elements details = todayDetails.select("[class*=TodayDetailsCard]");
        Element temp = details.select("[class*=feelsLikeTempValue]").first();
        if (temp != null){
            String tempFahrenheit = temp.text();
            System.out.println("Fahrenheit: " + tempFahrenheit);
            String degree = tempFahrenheit.substring(tempFahrenheit.length()-1);
            tempFahrenheit= tempFahrenheit.substring(0,tempFahrenheit.length()-1);
            double TempCelsius = Double.valueOf(tempFahrenheit);
            TempCelsius = (TempCelsius - 32) * 0.5556;
            System.out.println("Celsius: " + TempCelsius+ degree);
        }
    }

    public void getChanceOfRain(Document weather){
        Elements chanceOfRainHTMLs = weather.select("span:contains(Chance of Rain)");
        Element chanceOfRainHTML=chanceOfRainHTMLs.select("span:contains(%)").get(5);
        String chanceOfRain=chanceOfRainHTML.text().substring(14);
        System.out.println("Chance of Rain: "+ chanceOfRain);
    }

    public void getPercentageOfHumidity(Document weather){
        Element humidity = weather.select("[data-testid*=PercentageValue]").first();
        String humidityPercentage = humidity.text();
        System.out.println("Percentage of Humidity: "+ humidityPercentage);
    }

    public void getWindPower(Document weather){
        Element wind = weather.select("[data-testid*=Wind]").first();
        String windPowerAll= wind.text();
        String windPower=windPowerAll.replace("Wind Direction ","");
        System.out.println("Wind Power(mp/h): "+windPower);
        String windPowerMpH= windPower.substring(0,windPower.length()-3);
        double windPowerKmH= Double.valueOf(windPowerMpH);
        windPowerKmH = windPowerKmH*1.609344;
        System.out.println("Wind Power(km/h): "+windPowerKmH + " kmh");
    }
}