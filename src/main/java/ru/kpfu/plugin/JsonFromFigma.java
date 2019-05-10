package ru.kpfu.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.UIBundle;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import ru.kpfu.plugin.model.Child;
import ru.kpfu.plugin.model.Document;
import ru.kpfu.plugin.model.FigmaModel;
import ru.kpfu.plugin.network.FigmaRetrofit;
import ru.kpfu.plugin.utils.WidgetBuildHelper;
import ru.kpfu.plugin.utils.WidgetComposeHelper;

public class JsonFromFigma extends AnAction {

    private static final String NAMESPACES = " xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\" \n";

    private OutputStream newFile;
    private ArrayList<Tag> tagList;
    private Tag rootView;
    private ArrayList<String> recyclerItems = new ArrayList<>();
    private FigmaRetrofit retrofit;
    private List<Child> previous;
    private List<Child> output = new ArrayList<>();
    private WidgetComposeHelper widgetComposeHelper = new WidgetComposeHelper();
    private WidgetBuildHelper widgetBuildHelper = new WidgetBuildHelper();

    public JsonFromFigma() {
        super();
        retrofit = new FigmaRetrofit();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        String newFileName;

        while (true) {
            newFileName = Messages.showInputDialog("Insert FileID",
                    "Get Json", Messages.getQuestionIcon());

            if (newFileName == null) {
                return;
            } else {
                FigmaModel model = null;
                Response<FigmaModel> modelResponse;
                try {
                    modelResponse = retrofit.getService().getJson(newFileName).execute();
                    model = modelResponse.body();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (model != null) {
                    Document doc = model.getDocument();
                    List<Child> children = doc.getChildren();

                    addToChildList(children);

                    for (Child out : output) {

                        System.out.println(out.getName() + " at the x: "
                                + out.getBoundingBox().getX()
                                + "/ y: " + out.getBoundingBox().getY()
                                + " width: " + out.getBoundingBox().getWidth()
                                + " height: " + out.getBoundingBox().getHeight());

                        if (widgetComposeHelper.checkName(out)) {
                            Child widget = widgetComposeHelper.returnWidget(out);

                            writeToFile(widgetBuildHelper.build(widget));
                        }


                    }

                }


            }

            if ("".equals(newFileName.trim())) {
                Messages.showMessageDialog(UIBundle.message("create.new.file.file.name.cannot.be.empty.error.message"),
                        UIBundle.message("error.dialog.title"), Messages.getErrorIcon());
                return;
            }
        }
    }

    private void writeToFile(String output) {
        try {
            OutputStream itemFile = new FileOutputStream("C:\\Users\\Bulat\\IdeaProjects\\test\\src\\test" + ".dart");
            itemFile.write(output.getBytes());
            itemFile.flush();
            itemFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addToChildList(List<Child> children) {

        List<Child> newChildren = new ArrayList<Child>();

        for (Child child : children) {

            if (child.getBoundingBox() != null) {
                output.add(child);
            }

            newChildren = child.getChildren();

            if (newChildren != null) {
                addToChildList(newChildren);
            }
        }


    }

    private void encloseRootView(OutputStream file) throws IOException {
        file.write(("</" + rootView.getName() + ">").getBytes());
    }

    private void writeXmlHeader(OutputStream file) throws IOException {
        file.write("<?xml version=\"1.0\" encoding=\"utf-8\"?> \n".getBytes());
    }

    private void writeClassesAndValues(ArrayList<Tag> list, OutputStream file) throws IOException {
        Tag previousTag = null;

        for (Tag tag : list) {

            if (previousTag == null) {
                rootView = tag;
                writeRootView(tag, file);
                previousTag = tag;
                continue;
            }

            previousTag = tag;

            file.write(
                    checkProperties(tag, false).getBytes());
            insertTagEnding(tag, file);
        }

    }

    private void writeToRecyclerViewFile() {
        for (int i = 0; i < recyclerItems.size(); i++) {
            try {
                OutputStream itemFile = new FileOutputStream("C:\\Users\\Bulat\\IdeaProjects\\untitled\\src\\item" + i + ".xml");
                writeXmlHeader(itemFile);
                writeClassesAndValues(findAllClasses(recyclerItems.get(i)), itemFile);
                encloseRootView(itemFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void insertTagEnding(Tag tag, OutputStream file) throws IOException {
        file.write(("\n" + "</" + tag.getName() + ">" + "\n").getBytes());
    }


    private void writeRootView(Tag tag, OutputStream file) throws IOException {

        file.write(checkProperties(tag, true).getBytes());

    }

    private String checkProperties(Tag tag, boolean isRoot) {
        if (isRoot) {
            if (tag.getValues().isEmpty()) {
                return appendStandardTags("<" + tag.getName() + NAMESPACES);

            } else {
                return appendCustomTags("<" + tag.getName() + NAMESPACES, tag.getValues());
            }
        } else {
            if (tag.getValues().isEmpty()) {
                return appendStandardTags("\n" + "<" + tag.getName());
            } else {
                return appendCustomTags("\n" + "<" + tag.getName(), tag.getValues());
            }
        }
    }

    private ArrayList<Tag> findAllClasses(String input) {

        ArrayList<Tag> list = new ArrayList<>();

        while (input.contains("{")) {

            int start = input.indexOf("{") + 1;
            int end = input.indexOf("}") - 1;

            recyclerItems.add(input.substring(start, end));
            input = input.replace(input.substring(start - 1, end + 2), "");
        }

        String[] split = input.split("\\)");
        Tag tag;
        for (String out : split) {

            if (!out.matches("^[\\s]+")) {
                int valuesStart = out.lastIndexOf("(") + 1;
                int valuesEnd = out.length();
                HashMap<String, String> properties = new HashMap<>();
                String stringValues = out.substring(valuesStart, valuesEnd);
                String[] propArray = stringValues.split("=");
                int index = 0;
                String currentKey = "";
                for (String keyOrValue : propArray) {
                    if (index % 2 == 0) {
                        currentKey = keyOrValue;
                    } else {
                        properties.put(currentKey, keyOrValue);
                    }
                    index++;
                }
                out = out.substring(0, valuesStart);
                int level = 0;

                while (out.startsWith(" ") | out.startsWith("\r\n") | out.startsWith("\t")) {
                    if (out.startsWith("\r\n")) {
                        out = out.replaceFirst("\r\n", "");
                    } else if (out.startsWith(" ")) {
                        out = out.replaceFirst(" ", "");
                        level++;
                    } else {
                        out = out.replaceFirst("\t", "");
                        level += 4;
                    }
                }

                tag = new Tag(out.replace("(", "").replace(" ", ""), properties, level);
                System.out.println(tag);
                list.add(tag);
            }

        }
        return list;
    }


    private String appendCustomTags(String toWhat, HashMap<String, String> properties) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toWhat + "\n");
        for (String key : properties.keySet()) {
            buffer.append("\t" + "android:" + key + "=" + "\"" + properties.get(key) + "\"" + "\n");
        }
        buffer.replace(buffer.lastIndexOf("\n"), buffer.length(), ">");
        return buffer.toString();
    }

    private String appendStandardTags(String toWhat) {
        return toWhat + "\n" +
                "\t" + "android:layout_width=\"wrap_content\"" + "\n" +
                "\t" + "android:layout_height=\"wrap_content\"" + ">";
    }

    private static String getPaths(VirtualFile[] files) {
        StringBuilder buf = new StringBuilder(files.length * 64);
        for (VirtualFile file : files) {
            if (buf.length() > 0) buf.append('\n');
            buf.append(file.getPresentableUrl());
        }
        return buf.toString();
    }


}
