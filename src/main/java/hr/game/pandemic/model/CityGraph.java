package hr.game.pandemic.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class CityGraph {
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
            entry("saoPaulo", Arrays.asList("bogota", "buenosAires", "lagos")),
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
