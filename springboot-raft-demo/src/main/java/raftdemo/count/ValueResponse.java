package raftdemo.count;

import java.io.Serializable;

public class ValueResponse implements Serializable {

    private static final long serialVersionUID = -4220017686727146773L;

    private long value;
    private boolean success;
    /**
     * redirect peer id
     */
    private String redirect;

    private String errorMsg;

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
