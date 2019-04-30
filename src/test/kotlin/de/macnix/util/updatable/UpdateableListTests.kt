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

import com.jayway.awaitility.Awaitility.await
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@DisplayName("An UpdatableList")
class UpdateableListTests {
    private val log: Logger = LoggerFactory.getLogger(UpdateableListTests::class.java)

    @Test
    @DisplayName("it should publish the addition and removal of single element list items")
    fun publishIndividuallyAddedElements() {
        val ul = UpdatableList<String>()
        val listChangesFlux = ul.listChanges()

        val o = AtomicInteger(0)
        val receivedElements = mutableListOf<Pair<String, UpdatableList.ListChange>>()
        listChangesFlux.subscribe({ item ->
            log.debug("element received: {}", item)
            receivedElements += item
            o.incrementAndGet()
        }, { t -> log.debug("error: {}", t.message) }, { log.debug("completed") })
        ul.updateList(listOf("1"))
        ul.updateList(listOf("2"))

        await().atMost(5, TimeUnit.SECONDS).until<Boolean> { o.get() == 3 }
        assertThat(receivedElements).contains(
                Pair("1", UpdatableList.ListChange.ELEMENT_ADDED),
                Pair("1", UpdatableList.ListChange.ELEMENT_REMOVED),
                Pair("2", UpdatableList.ListChange.ELEMENT_ADDED))
    }

    @Test
    @DisplayName("it should publish the addition of all elements of multi item list")
    fun publishBulkAddedElements() {
        val ul = UpdatableList<String>()
        val listChangesFlux = ul.listChanges()

        val o = AtomicInteger(0)
        val receivedElements = mutableListOf<Pair<String, UpdatableList.ListChange>>()
        val disposable = listChangesFlux.subscribe { item ->
            log.debug("element received: {}", item)
            receivedElements += item
            o.incrementAndGet()
        }
        ul.updateList(listOf("1", "2", "3"))

        await().atMost(5, TimeUnit.SECONDS).until<Boolean> { o.get() == 3 }
        disposable.dispose()
        assertThat(receivedElements).contains(Pair("1", UpdatableList.ListChange.ELEMENT_ADDED), Pair("2", UpdatableList.ListChange.ELEMENT_ADDED), Pair("3", UpdatableList.ListChange.ELEMENT_ADDED))
    }

    @Test
    @DisplayName("it should publish the initial list elements given by the constructor when a consumer subscribes")
    fun publishCurrentElementsOnSubscription() {
        val ul = UpdatableList<String>(listOf("1", "2", "3"))
        val listChangesFlux = ul.listChanges()

        val o = AtomicInteger(0)
        val receivedElements = mutableListOf<Pair<String, UpdatableList.ListChange>>()
        listChangesFlux.subscribe { item ->
            log.debug("element received: {}", item)
            receivedElements += item
            o.incrementAndGet()
        }

        await().atMost(5, TimeUnit.SECONDS).until<Boolean> { o.get() >= 3 }
        assertThat(receivedElements).contains(Pair("1", UpdatableList.ListChange.ELEMENT_ADDED), Pair("2", UpdatableList.ListChange.ELEMENT_ADDED), Pair("3", UpdatableList.ListChange.ELEMENT_ADDED))
    }

    @Test
    @DisplayName("it should add and remove elements from the initial list after calling updateList()")
    fun addsIndividualElementsToListAfterSubscription() {
        val ul = UpdatableList<String>(listOf("1", "2", "3"))
        val listChangesFlux = ul.listChanges()

        val counter = AtomicInteger(0)
        val receivedElements = mutableListOf<Pair<String, UpdatableList.ListChange>>()
        listChangesFlux.subscribe { item ->
            log.debug("element received: {}", item)
            receivedElements += item
            counter.incrementAndGet()
        }
        await().atMost(5, TimeUnit.SECONDS).until<Boolean> { counter.get() == 3 }
        receivedElements.clear()
        counter.set(0)
        ul.updateList(listOf("2", "3", "4"))

        await().atMost(5, TimeUnit.SECONDS).until<Boolean> { counter.get() == 2 }
        assertThat(receivedElements).contains(
                Pair("1", UpdatableList.ListChange.ELEMENT_REMOVED),
                Pair("4", UpdatableList.ListChange.ELEMENT_ADDED))
    }


}