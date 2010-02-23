/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.grrrowl.impl.hawtjni;


/**
 * Represents an Objective-C <tt>selector</tt> type.
 *
 * @author spleaner
 * @since 1.0
 */
public class Selector
    extends NativeLong
{
    private String myName;

    public Selector() {
        this("undefined selector", 0);
    }

    public Selector(long value) {
        this("undefined selector", value);
    }

    public Selector(String name, long value) {
        super(value);
        myName = name;
    }


    public String getName() {
        return myName;
    }

    @Override
    public String toString() {
        return String.format("[Selector %s]", myName);
    }

    public Selector initName(final String name) {
        myName = name;
        return this;
    }
}
