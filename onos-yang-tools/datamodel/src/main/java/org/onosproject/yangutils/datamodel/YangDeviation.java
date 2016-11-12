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


package org.onosproject.yangutils.datamodel;

import org.onosproject.yangutils.datamodel.exceptions.DataModelException;
import org.onosproject.yangutils.datamodel.utils.Parsable;
import org.onosproject.yangutils.datamodel.utils.ResolvableStatus;
import org.onosproject.yangutils.datamodel.utils.YangConstructType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.onosproject.yangutils.datamodel.YangSchemaNodeType.YANG_NON_DATA_NODE;
import static org.onosproject.yangutils.datamodel.utils.DataModelUtils.detectCollidingChildUtil;
import static org.onosproject.yangutils.datamodel.utils.YangConstructType.DEVIATION_DATA;

/**
 * Represents deviation data represented in YANG.
 */
public class YangDeviation extends YangNode implements Parsable, YangDesc,
        YangReference, YangXPathResolver, Resolvable, CollisionDetector {

    /**
     * List of node identifiers.
     */
    private List<YangAtomicPath> targetNode;

    /**
     * Description of augment.
     */
    private String description;

    /**
     * Reference of the YANG augment.
     */
    private String reference;

    /**
     * Represents deviate-not-supported statement
     */
    private boolean isDeviateNotSupported;

    /**
     * Status of resolution. If completely resolved enum value is "RESOLVED",
     * if not enum value is "UNRESOLVED", in case
     * reference of grouping/typedef is added to uses/type but it's not
     * resolved value of enum should be
     * "INTRA_FILE_RESOLVED".
     */
    private ResolvableStatus resolvableStatus;

    /**
     * Creates a specific type of node.
     *
     * @param type              of YANG node
     * @param ysnContextInfoMap YSN context info map
     */
    public YangDeviation(YangNodeType type, Map<YangSchemaNodeIdentifier,
            YangSchemaNodeContextInfo> ysnContextInfoMap) {
        super(type, ysnContextInfoMap);
        targetNode = new LinkedList<>();
        resolvableStatus = ResolvableStatus.UNRESOLVED;
    }

    @Override
    public YangSchemaNodeType getYangSchemaNodeType() {
        return YANG_NON_DATA_NODE;
    }

    @Override
    public String getJavaPackage() {
        return null;
    }

    @Override
    public String getJavaClassNameOrBuiltInType() {
        return null;
    }

    @Override
    public String getJavaAttributeName() {
        return null;
    }

    @Override
    public void addToChildSchemaMap(YangSchemaNodeIdentifier schemaNodeIdentifier,
                                    YangSchemaNodeContextInfo yangSchemaNodeContextInfo)
            throws DataModelException {
    }

    @Override
    public void incrementMandatoryChildCount() {

    }

    @Override
    public void addToDefaultChildMap(YangSchemaNodeIdentifier yangSchemaNodeIdentifier,
                                     YangSchemaNode yangSchemaNode) {

    }

    /**
     * Returns the augmented node.
     *
     * @return the augmented node
     */
    public List<YangAtomicPath> getTargetNode() {
        return targetNode;
    }

    /**
     * Sets the augmented node.
     *
     * @param nodeIdentifiers the augmented node
     */
    public void setTargetNode(List<YangAtomicPath> nodeIdentifiers) {
        targetNode = nodeIdentifiers;
    }

    /**
     * Returns the textual reference.
     *
     * @return the reference
     */
    @Override
    public String getReference() {
        return reference;
    }

    /**
     * Sets the textual reference.
     *
     * @param reference the reference to set
     */
    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     *
     * @param description set the description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public YangConstructType getYangConstructType() {
        return DEVIATION_DATA;
    }

    @Override
    public void validateDataOnEntry() throws DataModelException {

    }

    @Override
    public void validateDataOnExit() throws DataModelException {

    }

    public boolean isDeviateNotSupported() {
        return isDeviateNotSupported;
    }

    public void setDeviateNotSupported(boolean deviateNotSupported) {
        isDeviateNotSupported = deviateNotSupported;
    }

    @Override
    public ResolvableStatus getResolvableStatus() {
        return resolvableStatus;
    }

    @Override
    public void setResolvableStatus(ResolvableStatus resolvableStatus) {
        this.resolvableStatus = resolvableStatus;
    }

    @Override
    public Object resolve() throws DataModelException {
        // Resolving of target node is being done in XPathLinker.
        return null;
    }
    @Override
    public void detectCollidingChild(String identifierName,
                                     YangConstructType dataType)
            throws DataModelException {
        detectCollidingChildUtil(identifierName, dataType, this);
    }

    @Override
    public void detectSelfCollision(String identifierName,
                                    YangConstructType dataType)
            throws DataModelException {
        if (getName().equals(identifierName)) {
            throw new DataModelException(
                    "YANG file error: Duplicate input identifier detected, " +
                            "same as input \"" +
                            getName() + " in " +
                            getLineNumber() + " at " +
                            getCharPosition() +
                            " in " + getFileName() + "\"");
        }
    }
}
