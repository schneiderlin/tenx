// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.example.linzihao97.plugindemo.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AnyDoorSettingsComponent {

  private final JPanel myMainPanel;
  private final JBTextField anyDoorPortText = new JBTextField();
  private final JBTextField versionText = new JBTextField();
  private final JBCheckBox enableAnyDoorBox = new JBCheckBox("Enable any-door");

  public AnyDoorSettingsComponent() {
    myMainPanel = FormBuilder.createFormBuilder()
//            .addComponent(enableAnyDoorBox, 1)
            .addLabeledComponent(new JBLabel("Enter project port: "), anyDoorPortText, 1, false)
            .addLabeledComponent(new JBLabel("Enter any-door jar version: "), versionText, 1, false)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return myMainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return anyDoorPortText;
  }

  @NotNull
  public String getAnyDoorPortText() {
    return anyDoorPortText.getText();
  }

  public void setAnyDoorPortText(@NotNull String newText) {
    anyDoorPortText.setText(newText);
  }

  public Boolean getEnableAnyDoorBox() {
    return enableAnyDoorBox.isSelected();
  }

  public void setEnableAnyDoorBox(boolean newStatus) {
    enableAnyDoorBox.setSelected(newStatus);
  }

  @NotNull
  public String getVersionText() {
    return versionText.getText();
  }

  public void setVersionText(@NotNull String newText) {
    versionText.setText(newText);
  }

}