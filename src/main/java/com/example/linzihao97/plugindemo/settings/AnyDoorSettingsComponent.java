// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.example.linzihao97.plugindemo.settings;

import com.example.linzihao97.plugindemo.tenx.MyNotifier;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AnyDoorSettingsComponent {

  private Project project;
  private final JPanel myMainPanel;
  private final JBTextField anyDoorPortText = new JBTextField();
  private final JBTextField versionText = new JBTextField();
  private final JBCheckBox enableAnyDoorBox = new JBCheckBox("Enable any-door");
  private final JButton button = new JButton("xxx");

  public AnyDoorSettingsComponent(Project project) {
    this.project = project;
    button.addActionListener(e -> {
      MyNotifier.notifyInfo(project, "yyds");
      //ModuleManager.getInstance(project).getModules()[0].get()
    });
    myMainPanel = FormBuilder.createFormBuilder()
//            .addComponent(enableAnyDoorBox, 1)
            .addLabeledComponent(new JBLabel("Enter project port: "), anyDoorPortText, 1, false)
            .addLabeledComponent(new JBLabel("Enter any-door jar version: "), versionText, 1, false)
            .addComponent(button)
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