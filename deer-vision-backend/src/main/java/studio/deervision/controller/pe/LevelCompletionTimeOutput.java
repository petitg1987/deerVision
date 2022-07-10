package studio.deervision.controller.pe;

import java.util.ArrayList;
import java.util.List;

public class LevelCompletionTimeOutput {

    private final Integer minute;
    private final List<LevelCompletionTimeQuantity> quantities = new ArrayList<>();

    public LevelCompletionTimeOutput(Integer minute) {
        this.minute = minute;
    }

    public Integer getMinute() {
        return minute;
    }

    public List<LevelCompletionTimeQuantity> getQuantities() {
        return quantities;
    }

    public void addQuantities(LevelCompletionTimeQuantity quantity) {
        this.quantities.add(quantity);
    }
}
