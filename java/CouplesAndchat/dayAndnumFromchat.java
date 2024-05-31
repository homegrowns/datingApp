package CouplesAndchat;

public class dayAndnumFromchat {
    private String key;
    private String Cday;
    private String msg;

    public dayAndnumFromchat(String key, String cday, String msg) {
        this.key = key;
        Cday = cday;
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCday() {
        return Cday;
    }

    public void setCday(String cday) {
        Cday = cday;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
