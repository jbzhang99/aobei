package custom.bean;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TimeModel implements Serializable{

    private static final long serialVersionUID = -5260459409611265181L;
    String dateTime;
    Date date;
    boolean active;
    Set<Model> models;

    public void setModels(Set<Model> models) {
        this.models = models;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void addModel(String start, String end, boolean active) {
        Model model = new Model();
        model.setDateTime(dateTime);
        model.setStart(start);
        model.setEnd(end);
        model.setActive(active);
        model.setDateStart(dateTime + " " + start);
        if(end!=null){
            model.setDateEnd(dateTime + " " + end);
        }
        if (models == null) {
            models = new TreeSet<>();
        }
        if (model.isActive() && models.contains(model)) {
            models.remove(model);
        }
        models.add(model);
    }

    public Model getModel(String dateTime, String start, String end) {
        Model tmp = new Model();
        tmp.setDateTime(dateTime);
        tmp.setStart(start);
        tmp.setEnd(end);
        tmp.setDateStart(dateTime + " " + start);
        if(end!=null){
            tmp.setDateEnd(dateTime + " " + end);
        }
        for (Model model : models) {
            if (model.equals(tmp))
                return model;
        }
        return null;
    }

    public Set<Model> getModels() {
        return models;
    }

    public class Model implements Comparable<Model> {
        String dateTime;
        String start;
        String end;
        String dateStart;
        String dateEnd;
        int num;
        boolean active;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getDateStart() {
            return dateStart;
        }

        public void setDateStart(String dateStart) {
            this.dateStart = dateStart;
        }

        public String getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(String dateEnd) {
            this.dateEnd = dateEnd;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return active == model.active &&
                    Objects.equals(start, model.start) &&
                    Objects.equals(end, model.end) &&
                    Objects.equals(dateStart, model.dateStart) &&
                    Objects.equals(dateEnd, model.dateEnd);
        }

        @Override
        public int hashCode() {

            return Objects.hash(start, end, active);
        }

        @Override
        public int compareTo(Model o) {
            if (this == o) return 0;
            if (this.start.compareTo(o.start) == 0 &&
                    this.end.compareTo(o.end) == 0 &&
                    this.dateStart.compareTo(o.dateStart) == 0 &&
                    this.dateEnd.compareTo(o.dateEnd) == 0 &&
                    this.dateTime.compareTo(o.dateTime) == 0)
                return 0;
            return this.start.compareTo(o.start) +
                    this.start.compareTo(o.end) +
                    this.dateStart.compareTo(o.dateStart) +
                    this.dateEnd.compareTo(o.dateEnd) +
                    this.dateTime.compareTo(o.dateTime);
        }
    }

    public boolean dd() {


        TimeModel timeModel = new TimeModel();
        timeModel.setDateTime("2010-03-20");
        timeModel.addModel("08:00", "10:00", false);
        timeModel.addModel("08:00", "10:00", true);
        timeModel.addModel("08:00", "10:00", false);


        return true;

    }

    public static void main(String[] args) {

        List<TimeModel> list = new ArrayList<>();


        TimeModel timeModel = new TimeModel();
        timeModel.setDateTime("2010-03-20");
        timeModel.addModel("08:00", "10:00", false);
        timeModel.addModel("08:00", "10:00", true);
        timeModel.addModel("08:00", "10:00", false);
        list.add(timeModel);
        int num = 3;

        list.stream().map(t -> {

            Set<TimeModel.Model> models = t.getModels();
            for (TimeModel.Model model : models) {
                if (model.getNum() < num) {
                    model.setActive(false);
                }
            }
            return t;

        }).collect(Collectors.toList());

        Model model  = list.get(0).getModel("2010-03-20","08:00","10:00");
        System.out.println(model.isActive());

    }
}
