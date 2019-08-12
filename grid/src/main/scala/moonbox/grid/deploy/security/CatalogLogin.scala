/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.security

import moonbox.catalog.{JdbcCatalog, PasswordEncryptor}
import moonbox.common.MbConf

class CatalogLogin(conf: MbConf) extends Login {
	private val catalog = new JdbcCatalog(conf)

	Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
		override def run(): Unit = {
			if (catalog != null) {
				catalog.close()
			}
		}
	}))

	override def doLogin(org: String, username: String, password: String): Boolean = {
		catalog.getUserOption(org, username) match {
			case Some(user) if user.password == PasswordEncryptor.encryptSHA(password) => true
			case _ => false
		}
	}
}
