package com.pinamu.ij.cgreview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;

public class CodeGenerattionPromptAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        String userInput = Messages.showInputDialog("Enter a description of the source code you wish to review.", "Request an OpenAI source code review", null);
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Document document = editor.getDocument();

        if (userInput != null) {
            String aiCode = requestCodeGeneration(userInput);
            Runnable runnable = () -> {
                CaretModel caretModel = editor.getCaretModel();
                int offset = caretModel.getCurrentCaret().getOffset();
                document.insertString(offset, aiCode);
            };
            WriteCommandAction.runWriteCommandAction(editor.getProject(), runnable);
        }
    }

    private String requestCodeGeneration(String query) {
        return OpenAIClient.request("only return java source code for next description: " + query);
    }
}
