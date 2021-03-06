/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jabylon.properties;

import java.util.Map;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property File</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jabylon.properties.PropertyFile#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.jabylon.properties.PropertyFile#getLicenseHeader <em>License Header</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jabylon.properties.PropertiesPackage#getPropertyFile()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface PropertyFile extends CDOObject {
    /**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.jabylon.properties.Property}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.jabylon.properties.PropertiesPackage#getPropertyFile_Properties()
	 * @model containment="true"
	 * @generated
	 */
    EList<Property> getProperties();

    /**
	 * Returns the value of the '<em><b>License Header</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>License Header</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>License Header</em>' attribute.
	 * @see #setLicenseHeader(String)
	 * @see org.jabylon.properties.PropertiesPackage#getPropertyFile_LicenseHeader()
	 * @model
	 * @generated
	 */
    String getLicenseHeader();

    /**
	 * Sets the value of the '{@link org.jabylon.properties.PropertyFile#getLicenseHeader <em>License Header</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>License Header</em>' attribute.
	 * @see #getLicenseHeader()
	 * @generated
	 */
    void setLicenseHeader(String value);

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    Property getProperty(String key);

    /**
	 * <!-- begin-user-doc -->
	 * Returns a map view of the properties file.
	 * If possible this will be a cached version
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    Map<String, Property> asMap();

} // PropertyFile
