package com.vimers.smartblock.persistence

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Container class which simplifies saving and loading serializable objects
 * utilizing the Shared Preferences mechanism and GSON library for
 * human-readable data representation.
 */
class PersistentObject<T : Any>(
        private val context: Context,
        private val type: KClass<T>,
        private val name: String
) {
    private companion object {
        /** Shared Preferences file name where all persistent objects reside. */
        const val PREFS_FILE = "persistent_objects"
    }

    constructor(context: Context, type: Class<T>, name: String) : this(context, type.kotlin, name)
    constructor(context: Context, type: KClass<T>) : this(
            context,
            type,
            type.simpleName ?: throw IllegalArgumentException(
                    "could not get the qualified name of the passed KClass instance, " +
                            "is it anonymous?"
            )
    )

    constructor(context: Context, type: Class<T>) : this(context, type.kotlin)

    private val gson = Gson()
    private val prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    /** The boxed object. */
    lateinit var obj: T

    init {
        reload()
    }

    /**
     * Reloads the boxed object from the persistent storage.
     * If there is no serialized object in the persistent storage,
     * a new instance of `T` will be created using the parameterless constructor.
     */
    fun reload() {
        val serializedObj = prefs.getString(name, null)
        obj = try {
            gson.fromJson(serializedObj, type.java) ?: type.createInstance()
        } catch (e: JsonSyntaxException) {
            type.createInstance()
        }
    }

    /** Saves the boxed object to the persistent storage. */
    fun save() {
        val json = gson.toJson(obj, type.java)
        prefs.edit().putString(name, json).apply()
    }

    /**
     * Allows fancy editing of the boxed object with saving it afterwards.
     *
     * @sample sample
     */
    fun edit(editor: T.() -> Unit): PersistentObject<T> {
        obj.editor()
        return this
    }
}

private fun sample(context: Context) {
    class Test {
        var property = ""
    }

    val persistentObject = PersistentObject<Test>(context, Test::class)
    persistentObject.edit {
        property = "Hello"
    }.save()
}