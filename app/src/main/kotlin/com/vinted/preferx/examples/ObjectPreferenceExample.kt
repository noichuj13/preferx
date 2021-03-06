package com.vinted.preferx.examples

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.vinted.preferx.PreferxSerializer
import com.vinted.preferx.objectPreference
import kotlinx.android.synthetic.main.activity_object.*
import java.lang.reflect.Type

class ObjectPreferenceExample : Activity() {

    private val sharedPreferences by lazy {
        getSharedPreferences("app-preferences", Context.MODE_PRIVATE)
    }

    private val gson by lazy {
        Gson()
    }


    private val objectPreference by lazy {
        sharedPreferences.objectPreference(
                name = "foo",
                defaultValue = Foo(bar = 0L),
                serializer = GsonSerializer(gson),
                clazz = Foo::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)

        update_foo.setOnClickListener {
            objectPreference.set(Foo(bar = System.currentTimeMillis()))
            updateFooValue()
        }

        delete_foo.setOnClickListener {
            objectPreference.delete()
            updateFooValue()
        }

        updateFooValue()
    }

    private fun updateFooValue() {
        foo_value.text = objectPreference.get().toString()
    }

    internal data class Foo(val bar: Long)

    internal class GsonSerializer<T : Any>(
            private val gson: Gson
    ) : PreferxSerializer<T> {
        override fun toString(value: T, type: Type): String {
            return gson.toJson(value, type)
        }

        override fun fromString(string: String, type: Type): T {
            return gson.fromJson(string, type)
        }
    }
}