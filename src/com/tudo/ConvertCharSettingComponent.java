package com.tudo;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConvertCharSettingComponent implements SearchableConfigurable {

    public static final int LENGTH = 40;
    public static final String DEFAULT_STRING = "，\n,\n。\n.\n；\n;\n！\n!\n【\n[\n】\n]\n（\n(\n）\n)\n「\n{\n」\n}\n《\n<\n》\n>";
    public static final String KEY = "welcome to convertchar";
    private JPanel settingPanel;
    private JTextField[] text1;
    private JTextField[] text2;
    private JLabel[] labels1;
    private JLabel[] labels2;


    @NotNull
    public String getId() {
        return "中文字符替换";
    }

    @Nls
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    public String getHelpTopic() {
        return getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingPanel != null) {
            settingPanel.repaint();
            return settingPanel;
        }
        settingPanel = new JPanel();
        settingPanel.setLayout(null);
        text1 = new JTextField[LENGTH];
        text2 = new JTextField[LENGTH];
        labels1 = new JLabel[LENGTH];
        labels2 = new JLabel[LENGTH];
        String[] configString = PropertiesComponent.getInstance().getValue(KEY, DEFAULT_STRING).split("\n");
        for (int i = 0; i < LENGTH; i++) {
            text1[i] = new JTextField();
            text2[i] = new JTextField();
            labels1[i] = new JLabel();
            labels2[i] = new JLabel();
            text1[i].setBounds(35 + (i / 15) * 200, 32 * (i % 15), 60, 32);
            text2[i].setBounds(120 + (i / 15) * 200, 32 * (i % 15), 60, 32);
            labels1[i].setBounds(5 + (i / 15) * 200, 32 * (i % 15), 30, 32);
            labels2[i].setBounds(95 + (i / 15) * 200, 32 * (i % 15), 25, 32);
            text1[i].setText((2 * i) < configString.length ? configString[2 * i].trim() : "");
            text2[i].setText((2 * i + 1) < configString.length ? configString[2 * i + 1].trim() : "");
            labels1[i].setText((i + 1) + ".");
            labels2[i].setText("->");
            labels1[i].setHorizontalAlignment(JLabel.CENTER);
            labels2[i].setHorizontalAlignment(JLabel.CENTER);
            text1[i].setHorizontalAlignment(JLabel.CENTER);
            text2[i].setHorizontalAlignment(JLabel.CENTER);
            settingPanel.add(text1[i]);
            settingPanel.add(text2[i]);
            settingPanel.add(labels1[i]);
            settingPanel.add(labels2[i]);
        }
        return settingPanel;
    }

    //用户修改配置参数后，在点击“OK”“Apply”按钮前，框架会自动调用该方法，判断是否有修改，进而控制按钮“OK”“Apply”的是否可用。
    @Override
    public boolean isModified() {
        String oldStr = PropertiesComponent.getInstance().getValue(KEY, DEFAULT_STRING).trim();
        String newStr = getConfigString().trim();
        return !newStr.equals(oldStr);
    }

    //用户点击“OK”或“Apply”按钮后会调用该方法，通常用于完成配置信息持久化。
    @Override
    public void apply() {
        String str = getConfigString();
        PropertiesComponent.getInstance().setValue(KEY, str);
        CharReplaceTypedHandler.reload();
    }

    //点reset按钮,打开页面时调用
    @Override
    public void reset() {
        String str = PropertiesComponent.getInstance().getValue(KEY, DEFAULT_STRING);
        String[] configString = str.split("\n");
        for (int i = 0; i < LENGTH; i++) {
            text1[i].setText((2 * i) < configString.length ? configString[2 * i].trim() : "");
            text2[i].setText((2 * i + 1) < configString.length ? configString[2 * i + 1].trim() : "");
        }
        PropertiesComponent.getInstance().setValue(KEY, str);
        CharReplaceTypedHandler.reload();
    }

    private String getConfigString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            sb.append(text1[i].getText().trim()).append("\n").append(text2[i].getText().trim()).append("\n");
        }
        return sb.toString();
    }

}
