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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/*-
 *  Reference RFC 6020.
 *
 *  The pattern Statement
 *
 *  The "pattern" statement, which is an optional sub-statement to the
 *  "type" statement, takes as an argument a regular expression string.
 *  It is used to restrict the built-in type "string", or types derived
 *  from "string", to values that match the pattern.
 *
 *  If the type has multiple "pattern" statements, the expressions are
 *  ANDed together, i.e., all such expressions have to match.
 *
 *  If a pattern restriction is applied to an already pattern-restricted
 *  type, values must match all patterns in the base type, in addition to
 *  the new patterns.
 *  The pattern's sub-statements
 *
 *   +---------------+---------+-------------+
 *   | substatement  | section | cardinality |
 *   +---------------+---------+-------------+
 *   | description   | 7.19.3  | 0..1        |
 *   | error-app-tag | 7.5.4.2 | 0..1        |
 *   | error-message | 7.5.4.1 | 0..1        |
 *   | reference     | 7.19.4  | 0..1        |
 *   +---------------+---------+-------------+
 */

/**
 * Represents pattern restriction information. The regular expression restriction on string
 * data type.
 */
public class YangPatternRestriction extends DefaultLocationInfo
        implements Serializable, YangAppErrorHolder {

    private static final long serialVersionUID = 806201649L;

    /**
     * Pattern restriction defined for the current type.
     */
    private final List<String> patternList;

    /**
     * YANG application error information.
     */
    private YangAppErrorInfo yangAppErrorInfo;

    /**
     * Creates a YANG pattern restriction object.
     */
    public YangPatternRestriction() {
        patternList = new LinkedList<>();
        yangAppErrorInfo = new YangAppErrorInfo();
    }

    /**
     * Returns the pattern restriction defined for the current type.
     *
     * @return pattern restriction defined for the current type.
     */
    public List<String> getPatternList() {
        return unmodifiableList(patternList);
    }

    /**
     * Adds a new pattern to the list of pattern restriction.
     *
     * @param newPattern pattern restriction.
     */
    public void addPattern(String newPattern) {
        patternList.add(newPattern);
    }

    @Override
    public void setAppErrorInfo(YangAppErrorInfo yangAppErrorInfo) {
        this.yangAppErrorInfo = yangAppErrorInfo;
    }

    @Override
    public YangAppErrorInfo getAppErrorInfo() {
        return yangAppErrorInfo;
    }
}
