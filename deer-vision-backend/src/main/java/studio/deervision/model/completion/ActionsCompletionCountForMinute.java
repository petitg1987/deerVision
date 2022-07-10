package studio.deervision.model.completion;

import java.util.ArrayList;
import java.util.List;

public class ActionsCompletionCountForMinute {

    private final Integer minute;
    private final List<ActionCompletionCount> actionCompletionCounts = new ArrayList<>();

    public ActionsCompletionCountForMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getMinute() {
        return minute;
    }

    public List<ActionCompletionCount> getActionCompletionCounts() {
        return actionCompletionCounts;
    }

    public void addActionCompletionCounts(ActionCompletionCount quantity) {
        this.actionCompletionCounts.add(quantity);
    }
}
