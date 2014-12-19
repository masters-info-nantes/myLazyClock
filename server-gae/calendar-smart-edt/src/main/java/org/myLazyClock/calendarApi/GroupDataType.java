package org.myLazyClock.calendarApi;

/**
 * Created by jeremy on 09/12/14.
 */
public enum GroupDataType {
    TEACHER("teachers"),
    ROOM("rooms"),
    GROUP("groups"),
    COURSE_NAME("courseNames"),
    COURSE_CATEGORY("courseCategory");

    private final String name;

    private GroupDataType(String s) {
        name = s;
    }

    public String getName(){
        return name;
    }
}
