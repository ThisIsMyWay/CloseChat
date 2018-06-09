package com.closechat.closechat;

import android.support.annotation.NonNull;

import java.util.Objects;

public class Friend implements Comparable<Friend>{
    private final String name;
    private final String avatarUrl;

    public Friend(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(name, friend.name) &&
                Objects.equals(avatarUrl, friend.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatarUrl);
    }

    @Override
    public int compareTo(@NonNull Friend o) {
        return this.name.compareTo(o.name);
    }
}
