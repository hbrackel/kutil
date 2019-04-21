/*
 * Copyright (c) 2019 Hans-Uwe Brackel
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.macnix.util.updatable

import de.macnix.util.updatable.UpdatableList.ListChange.ELEMENT_ADDED
import de.macnix.util.updatable.UpdatableList.ListChange.ELEMENT_REMOVED
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux

class UpdatableList<T>(initialList: List<T> = emptyList(), val isSameElement: (a: T, b: T) -> Boolean = { a, b -> a == b }, val hasSameProperties: (a: T, b: T) -> Boolean = { a, b -> a == b }) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(UpdatableList::class.java)
    }

    private val list = ArrayList<T>()
    private val listChangeProcessor: DirectProcessor<Pair<T, ListChange>> = DirectProcessor.create()

    init {
        list.addAll(initialList)
        log.debug("<init>: instantiated an UpdatableList with elements {}", list)
    }

    fun updateList(updatedList: List<T>) {
        log.trace("updateList({})", updatedList)
        val removedElements = list.filter { currentElement -> updatedList.find { isSameElement(currentElement, it) } == null }
        val addedElements = updatedList.filter { newElement -> list.find { isSameElement(newElement, it) } == null }
        list.removeAll(removedElements)
        list.addAll(addedElements)
        removedElements.forEach { element -> listChangeProcessor.onNext(Pair(element, ELEMENT_REMOVED)) }
        addedElements.forEach { element -> listChangeProcessor.onNext(Pair(element, ELEMENT_ADDED)) }
    }

    fun listChanges(): Flux<Pair<T, ListChange>> {
        log.trace("#listChanges()")
        return Flux.fromIterable(list)
                .map { Pair(it, ELEMENT_ADDED) }
                .concatWith(listChangeProcessor)
    }

    enum class ListChange {
        ELEMENT_ADDED, ELEMENT_REMOVED, ELEMENT_UPDATED
    }

}