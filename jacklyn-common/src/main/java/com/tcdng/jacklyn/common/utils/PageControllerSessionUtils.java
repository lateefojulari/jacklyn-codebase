/*
 * Copyright 2018 The Code Department
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.common.utils;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.PageController;

/**
 * Page controller session utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface PageControllerSessionUtils extends UnifyComponent {

	/**
	 * Loads session for supplied controller using data object
	 * 
	 * @param pageController
	 *            the page controller
	 * @throws UnifyException
	 *             if an error occurs
	 */
	<T extends Entity> void loadSession(PageController pageController) throws UnifyException;

	/**
	 * Clears session for supplied controller.
	 * 
	 * @param pageController
	 *            the page controller
	 * @throws UnifyException
	 *             if an error occurs
	 */
	void unloadSession(PageController pageController) throws UnifyException;
}
