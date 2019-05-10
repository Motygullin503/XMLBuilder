package ru.kpfu.plugin.utils;

import ru.kpfu.plugin.model.Child;

public class WidgetBuildHelper {

    public static final String HEADER = "Widget build(BuildContext context) {\n" +
            "\treturn Container(" + "\n\t\t";

    private StringBuilder widget = new StringBuilder();
    private Child mChild;


    public String build (Child child){

        mChild = child;

        widget.append(HEADER);

        if (mChild.getFlutterType() == 0) {
            widget.append("child: ").append(mChild.getFlutterName()).append("(),");
        }
        //else {
            //TODO
        //}

        widget.append("\n\t);\n}\n\n");

        return widget.toString();
    }
}
