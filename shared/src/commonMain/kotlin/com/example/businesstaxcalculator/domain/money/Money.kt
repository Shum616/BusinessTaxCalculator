package com.example.businesstaxcalculator.domain.money

import kotlin.jvm.JvmInline

@JvmInline
value class Money(val kopiyky: Long) : Comparable<Money> {
    operator fun plus(other: Money) = Money(checkedAdd(kopiyky, other.kopiyky))
    operator fun minus(other: Money) = Money(checkedSubtract(kopiyky, other.kopiyky))

    fun percent(percent: Int): Money {
        require(percent >= 0)
        val multiplied = checkedMultiply(kopiyky, percent.toLong())
        return Money(divideRounded(multiplied, 100L))
    }

    override fun compareTo(other: Money) = kopiyky.compareTo(other.kopiyky)

    companion object {
        val ZERO = Money(0)

        fun fromHryvnias(hryvnias: Long) = Money(checkedMultiply(hryvnias, 100L))

        fun parse(text: String): Money? = parseFixedPoint(text, FRACTION_DIGITS)?.let(::Money)
    }
}

@JvmInline
value class ExchangeRate(val scaledValue: Long) {
    fun toPlainString(): String {
        val whole = scaledValue / SCALE
        val fraction = (scaledValue % SCALE).toString().padStart(FRACTION_DIGITS, '0').trimEnd('0')
        return if (fraction.isEmpty()) whole.toString() else "$whole.$fraction"
    }

    companion object {
        const val SCALE = 10_000L
        const val FRACTION_DIGITS = 4

        fun parse(text: String): ExchangeRate? =
            parseFixedPoint(text, FRACTION_DIGITS)?.let(::ExchangeRate)
    }
}

private const val FRACTION_DIGITS = 2

private fun parseFixedPoint(text: String, fractionDigits: Int): Long? {
    val normalized = text.trim().replace(',', '.')
    if (!normalized.matches(Regex("\\d+(?:\\.\\d{1,$fractionDigits})?"))) return null
    val parts = normalized.split('.', limit = 2)
    val whole = parts[0].toLongOrNull() ?: return null
    val fraction = parts.getOrElse(1) { "" }.padEnd(fractionDigits, '0').toLongOrNull() ?: 0L
    val scale = powerOfTen(fractionDigits)
    return runCatching { checkedAdd(checkedMultiply(whole, scale), fraction) }.getOrNull()
}

private fun powerOfTen(exponent: Int): Long {
    var result = 1L
    repeat(exponent) { result *= 10L }
    return result
}

private fun divideRounded(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    val remainder = value % divisor
    if (kotlin.math.abs(remainder) * 2 < divisor) return quotient
    return checkedAdd(quotient, if (value >= 0) 1L else -1L)
}

private fun checkedAdd(first: Long, second: Long): Long {
    val result = first + second
    if (((first xor result) and (second xor result)) < 0) throw ArithmeticException("Long overflow")
    return result
}

private fun checkedSubtract(first: Long, second: Long): Long {
    val result = first - second
    if (((first xor second) and (first xor result)) < 0) throw ArithmeticException("Long overflow")
    return result
}

private fun checkedMultiply(first: Long, second: Long): Long {
    if (first == 0L || second == 0L) return 0L
    val result = first * second
    if (result / second != first || (first == Long.MIN_VALUE && second == -1L)) {
        throw ArithmeticException("Long overflow")
    }
    return result
}
