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

package org.onosproject.yangutils.parser.impl.listeners;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.onosproject.yangutils.datamodel.YangAtomicPath;
import org.onosproject.yangutils.datamodel.YangContainer;
import org.onosproject.yangutils.datamodel.YangDeviation;
import org.onosproject.yangutils.datamodel.YangLeaf;
import org.onosproject.yangutils.datamodel.YangLeavesHolder;
import org.onosproject.yangutils.datamodel.YangModule;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.linker.impl.YangLinkerManager;
import org.onosproject.yangutils.parser.exceptions.ParserException;
import org.onosproject.yangutils.parser.impl.YangUtilsParserManager;
import org.onosproject.yangutils.plugin.manager.YangUtilManager;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoContainer;
import org.onosproject.yangutils.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yangutils.utils.io.YangPluginConfig;
import org.onosproject.yangutils.utils.io.impl.YangFileScanner;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.onosproject.yangutils.datamodel.YangNodeType.MODULE_NODE;
import static org.onosproject.yangutils.datamodel.utils.ResolvableStatus.UNRESOLVED;
import static org.onosproject.yangutils.linker.impl.YangLinkerUtils.updateFilePriority;
import static org.onosproject.yangutils.utils.io.YangPluginConfig.compileCode;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.deleteDirectory;

/**
 * Test cases for testing deviation listener.
 */
