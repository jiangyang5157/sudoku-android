package com.gmail.jiangyang5157.sudoku_presenter.model

/**
 * Created by Yang Jiang on July 09, 2017
 */
data class Terminal(val e: Int) {

    val c = arrayOfNulls<Cell>(e * e)

//    fun fromJson(json: String): Boolean {
//        try {
//            val jsonObj = JSONObject(json)
//            val e = jsonObj.getInt("E")
//            if (e != this.e) {
//                return false
//            }
//
//            val c = jsonObj.getJSONArray("C")
//            if (c.length() != this.c.size) {
//                return false
//            }
//
//            for (i in 0 until c.length()) {
//                val b = c.getJSONObject(i).getInt("B")
//                val d = c.getJSONObject(i).getInt("D")
//                this.c[i] = Cell(b, d)
//            }
//        } catch (e: JSONException) {
//            return false
//        }
//
//        return true
//    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("#$e*$e")
        for (i in c.indices) {
            if (i % e == 0) {
                sb.append("\n")
            }
            sb.append("${c[i]}, ")
        }
        sb.append("\n")
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Terminal

        if (e != other.e) return false
        if (!c.contentEquals(other.c)) return false

        return true
    }

    override fun hashCode(): Int {
        var ret = 1
        val prime = 37
        ret = prime * ret + (e.toLong() xor e.toLong().ushr(32)).toInt()
        ret = prime * ret + (c.hashCode().toLong() xor c.hashCode().toLong().ushr(32)).toInt()
        return ret
    }

}