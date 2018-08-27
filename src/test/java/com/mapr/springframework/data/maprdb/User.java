package com.mapr.springframework.data.maprdb;

import com.mapr.springframework.data.maprdb.core.mapping.Document;
import com.mapr.springframework.data.maprdb.core.mapping.MaprId;

@Document
public class User {

    @MaprId
    private String _id;
    private String name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (get_id() != null ? !get_id().equals(user.get_id()) : user.get_id() != null) return false;
        return getName() != null ? getName().equals(user.getName()) : user.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = get_id() != null ? get_id().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
