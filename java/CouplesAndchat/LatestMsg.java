package CouplesAndchat;

public class LatestMsg {
    private String msg;
    private String MyNum;
    //값을 추가할때 쓰는 함수
    public LatestMsg(String msg,String mynum) {

        this.msg = msg;
        this.MyNum = mynum;
    }

    public String getMsg() {
        return msg;
    }



    public String getMyNum() {
        return MyNum;
    }


}
