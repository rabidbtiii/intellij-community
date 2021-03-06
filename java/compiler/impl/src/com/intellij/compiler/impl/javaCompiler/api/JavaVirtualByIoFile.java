/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.compiler.impl.javaCompiler.api;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * User: cdr
 */
@SuppressWarnings({"Since15"})
class JavaVirtualByIoFile extends FileVirtualObject {
  private final File myFile;

  protected JavaVirtualByIoFile(File file, Kind kind) {
    super(file.toURI(), kind);
    myFile = file;
  }

  protected VirtualFile getVirtualFile() {
    return LocalFileSystem.getInstance().findFileByIoFile(myFile);
  }
}