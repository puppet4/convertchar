package com.tudo;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 将代码中中文字符替换成英文字符
 * @author lvkangjia
 * @date 2020/2/10
 */
public class EditorHandlerIllustration extends AnAction {
    static {
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        final TypedAction typedAction = actionManager.getTypedAction();
        typedAction.setupHandler(new CharReplaceTypedHandler(typedAction.getHandler()));
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    }
}

class CharReplaceTypedHandler implements TypedActionHandler {
    public static Map<String, String> cnCharMap = new HashMap<>();

    static {
        reload();
    }

    private TypedActionHandler orignTypedActionHandler;
    private char lastChar = ' ';

    public static void reload() {
        cnCharMap.clear();
        String[] configString = PropertiesComponent.getInstance().getValue(ConvertCharSettingComponent.KEY, ConvertCharSettingComponent.DEFAULT_STRING).split("\n");
        for (int i = 0; i < configString.length; i += 2) {
            cnCharMap.put(configString[i].trim(), configString[i + 1].trim());
        }
    }

    public CharReplaceTypedHandler(TypedActionHandler orignTypedActionHandler) {
        this.orignTypedActionHandler = orignTypedActionHandler;
    }

    @Override
    public void execute(@NotNull Editor editor, char c, @NotNull DataContext dataContext) {
        final Document document = editor.getDocument();
        final Project project = editor.getProject();
        final CaretModel caretModel = editor.getCaretModel();
        final Caret primaryCaret = caretModel.getPrimaryCaret();
        int caretOffset = primaryCaret.getOffset();
        String enChar = cnCharMap.get(String.valueOf(c));
        if (lastChar == '/' && enChar != null) {
            Runnable runnable = () -> {
                document.deleteString(caretOffset - 1, caretOffset);
                document.insertString(caretOffset - 1, String.valueOf(c));
                primaryCaret.moveToOffset(caretOffset);
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        } else if (enChar != null) {
            this.orignTypedActionHandler.execute(editor, enChar.charAt(0), dataContext);
        } else {
            this.orignTypedActionHandler.execute(editor, c, dataContext);
        }
        this.lastChar = c;
    }
}

