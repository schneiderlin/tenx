// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.example.linzihao97.plugindemo.settings;

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
//  private final JBCheckBox myIdeaUserStatus = new JBCheckBox("Do you use IntelliJ IDEA? ");

  public AnyDoorSettingsComponent() {
    myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Enter project port: "), anyDoorPortText, 1, false)
//            .addComponent(myIdeaUserStatus, 1)
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

//  public boolean getIdeaUserStatus() {
//    return myIdeaUserStatus.isSelected();
//  }
//
//  public void setIdeaUserStatus(boolean newStatus) {
//    myIdeaUserStatus.setSelected(newStatus);
//  }

}