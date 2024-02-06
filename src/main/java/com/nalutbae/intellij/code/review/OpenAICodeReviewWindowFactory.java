package com.nalutbae.intellij.code.review;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import com.intellij.ui.content.*;

import javax.swing.*;

public class OpenAICodeReviewWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JTextArea textArea = new JTextArea();
        textArea.setText("Hello OpenAI");
        JBScrollPane scrollPane = new JBScrollPane(textArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(scrollPane, "OpenAI Reviewer", false);
        toolWindow.getContentManager().addContent(content);
    }

}
