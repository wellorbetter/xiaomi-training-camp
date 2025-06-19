package com.example.day6.events;

/**
 * @author wellorbetter
 */

public class UpdateLikeEvent {
    private int position;
    private boolean isLike;

    public UpdateLikeEvent(int position, boolean isLike) {
        this.position = position;
        this.isLike = isLike;
    }

    public int getPosition() {
        return position;
    }

    public boolean isLike() {
        return isLike;
    }
}
