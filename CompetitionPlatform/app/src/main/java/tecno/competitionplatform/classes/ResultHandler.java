package tecno.competitionplatform.classes;

/**
 * Created by Andres on 21/10/2015.
 */
public class ResultHandler<Object> {

    private Exception exception;
    private Object data;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean isResultValid() {
        return (this.exception == null);
    }


}
