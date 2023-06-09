package ru.yandex.practicum.filmorate.model;

public enum FriendshipStatuses {
    REQESTED,
    APPROVED,
    REJECTED;

    public static int getIdByEnumName(FriendshipStatuses friendshipStatuses) {
        switch (friendshipStatuses) {
            case REQESTED: return 1;
            case APPROVED: return 2;
            case REJECTED: return 3;
            default: return 0;
        }
    }
}
