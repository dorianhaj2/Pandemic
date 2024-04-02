package hr.game.pandemic.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class CityGraph {
//    @FXML public static Button paris;
//    @FXML public static Button chicago;
//    @FXML public static Button "madrid";
//    @FXML public static Button essen;
//    @FXML public static Button london;
//    @FXML public static Button newYork;
//    @FXML public static Button washington;
//    @FXML public static Button sanFrancisco;
//    @FXML public static Button montreal;
//    @FXML public static Button atlanta;
//    @FXML public static Button milan;
//    @FXML public static Button stPetersburg;
//    @FXML public static Button bogota;
//    @FXML public static Button "mexicoCity";
//    @FXML public static Button "khartoum";
//    @FXML public static Button "saoPaulo";
//    @FXML public static Button "miami";
//    @FXML public static Button "losAngeles";
//    @FXML public static Button "kinshasa";
//    @FXML public static Button "lagos";
//    @FXML public static Button "lima";
//    @FXML public static Button "buenosAires";
//    @FXML public static Button "johannesburg";
//    @FXML public static Button "santiago";
//    @FXML public static Button "istanbul";
//    @FXML public static Button "baghdad";
//    @FXML public static Button "delhi";
//    @FXML public static Button "karachi";
//    @FXML public static Button "cairo";
//    @FXML public static Button "chennai";
//    @FXML public static Button "tehran";
//    @FXML public static Button "algiers";
//    @FXML public static Button "kolkata";
//    @FXML public static Button "mumbai";
//    @FXML public static Button "riyadh";
//    @FXML public static Button "moscow";
//    @FXML public static Button "hongKong";
//    @FXML public static Button "shanghai";
//    @FXML public static Button "manila";
//    @FXML public static Button "bangkok";
//    @FXML public static Button "hoChiMinhCity";
//    @FXML public static Button "taipei";
//    @FXML public static Button "jakarta";
//    @FXML public static Button "tokyo";
//    @FXML public static Button "seoul";
//    @FXML public static Button "sydney";
//    @FXML public static Button "beijing";
//    @FXML public static Button "osaka";

    public static Map<String, List<String>> cityGraph = Map.ofEntries(
            entry("paris", Arrays.asList("essen", "london", "madrid", "milan", "algiers")),
            entry("chicago", Arrays.asList("atlanta", "sanFrancisco", "montreal", "losAngeles", "mexicoCity")),
            entry("madrid", Arrays.asList("london", "newYork", "paris", "saoPaulo", "algiers")),
            entry("essen", Arrays.asList("london", "milan", "paris", "stPetersburg")),
            entry("london", Arrays.asList("essen", "madrid", "newYork", "paris")),
            entry("newYork", Arrays.asList("london", "madrid", "montreal", "washington")),
            entry("washington", Arrays.asList("atlanta", "newYork", "montreal", "miami")),
            entry("sanFrancisco", Arrays.asList("chicago", "manila", "tokyo", "losAngeles")),
            entry("montreal", Arrays.asList("chicago", "newYork", "washington")),
            entry("atlanta", Arrays.asList("chicago", "washington", "miami")),
            entry("milan", Arrays.asList("essen", "paris", "istanbul")),
            entry("stPetersburg", Arrays.asList("essen", "istanbul", "moscow")),
            entry("bogota", Arrays.asList("buenosAires", "lima", "mexicoCity", "miami", "saoPaulo")),
            entry("mexicoCity", Arrays.asList("bogota", "lima", "losAngeles", "miami", "chicago")),
            entry("khartoum", Arrays.asList("johannesburg", "kinshasa", "lagos", "cairo")),
            entry("saoPaulo", Arrays.asList("bogota", "buenosAires", "lagos", "cairo")),
            entry("miami", Arrays.asList("bogota", "mexicoCity", "atlanta", "washington")),
            entry("losAngeles", Arrays.asList("mexicoCity", "chicago", "sanFrancisco", "sydney")),
            entry("kinshasa", Arrays.asList("johannesburg", "khartoum", "lagos")),
            entry("lagos", Arrays.asList("khartoum", "kinshasa", "saoPaulo")),
            entry("lima", Arrays.asList("bogota", "mexicoCity", "santiago")),
            entry("buenosAires", Arrays.asList("bogota", "saoPaulo")),
            entry("johannesburg", Arrays.asList("khartoum", "kinshasa")),
            entry("santiago", Arrays.asList("lima")),
            entry("istanbul", Arrays.asList("algiers", "baghdad", "cairo", "moscow", "milan", "stPetersburg")),
            entry("baghdad", Arrays.asList("cairo", "istanbul", "karachi", "riyadh", "tehran")),
            entry("delhi", Arrays.asList("chennai", "karachi", "kolkata", "mumbai", "tehran")),
            entry("karachi", Arrays.asList("baghdad", "delhi", "mumbai", "riyadh", "tehran")),
            entry("cairo", Arrays.asList("algiers", "baghdad", "istanbul", "riyadh", "khartoum")),
            entry("chennai", Arrays.asList("delhi", "kolkata", "mumbai", "bangkok", "jakarta")),
            entry("tehran", Arrays.asList("baghdad", "delhi", "karachi", "moscow")),
            entry("algiers", Arrays.asList("cairo", "istanbul", "madrid", "paris")),
            entry("kolkata", Arrays.asList("chennai", "delhi", "bangkok", "hongKong")),
            entry("mumbai", Arrays.asList("chennai", "delhi", "karachi")),
            entry("riyadh", Arrays.asList("baghdad", "cairo", "karachi")),
            entry("moscow", Arrays.asList("istanbul", "tehran", "stPetersburg")),
            entry("hongKong", Arrays.asList("bangkok", "hoChiMinhCity", "manila", "shanghai", "taipei", "kolkata")),
            entry("shanghai", Arrays.asList("beijing", "hongKong", "seoul", "taipei", "tokyo")),
            entry("manila", Arrays.asList("hoChiMinhCity", "hongKong", "sydney", "taipei", "sanFrancisco")),
            entry("bangkok", Arrays.asList("hoChiMinhCity","hongKong", "jakarta", "chennai", "kolkata")),
            entry("hoChiMinhCity", Arrays.asList("bangkok", "hongKong", "jakarta", "manila")),
            entry("taipei", Arrays.asList("hongKong", "manila", "osaka", "shanghai")),
            entry("jakarta", Arrays.asList("bangkok", "hoChiMinhCity", "sydney", "chennai")),
            entry("tokyo", Arrays.asList("seoul", "shanghai", "sanFrancisco")),
            entry("seoul", Arrays.asList("beijing", "shanghai", "tokyo")),
            entry("sydney", Arrays.asList("jakarta", "manila", "losAngeles")),
            entry("beijing", Arrays.asList("seoul", "shanghai")),
            entry("osaka", Arrays.asList("taipei", "tokyo"))
    );


}
