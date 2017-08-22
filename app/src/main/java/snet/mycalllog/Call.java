package snet.mycalllog;


import java.util.Date;

public class Call {

    private String numberPhone;
    private String status;
    private String date;
    private String dateTime;
    private String duration;

    public Call(String numberPhone, String status, String date, String dateTime, String duration) {
        this.numberPhone = numberPhone;
        this.status = status;
        this.date = date;
        this.dateTime = dateTime;
        this.duration = duration;
    }




    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Call{" +
                "numberPhone='" + numberPhone + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
