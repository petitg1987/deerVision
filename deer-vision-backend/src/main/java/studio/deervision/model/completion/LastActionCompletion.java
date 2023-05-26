package studio.deervision.model.completion;

public interface LastActionCompletion {
    String getRequestKey();
    Integer getLevelId();
    String getActionName();
    String getCreationDateTime();
}
