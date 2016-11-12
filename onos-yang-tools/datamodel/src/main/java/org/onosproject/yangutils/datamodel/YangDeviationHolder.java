/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.onosproject.yangutils.datamodel;

public interface YangDeviationHolder {

    boolean isDeviatedNodeCloned();

    void setDeviatedNodeCloned(boolean deviatedNodeCloned);

    boolean isModuleForDeviation();

    void setModuleForDeviation(boolean moduleForDeviation);
}
