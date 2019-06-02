package maderski.chargingindicator.delegatedproperties

import com.google.gson.reflect.TypeToken
import java.lang.IllegalStateException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Only allows a variable to be set once. Behaves similar to the lazy delegated property, except
 * this doesn't require a lambda.
 */
class SetOnceDelegate<T : Any>(private val typeToken: TypeToken<T>) : ReadWriteProperty<Any, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (value == null) {
            throw IllegalStateException("Value of ${typeToken.type} has NOT been set!")
        } else {
            return value!!
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (this.value == null) {
            this.value = value
        }
    }
}

inline fun <reified T : Any> setOnceOf(): SetOnceDelegate<T> = SetOnceDelegate(object : TypeToken<T>() {})