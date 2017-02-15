/*
 *  Jenkins Pipeline Goodness
 *
 *  Copyright (c) 2017 DoubleData Ltd. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * Generate salt configuration
 */
String generateConfig(Map map) {
    return getMapBlock(map, 0)
}

/**
 * Write salt configuration
 */
void writeConfig(String file, Map map) {
    writeFile encoding: 'UTF-8', file: file, text: generateConfig(map)
}

String getArrayBlock(List list, int level) {
    def output = ""
    def iValue = 0
    while (iValue < list.size()) {
        def value = list[iValue]
        if (Map.isAssignableFrom(value.getClass())) {
            output = output + "  " * (level + 1) + "-\n" + getMapBlock(value[i], level + 2)
        } else if (Collection.isAssignableFrom(value.getClass()) || Object[].isAssignableFrom(value.getClass())) {
            output = output + "  " * level + "-\n" + getArrayBlock(value, level + 1)
        } else {
            output = output + "  " * (level + 1) + "- '$value'\n"
        }
        iValue++
    }
    return output
}

String getMapBlock(Map map, int level) {
    def output = ""
    def keys = map.keySet()
    def iKey = 0
    while (iKey < keys.size()) {
        def key = keys[iKey]
        def value = map[key]
        if (Map.isAssignableFrom(value.getClass())) {
            output = output + "  " * level + "'$key':\n" + getMapBlock(value, level + 1)
        } else if (Collection.isAssignableFrom(value.getClass()) || Object[].isAssignableFrom(value.getClass())) {
            output = output + "  " * level + "'$key':\n" + getArrayBlock(value, level + 1)
        } else {
            output = output + "  " * level + "'$key':" + value
        }
        output = output + "\n"
        iKey++
    }
    return output
}

return this;
