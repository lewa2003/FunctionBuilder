package com.lewa2003.functionbuilder;

public class Parameter {
    private String name;
    private String place;
    private String type;

    Parameter () {
    }

    Parameter (String name, String place, String type) {
        this.name = name;
        this.place = place;
        this.type = type;
    }

    public String getName(){ return name; }
    public String getPlace() { return place; }
    public String getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setPlace(String place) { this.place = place; }
    public void setType(String type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parameter parameter = (Parameter) o;
        return (name == null ? parameter.getName() == null : name.equals(parameter.getName())) &&
                (place == null ? parameter.getPlace() == null : place.equals(parameter.getPlace())) &&
                (type == null ? parameter.getType() == null : type.equals(parameter.getType()));
    }
}
