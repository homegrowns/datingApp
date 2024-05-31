package Utills;


public class NotiModel {

    public String to;

    public Notification notification = new Notification();
    public Data data = new Data();

     public static class Notification {
        public String title;
        public String text;
         public String body;
         public String clickAction;


     }
    public static class Data{
         public String title;
         public String text;
        public String body;
        public String clickAction;


    }
}
