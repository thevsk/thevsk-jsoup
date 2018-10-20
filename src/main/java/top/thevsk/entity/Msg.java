package top.thevsk.entity;

public class Msg {
    private int code;
    private Object data;
    private String msg;

    public int getCode() {
        return code;
    }

    public Msg setCode(int code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Msg setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Msg setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
