package com.pinamu.ij.cgreview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ReviewCodeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] selectedFiles = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (project != null && selectedFiles != null) {
            for (VirtualFile file : selectedFiles) {
                if (!file.isDirectory() && file.getFileType().getName().equals("JAVA")) {
                    String code = FileDocumentManager.getInstance().getDocument(file).getText();
                    String review = requestCodeReview(code);

                    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
                    ToolWindow toolWindow = toolWindowManager.getToolWindow("OpenAI Reviewer");
                    ContentManager contentManager = toolWindow.getContentManager();
                    Content content = contentManager.getContent(0);
                    JBScrollPane panel = (JBScrollPane) content.getComponent();
                    JTextArea textArea = (JTextArea) panel.getViewport().getView();
                    textArea.setText(review);
                    Content newContent = contentManager.getFactory().createContent(panel, "", false);

                    contentManager.removeContent(content, true);
                    contentManager.addContent(newContent);
                }
            }
        }
    }

    private String requestCodeReview(String code) {
        return OpenAIClient.request(
                "Please review the following source code and provide suggestions on how to improve it. " +
                "If refactoring is necessary, please let us know the refactored code along with an explanation.:\n" +
                code);
    }
}
