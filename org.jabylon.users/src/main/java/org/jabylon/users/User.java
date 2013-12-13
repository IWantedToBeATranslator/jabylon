/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jabylon.users;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jabylon.users.User#getName <em>Name</em>}</li>
 *   <li>{@link org.jabylon.users.User#getPassword <em>Password</em>}</li>
 *   <li>{@link org.jabylon.users.User#getRoles <em>Roles</em>}</li>
 *   <li>{@link org.jabylon.users.User#getPermissions <em>Permissions</em>}</li>
 *   <li>{@link org.jabylon.users.User#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link org.jabylon.users.User#getType <em>Type</em>}</li>
 *   <li>{@link org.jabylon.users.User#getEmail <em>Email</em>}</li>
 *   <li>{@link org.jabylon.users.User#getToken <em>Token</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jabylon.users.UsersPackage#getUser()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface User extends CDOObject {
    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jabylon.users.UsersPackage#getUser_Name()
	 * @model annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='255'"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link org.jabylon.users.User#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Password</em>' attribute.
	 * @see #setPassword(String)
	 * @see org.jabylon.users.UsersPackage#getUser_Password()
	 * @model required="true"
	 *        annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='255'"
	 * @generated
	 */
    String getPassword();

    /**
	 * Sets the value of the '{@link org.jabylon.users.User#getPassword <em>Password</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Password</em>' attribute.
	 * @see #getPassword()
	 * @generated
	 */
    void setPassword(String value);

    /**
	 * Returns the value of the '<em><b>Roles</b></em>' reference list.
	 * The list contents are of type {@link org.jabylon.users.Role}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Roles</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Roles</em>' reference list.
	 * @see org.jabylon.users.UsersPackage#getUser_Roles()
	 * @model
	 * @generated
	 */
    EList<Role> getRoles();

    /**
	 * Returns the value of the '<em><b>Permissions</b></em>' reference list.
	 * The list contents are of type {@link org.jabylon.users.Permission}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Permissions</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Permissions</em>' reference list.
	 * @see org.jabylon.users.UsersPackage#getUser_Permissions()
	 * @model
	 * @generated
	 */
    EList<Permission> getPermissions();

    /**
	 * Returns the value of the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Display Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Name</em>' attribute.
	 * @see #setDisplayName(String)
	 * @see org.jabylon.users.UsersPackage#getUser_DisplayName()
	 * @model annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='255'"
	 * @generated
	 */
    String getDisplayName();

    /**
	 * Sets the value of the '{@link org.jabylon.users.User#getDisplayName <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Name</em>' attribute.
	 * @see #getDisplayName()
	 * @generated
	 */
    void setDisplayName(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.jabylon.users.UsersPackage#getUser_Type()
	 * @model annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='255'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link org.jabylon.users.User#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

    /**
	 * Returns the value of the '<em><b>Email</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Email</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Email</em>' attribute.
	 * @see #setEmail(String)
	 * @see org.jabylon.users.UsersPackage#getUser_Email()
	 * @model annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='255'"
	 * @generated
	 */
    String getEmail();

    /**
	 * Sets the value of the '{@link org.jabylon.users.User#getEmail <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Email</em>' attribute.
	 * @see #getEmail()
	 * @generated
	 */
    void setEmail(String value);

    /**
	 * Returns the value of the '<em><b>Token</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The generated auth token of the user (if available)
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Token</em>' attribute.
	 * @see #setToken(String)
	 * @see org.jabylon.users.UsersPackage#getUser_Token()
	 * @model annotation="http://www.eclipse.org/CDO/DBStore columnType='VARCHAR' columnLength='32'"
	 * @generated
	 */
	String getToken();

				/**
	 * Sets the value of the '{@link org.jabylon.users.User#getToken <em>Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Token</em>' attribute.
	 * @see #getToken()
	 * @generated
	 */
	void setToken(String value);

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
    EList<Permission> getAllPermissions();

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    boolean hasPermission(String permission);

} // User
