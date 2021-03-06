/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.yangutils.translator.tojava;

import org.onosproject.yangutils.datamodel.LocationInfo;

/**
 * Represents YANG java info containing interface for java code generator, java
 * file information, java import data and temp java code fragment files. This
 * interface serves as a generic interface and help to unify the generate code
 * entry function.
 */
public interface JavaCodeGeneratorInfo
        extends JavaFileInfoContainer, TempJavaCodeFragmentFilesContainer, LocationInfo {
}
