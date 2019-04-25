package ru.kpfu.plugin;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ProjectComponent;

import org.jetbrains.annotations.NotNull;

public class TestProjectComponent implements ProjectComponent{
    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {
        System.out.println("THIS IS A TEST");
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "XMLBuilder";
    }
}
