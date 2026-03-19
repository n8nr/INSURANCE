import java.util.*;

public class CarData {

    private static final Map<String, List<String>> MAKE_TO_MODELS = new LinkedHashMap<>();

    static {
        MAKE_TO_MODELS.put("Ford", List.of("Fiesta", "Focus", "Ka"));
        MAKE_TO_MODELS.put("Vauxhall", List.of("Corsa", "Astra", "Adam"));
        MAKE_TO_MODELS.put("Volkswagen", List.of("Polo", "Golf", "Up"));
        MAKE_TO_MODELS.put("Toyota", List.of("Yaris", "Aygo", "Corolla"));
    }

    public static List<String> getMakes() {
        return new ArrayList<>(MAKE_TO_MODELS.keySet());
    }

    public static List<String> getModelsForMake(String make) {
        if (make == null) return List.of();
        return MAKE_TO_MODELS.getOrDefault(make, List.of());
    }
}