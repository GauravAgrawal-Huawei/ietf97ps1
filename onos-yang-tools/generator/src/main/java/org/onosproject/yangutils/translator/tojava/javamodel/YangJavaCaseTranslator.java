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
package org.onosproject.yangutils.translator.tojava.javamodel;

import java.io.IOException;

import org.onosproject.yangutils.datamodel.javadatamodel.YangJavaCase;
import org.onosproject.yangutils.translator.exception.TranslatorException;
import org.onosproject.yangutils.translator.tojava.JavaCodeGenerator;
import org.onosproject.yangutils.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.translator.tojava.TempJavaCodeFragmentFiles;
import org.onosproject.yangutils.utils.io.YangPluginConfig;

import static org.onosproject.yangutils.translator.tojava.GeneratedJavaFileType.GENERATE_INTERFACE_WITH_BUILDER;
import static org.onosproject.yangutils.translator.tojava.YangJavaModelUtils.generateCodeOfAugmentableNode;
import static org.onosproject.yangutils.translator.tojava.YangJavaModelUtils.generateJava;

/**
 * Represents case information extended to support java code generation.
 */
public class YangJavaCaseTranslator
        extends YangJavaCase
        implements JavaCodeGeneratorInfo, JavaCodeGenerator {

    private static final long serialVersionUID = 806201631L;

    /**
     * File handle to maintain temporary java code fragments as per the code
     * snippet types.
     */
    private transient TempJavaCodeFragmentFiles tempFileHandle;

    /**
     * Creates YANG java case object.
     */
    public YangJavaCaseTranslator() {
        super();
        setJavaFileInfo(new JavaFileInfoTranslator());
        getJavaFileInfo().setGeneratedFileTypes(GENERATE_INTERFACE_WITH_BUILDER);
    }

    /**
     * Returns the generated java file information.
     *
     * @return generated java file information
     */
    @Override
    public JavaFileInfoTranslator getJavaFileInfo() {
        if (javaFileInfo == null) {
            throw new TranslatorException("Missing java info in java datamodel node" +
                    getName() + " in " +
                    getLineNumber() + " at " +
                    getCharPosition()
                    + " in " + getFileName());
        }
        return (JavaFileInfoTranslator) javaFileInfo;
    }

    /**
     * Sets the java file info object.
     *
     * @param javaInfo java file info object
     */
    @Override
    public void setJavaFileInfo(JavaFileInfoTranslator javaInfo) {
        javaFileInfo = javaInfo;
    }

    /**
     * Returns the temporary file handle.
     *
     * @return temporary file handle
     */
    @Override
    public TempJavaCodeFragmentFiles getTempJavaCodeFragmentFiles() {
        return tempFileHandle;
    }

    /**
     * Sets temporary file handle.
     *
     * @param fileHandle temporary file handle
     */
    @Override
    public void setTempJavaCodeFragmentFiles(TempJavaCodeFragmentFiles fileHandle) {
        tempFileHandle = fileHandle;
    }

    /**
     * Prepare the information for java code generation corresponding to YANG
     * case info.
     *
     * @param yangPlugin YANG plugin config
     * @throws TranslatorException translator operation fail
     */
    @Override
    public void generateCodeEntry(YangPluginConfig yangPlugin) throws TranslatorException {
        try {
            if (getReferredSchema() != null) {
                return;
            }
            generateCodeOfAugmentableNode(this, yangPlugin);
        } catch (IOException e) {
            throw new TranslatorException(
                    "Failed to prepare generate code entry for case node " +
                            getName() + " in " +
                            getLineNumber() + " at " +
                            getCharPosition()
                            + " in " + getFileName() + " " + e.getLocalizedMessage());
        }
    }

    /**
     * Creates a java file using the YANG case info.
     */
    @Override
    public void generateCodeExit() throws TranslatorException {
        try {
            generateJava(GENERATE_INTERFACE_WITH_BUILDER, this);
        } catch (IOException e) {
            throw new TranslatorException("Failed to generate code for case node " +
                    getName() + " in " +
                    getLineNumber() + " at " +
                    getCharPosition()
                    + " in " + getFileName() + " " + e.getLocalizedMessage());
        }
    }

}
