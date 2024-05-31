package Matching;

import android.os.CountDownTimer;

public class ItemModel {
    private String phone,image,gender,school, intro, job;
    private String name, age, city, hobbies, token, distance, msg, textFornums;
    private String img2,img3,img4,img5,img6;
    private String[] likes;
    private CountDownTimer ctimer;
    private String work;

    public ItemModel(String phone, String image, String gender, String school, String intro, String job, String name, String age, String city, String hobbies, String token, String distance,
                     String img2, String img3, String img4, String img5, String img6, String[] likes, String msg, CountDownTimer ctimer, String textFornums, String work) {

        this.phone = phone;
        this.image = image;
        this.gender = gender;
        this.school = school;
        this.intro = intro;
        this.job = job;
        this.name = name;
        this.age = age;
        this.city = city;
        this.hobbies = hobbies;
        this.token = token;
        this.distance = distance;
        this.msg = msg;
        this.textFornums = textFornums;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.img6 = img6;
        this.likes = likes;
        this.ctimer = ctimer;
        this.work = work;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTextFornums() {
        return textFornums;
    }

    public String setTextFornums(String textFornums) {
        this.textFornums = textFornums;
        return textFornums;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public CountDownTimer getCtimer() {
        return ctimer;
    }

    public void setCtimer(CountDownTimer ctimer) {
        this.ctimer = ctimer;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
