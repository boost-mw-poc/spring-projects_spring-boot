/*
 * Copyright 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.configurationsample.lombok;

import lombok.Data;

import org.springframework.boot.configurationsample.ConfigurationProperties;

@Data
@ConfigurationProperties("config")
@SuppressWarnings("unused")
public class LombokInnerClassWithGetterProperties {

	private final Foo first = new Foo();

	public Foo getFirst() {
		return this.first;
	}

	@Data
	public static class Foo {

		private String name;

	}

}
