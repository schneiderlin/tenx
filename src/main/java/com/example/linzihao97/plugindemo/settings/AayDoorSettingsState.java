// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.example.linzihao97.plugindemo.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.example.linzihao97.plugindemo.settings.AayDoorSettingsState",
        storages = @Storage("SdkSettingsPlugin.xml")
)
public class AayDoorSettingsState implements PersistentStateComponent<AayDoorSettingsState> {

  public Integer port = 8080;
//  public boolean ideaStatus = false;

//  public static AppSettingsState getInstance() {
//    return ApplicationManager.getApplication().getService(AppSettingsState.class);
//  }

  @Nullable
  @Override
  public AayDoorSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull AayDoorSettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}