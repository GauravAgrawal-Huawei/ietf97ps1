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

package org.onosproject.yangutils.translator.tojava.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoContainer;
import org.onosproject.yangutils.translator.tojava.JavaImportData;
import org.onosproject.yangutils.translator.tojava.JavaQualifiedTypeInfoTranslator;
import org.onosproject.yangutils.translator.tojava.TempJavaFragmentFiles;

import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCapitalCase;

/**
 * Represent the extends list for generated java classes. It holds the class details which needs to be extended by the
 * generated java code.
 */
public class JavaExtendsListHolder {

    private Map<JavaQualifiedTypeInfoTranslator, Boolean> extendedClassStore;
    private List<JavaQualifiedTypeInfoTranslator> extendsList;

    /**
     * Creates an instance of JavaExtendsListHolder.
     */
    public JavaExtendsListHolder() {
        setExtendedClassStore(new HashMap<>());
        setExtendsList(new ArrayList<>());
    }

    /**
     * Returns extends list.
     *
     * @return extends list
     */
    Map<JavaQualifiedTypeInfoTranslator, Boolean> getExtendedClassStore() {
        return extendedClassStore;
    }

    /**
     * Sets extends list.
     *
     * @param extendedClass map of classes need to be extended
     */
    private void setExtendedClassStore(Map<JavaQualifiedTypeInfoTranslator, Boolean> extendedClass) {
        extendedClassStore = extendedClass;
    }

    /**
     * Adds to the extends list.
     *
     * @param info                  java file info
     * @param node                  YANG node
     * @param tempJavaFragmentFiles temp java fragment files
     */
    public void addToExtendsList(JavaQualifiedTypeInfoTranslator info, YangNode node,
                                 TempJavaFragmentFiles tempJavaFragmentFiles) {
        JavaFileInfoTranslator fileInfo = ((JavaFileInfoContainer) node).getJavaFileInfo();

        JavaImportData importData = tempJavaFragmentFiles.getJavaImportData();
        boolean qualified = importData.addImportInfo(info,
                getCapitalCase(fileInfo.getJavaName()), fileInfo.getPackage());

            /*true means import should be added*/
        getExtendedClassStore().put(info, qualified);

        addToExtendsList(info);
    }

    /**
     * Returns extends list.
     *
     * @return the extendsList
     */
    public List<JavaQualifiedTypeInfoTranslator> getExtendsList() {
        return extendsList;
    }

    /**
     * Sets extends info list.
     *
     * @param classInfoList the extends List to set
     */
    private void setExtendsList(List<JavaQualifiedTypeInfoTranslator> classInfoList) {
        extendsList = classInfoList;
    }

    /**
     * Adds extends info to list.
     *
     * @param classInfo class info
     */
    private void addToExtendsList(JavaQualifiedTypeInfoTranslator classInfo) {
        getExtendsList().add(classInfo);
    }

}
