package ru.kpfu.plugin.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.kpfu.plugin.model.Child;

public class WidgetComposeHelper {

    private Child mChild;
    private static final String[] GROUP_VALUES = new String[] { "Row", "AppBar" };
    private static final Set<String> GROUPS_SET = new HashSet<>(Arrays.asList(GROUP_VALUES));


    public boolean checkName(Child child) {
        String previousName = child.getName();
        return previousName.contains("#");
    }

    private Child setName() {
        String previousName = mChild.getName();

        if (previousName.contains("#")) {
            mChild.setFlutterName(previousName.substring(previousName.indexOf("#") + 1));
        }

        return mChild;
    }

    private Child checkIsGroup() {

        if (GROUPS_SET.contains(mChild.getFlutterName())) {
            mChild.setFlutterType(1);
        } else {
            mChild.setFlutterType(0);
        }

        return mChild;
    }

    public Child returnWidget(Child child) {
        mChild = child;
        setName();
        checkIsGroup();
        return mChild;
    }
}
