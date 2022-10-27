// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.example.linzihao97.plugindemo.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AnyDoorSettingsConfigurable implements Configurable {

  private AnyDoorSettingsComponent mySettingsComponent;
  private Project project;


  public AnyDoorSettingsConfigurable(@NotNull Project project) {
    this.project = project;
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Settings Any Door";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return mySettingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    mySettingsComponent = new AnyDoorSettingsComponent(project);
    return mySettingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AayDoorSettingsState settings = project.getService(AayDoorSettingsState.class);
    return !mySettingsComponent.getAnyDoorPortText().equals(String.valueOf(settings.port)) ||
            !mySettingsComponent.getVersionText().equals(settings.version) ||
            !mySettingsComponent.getEnableAnyDoorBox().equals(settings.enable) ||
            !mySettingsComponent.getMainClassModuleText().equals(settings.runModule)
            ;
  }

  @Override
  public void apply() {
    AayDoorSettingsState settings = project.getService(AayDoorSettingsState.class);
    String anyDoorPortText = mySettingsComponent.getAnyDoorPortText();
    settings.port = NumberUtils.toInt(anyDoorPortText, settings.port);

    settings.enable = mySettingsComponent.getEnableAnyDoorBox();
    settings.version = mySettingsComponent.getVersionText();
    settings.runModule = mySettingsComponent.getMainClassModuleText();
  }

  @Override
  public void reset() {
    AayDoorSettingsState settings = project.getService(AayDoorSettingsState.class);
    mySettingsComponent.setAnyDoorPortText(String.valueOf(settings.port));
    mySettingsComponent.setEnableAnyDoorBox(settings.enable);
    mySettingsComponent.setVersionText(settings.version);
    mySettingsComponent.setMainClassModuleText(settings.runModule);
  }

  @Override
  public void disposeUIResources() {
    project = null;
    mySettingsComponent = null;
  }

}