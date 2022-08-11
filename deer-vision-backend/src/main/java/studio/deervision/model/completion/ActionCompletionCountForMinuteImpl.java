package studio.deervision.model.completion;

public class ActionCompletionCountForMinuteImpl implements ActionCompletionCountForMinute {

    private Integer minute;
    private Integer playerCount;

    public ActionCompletionCountForMinuteImpl(Integer minute, Integer playerCount) {
        this.minute = minute;
        this.playerCount = playerCount;
    }

    @Override
    public Integer getMinute() {
        return minute;
    }

    @Override
    public Integer getPlayerCount() {
        return playerCount;
    }
}