public class DeviationListenerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final YangUtilsParserManager manager = new YangUtilsParserManager();
    private static final String DIR = "target/deviationTest/";
    private final YangUtilManager utilManager = new YangUtilManager();
    private final YangLinkerManager yangLinkerManager = new YangLinkerManager();
    private static final String COMP = System.getProperty("user.dir") + File
            .separator + DIR;


    /**
     * Checks deviation statement as sub-statement of module.
     */
    @Test
    public void processDeviationNotSupported() throws IOException,
            ParserException {

        YangNode node = manager
                .getDataModel("src/test/resources/" +
                                      "ValidDeviationNotSupported.yang");

        assertThat((node instanceof YangModule), is(true));

        // Check whether the node type is set properly to module.
        assertThat(node.getNodeType(), is(MODULE_NODE));

        // Check whether the module name is set correctly.
        YangModule yangNode = (YangModule) node;
        assertThat(yangNode.getName(), is("Test"));

        // Check whether the container is child of module
        YangDeviation deviation = (YangDeviation) yangNode.getChild();
        assertThat(deviation.getName(), is("/base:system/base:daytime"));
        assertThat(deviation.getDescription(), is("\"desc\""));
        assertThat(deviation.getReference(), is("\"ref\""));
        assertThat(deviation.isDeviateNotSupported(), is(true));
        List<YangAtomicPath> targetNode = deviation.getTargetNode();
        assertThat(targetNode.get(0).getNodeIdentifier().getName(),
                   is("system"));
        assertThat(targetNode.get(1).getNodeIdentifier().getName(),
                   is("daytime"));
        assertThat(targetNode.get(0).getNodeIdentifier().getPrefix(),
                   is("base"));
        assertThat(deviation.getResolvableStatus(), is(UNRESOLVED));
    }

    /**
     * Checks deviation statement as sub-statement of module.
     */
    @Test
    public void processDeviationNotSupportedLinking() throws IOException,
            ParserException {

        String searchDir = "src/test/resources/deviationlinking";
        utilManager.createYangFileInfoSet(YangFileScanner.getYangFiles(searchDir));
        utilManager.parseYangFileInfoSet();
        utilManager.createYangNodeSet();

        YangNode refNode;
        YangNode selfNode;

        // Create YANG node set
        yangLinkerManager.createYangNodeSet(utilManager.getYangNodeSet());

        // Add references to import list.
        yangLinkerManager.addRefToYangFilesImportList(utilManager.getYangNodeSet());

        updateFilePriority(utilManager.getYangNodeSet());

        Iterator<YangNode> yangNodeIterator = utilManager.getYangNodeSet().iterator();

        YangNode rootNode = yangNodeIterator.next();

        if (rootNode.getName().equals("deviation-module")) {
            selfNode = rootNode;
            refNode = yangNodeIterator.next();
        } else {
            refNode = rootNode;
            selfNode = yangNodeIterator.next();
        }

        // Check whether the node type is set properly to module.
        assertThat(selfNode.getNodeType(), is(MODULE_NODE));

        // Check whether the deviation module info is set correctly after
        // parsing.
        YangModule yangNode = (YangModule) selfNode;
        assertThat(yangNode.getName(), is("deviation-module"));

        // Check whether the container is child of module
        YangDeviation deviation = (YangDeviation) yangNode.getChild();
        assertThat(deviation.getName(), is("/t:ospf"));
        assertThat(deviation.getDescription(), is("\"desc\""));
        assertThat(deviation.getReference(), is("\"ref\""));
        assertThat(deviation.isDeviateNotSupported(), is(true));
        List<YangAtomicPath> targetNode = deviation.getTargetNode();
        assertThat(targetNode.get(0).getNodeIdentifier().getName(), is("ospf"));
        assertThat(targetNode.get(0).getNodeIdentifier().getPrefix(), is("t"));
        assertThat(deviation.getResolvableStatus(), is(UNRESOLVED));

        // Check whether the base module - test information is set correctly.
        YangModule yangRefNode = (YangModule) refNode;
        assertThat(yangRefNode.getName(), is("Test"));

        YangNode ospfNode = yangRefNode.getChild();
        assertThat(ospfNode.getName(), is("ospf"));

        YangNode testValid = ospfNode.getNextSibling();
        assertThat(testValid.getName(), is("valid"));

        assertThat(testValid.getNextSibling(), nullValue());

        // Carry out inter-file linking.
        yangLinkerManager.processInterFileLinking(utilManager.getYangNodeSet());
        YangPluginConfig yangPluginConfig = new YangPluginConfig();
        yangPluginConfig.setCodeGenDir(DIR);
        utilManager.translateToJava(yangPluginConfig);

        compileCode(COMP);
        deleteDirectory(DIR);

        // Check whether the data model tree returned is of type module.
        assertThat(selfNode instanceof YangModule, is(true));

        // Check whether the node type is set properly to module.
        assertThat(selfNode.getNodeType(), is(MODULE_NODE));


        // Check whether the module name is set correctly.
        yangRefNode = (YangModule) refNode;
        assertThat(yangRefNode.getName(), is("Test"));

        System.out.println("");
        System.out.println("TargetNode");
        System.out.println("module " + yangRefNode.getName());

        YangNode childNode2 = yangRefNode.getChild();
        assertThat(childNode2.getName(), is("ospf"));
        System.out.println("    container " + childNode2.getName());
        System.out.println("        leaf " + ((YangLeavesHolder) childNode2)
                .getListOfLeaf().get(0).getName());

        testValid = childNode2.getNextSibling();
        assertThat(testValid.getName(), is("valid"));
        System.out.println("    container " + testValid.getName());
        System.out.println("        leaf " + ((YangLeavesHolder) testValid)
                .getListOfLeaf().get(0).getName());

        // Check whether the module name is set correctly.
        yangNode = (YangModule) selfNode;
        assertThat(yangNode.getName(), is("deviation-module"));
        System.out.println("");
        System.out.println("Deviation module ");
        System.out.println("module " + yangNode.getName());
        System.out.println("    deviation " + yangNode.getChild().getName());

        YangNode deviationValid = yangNode.getChild().getNextSibling()
                .getNextSibling();
        assertThat(deviationValid.getName(), is("valid"));
        System.out.println("    container " + deviationValid.getName());
        List<YangLeaf> lisfOfLeaf = ((YangLeavesHolder) deviationValid).getListOfLeaf();
        assertThat(lisfOfLeaf.isEmpty(), is(true));
        assertThat(deviationValid.getNextSibling(), nullValue());

        assertThat(testValid.getYangSchemaNodeIdentifier(), is
                (deviationValid.getYangSchemaNodeIdentifier()));

        JavaFileInfoTranslator deviateJavaFile = ((JavaFileInfoContainer)
                deviationValid).getJavaFileInfo();

        JavaFileInfoTranslator testJavaFile = ((JavaFileInfoContainer)
                testValid).getJavaFileInfo();
        assertThat(testJavaFile, is(deviateJavaFile));
    }
}

